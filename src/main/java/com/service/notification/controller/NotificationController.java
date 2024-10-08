package com.service.notification.controller;

import com.service.notification.records.Notification;
import com.service.notification.records.NotificationMessage;
import com.service.notification.records.PushNotification;
import com.service.notification.service.NotificationService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {


    private NotificationService<NotificationMessage> notificationService;

    public NotificationController(NotificationService<NotificationMessage> notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> sendNotification(@RequestBody NotificationMessage notificationMessage) {
        return notificationService.sendNotification(
                () -> notificationMessage,
                n -> {
//                    kafkaTemplate.send("notifications", notification.type(), notification);
//                    // Example of handling WebSocket notifications based on type
//                    if (notification instanceof PushNotification push) {
//                        messagingTemplate.convertAndSend("/topic/notifications/" + push.getUserId(), push);
//                    }
                }
        ).then(Mono.just(ResponseEntity.ok().build()));
    }
}
