package com.service.notification.entities;

import com.service.notification.util.NotificationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String type;
    private String content;
    private NotificationStatus status;

    public NotificationEntity(String type, String content) {
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
    }

    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
    }

    public void markAsRead() {
        this.status = NotificationStatus.READ;
    }

    public void markAsNotSent() {
        this.status = NotificationStatus.valueOf("not sent");
    }
}
