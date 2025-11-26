package com.document.search.kafka;


import document.search.avro.model.DocumentIndexEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@Service
public class IndexEventProducer {

    @Value("${topic.product}")
    private String topic;

    private final KafkaTemplate<String, DocumentIndexEvent> kafkaTemplate;

    public IndexEventProducer(KafkaTemplate<String, DocumentIndexEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(DocumentIndexEvent indexEvent){
        log.info("PRODUCING:" + indexEvent.getBody().getDocumentId());
        try {
            kafkaTemplate.send(
                    topic,
                    indexEvent.getBody().getTenentId(),
                    indexEvent
            );
        } catch (SerializationException e){
            log.error("KAFKA Serialization ERROR:" + e.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();
            log.error(sStackTrace);
        } catch (Exception e){
            log.error("KAFKA ERROR:" + e.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();
            log.error(sStackTrace);
        }
    }
}