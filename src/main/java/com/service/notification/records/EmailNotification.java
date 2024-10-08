package com.service.notification.records;

public record EmailNotification(String recipient, String subject, String body) implements Notification {}

