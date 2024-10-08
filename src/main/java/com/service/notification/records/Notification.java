package com.service.notification.records;

public sealed interface Notification permits EmailNotification, PushNotification, SMSNotification {
}
