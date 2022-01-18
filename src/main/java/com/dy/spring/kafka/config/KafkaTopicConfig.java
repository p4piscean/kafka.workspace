package com.dy.spring.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class KafkaTopicConfig {

	@Value("${spring.kafka.topics}")
	public String[] topics;

	@Bean
	public NewTopic getStockTopic() {
		return TopicBuilder.name(topics[0]).partitions(3).replicas(1).compact().build();
	}

	@Bean
	public NewTopic getFiTopic() {
		return new NewTopic(topics[1], 2, (short) 1);
	}

	@Bean
	public NewTopic getDerivativesTopic() {
		return new NewTopic(topics[2], 1, (short) 1);
	}

	@Bean
	public NewTopic getFxTopic() {
		return new NewTopic(topics[3], 3, (short) 1);
	}

	@Bean
	public NewTopic getCommoditiesTopic() {
		return new NewTopic(topics[4], 1, (short) 1);
	}

}