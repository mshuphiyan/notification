//package com.service.notification.controller;
//
//import com.service.notification.records.SMSNotification;
//import com.service.notification.service.NotificationService;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import reactor.core.publisher.Mono;
//
//
//@RestController
//@RequestMapping("/api")
//public class SMSNotificationController {
//
//    private final NotificationService<SMSNotification> smsNotificationService;
//
//    public SMSNotificationController(NotificationService<SMSNotification> smsNotificationService) {
//        this.smsNotificationService = smsNotificationService;
//    }
//
//    @PostMapping("/sms")
//    public Mono<ResponseEntity<Void>> sendSMSNotification(@RequestBody SMSNotification smsNotification) {
//        return smsNotificationService.sendNotification(
//                () -> smsNotification,
//                n -> {
//                    smsNotificationService.kafkaTemplate.send(new ProducerRecord<>("sms-notifications", n));
//                    // Additional SMS processing logic could be added here
//                }
//        ).then(Mono.just(ResponseEntity.ok().build()));
//    }
//}
