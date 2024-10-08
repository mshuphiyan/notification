package com.service.notification.processor;

import com.service.notification.records.NotificationMessage;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class NotificationMessageSerde extends Serdes.WrapperSerde<NotificationMessage>{
    public NotificationMessageSerde(Serializer<NotificationMessage> serializer, Deserializer<NotificationMessage> deserializer) {
        super(serializer, deserializer);
    }
}
