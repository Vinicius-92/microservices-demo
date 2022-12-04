package com.microservices.demo.kafka.producer.config;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaProducerConfigData;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducerConfigData producerConfigData;

    public KafkaProducerConfig(KafkaConfigData kafkaConfigData,
                               KafkaProducerConfigData producerConfigData) {
        this.kafkaConfigData = kafkaConfigData;
        this.producerConfigData = producerConfigData;
    }

    @Bean
    public Map<String, Object> producerConfig() {
        Map<String, Object> p = new HashMap<>();
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        p.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerConfigData.getKeySerializerClass());
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerConfigData.getValueSerializerClass());
        p.put(ProducerConfig.BATCH_SIZE_CONFIG, producerConfigData.getBatchSize() *
                producerConfigData.getBatchSizeBoostFactor());
        p.put(ProducerConfig.LINGER_MS_CONFIG, producerConfigData.getLingerMs());
        p.put(ProducerConfig.ACKS_CONFIG, producerConfigData.getAcks());
        p.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerConfigData.getRequestTimeoutMs());
        p.put(ProducerConfig.RETRIES_CONFIG, producerConfigData.getRetryCount());
        return p;
    }

    @Bean
    public ProducerFactory<K, V> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<K, V> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
