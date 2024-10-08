package com.service.notification.records;

public record PushNotification(String userId, String message) implements Notification {}