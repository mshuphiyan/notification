package com.service.notification.repository;

import com.service.notification.entities.NotificationEntity;
import com.service.notification.records.NotificationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Optional<NotificationEntity> findById(Long id);

    Optional<NotificationEntity> findById(NotificationMessage notificationId);
}
