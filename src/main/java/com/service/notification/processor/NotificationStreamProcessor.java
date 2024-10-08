package com.service.notification.processor;

import com.service.notification.records.NotificationMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@EnableKafkaStreams
public class NotificationStreamProcessor implements ApplicationRunner {
    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Autowired
    public NotificationStreamProcessor(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception{
        StreamsBuilder builder = streamsBuilderFactoryBean.getObject();
        KStream<String, NotificationMessage> notificationStream = builder.stream("notifications-input",
                Consumed.with(Serdes.String(), /* your NotificationMessage serde */));

        notificationStream.foreach((key, notification) -> {
            // Process notification (e.g., send to WebSocket, save to DB, etc.)
            // You can mark as sent or not based on your processing logic
            System.out.println("Processing notification: " + notification);
            // Implement your logic here (like saving to the DB, etc.)
        });

        KafkaStreams streams = new KafkaStreams(builder.build(), createStreamsConfig());
        streams.start();
    }

    private Properties createStreamsConfig() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "notification-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return props;
    }

}
