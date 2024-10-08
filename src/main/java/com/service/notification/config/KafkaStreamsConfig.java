package com.service.notification.config;

import com.service.notification.records.Notification;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.Properties;

@EnableKafkaStreams
public class KafkaStreamsConfig {
    @Bean
    public StreamsConfig kafkaStreamsConfig() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "notification-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new StreamsConfig(props);
    }

    @Bean
    public KStream<String, Notification> notificationStream(StreamsBuilder streamsBuilder) {
        return streamsBuilder.stream("notifications");
    }
}
