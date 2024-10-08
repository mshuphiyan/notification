package com.service.notification.service;

import com.service.notification.entities.NotificationEntity;
import com.service.notification.records.Notification;
import com.service.notification.records.NotificationMessage;
import com.service.notification.repository.NotificationRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class NotificationService<T extends NotificationMessage> {
    public final KafkaTemplate<String, Notification> kafkaTemplate;
    public final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    public NotificationService(KafkaTemplate<String, Notification> kafkaTemplate,
                               SimpMessagingTemplate messagingTemplate,
                               NotificationRepository notificationRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public Mono<Void> sendNotification(Supplier<T> notificationSupplier, Consumer<T> notificationProcessor) {
        T notification = notificationSupplier.get();
        Objects.requireNonNull(notification, "Notification cannot be null");
        return Mono.fromRunnable(() -> {
            try {
                // Process and send the notification
                notificationProcessor.accept(notification);

                // Mark notification as sent
                NotificationEntity notificationEntity = new NotificationEntity(notification.type(), notification.content());
                notificationEntity.markAsSent();
                notificationRepository.save(notificationEntity);

                // Call markAsRead if sending was successful
                markAsRead(notification);  // Assuming NotificationMessage has a getId() method
            } catch (Exception e) {
                // Update status to not sent on error
                NotificationEntity notificationEntity = new NotificationEntity(notification.type(), notification.content());
                notificationEntity.markAsNotSent();
                notificationRepository.save(notificationEntity);
                throw e;  // Re-throw the exception for retry
            }
        });
    }

    @Recover
    public void recover(Exception e, Supplier<T> notificationSupplier) {
        T notification = notificationSupplier.get();
        NotificationEntity notificationEntity = new NotificationEntity(notification.type(), notification.content());
        notificationEntity.markAsFailed();
        notificationRepository.save(notificationEntity);

        System.err.println("Failed to send notification: " + e.getMessage());
    }

    public void markAsRead(T notificationId) {
        Optional<NotificationEntity> notificationEntityOpt = notificationRepository.findById(notificationId);
        notificationEntityOpt.ifPresentOrElse(
                notificationEntity -> {
                    notificationEntity.markAsRead();
                    notificationRepository.save(notificationEntity);
                },
                () -> System.err.println("Notification not found for ID: " + notificationId)
        );
    }
}
