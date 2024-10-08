package com.service.notification.records;

public record SMSNotification(String phoneNumber, String message) implements Notification {}