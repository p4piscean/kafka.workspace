package com.dy.spring.kafka.service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

//@Service
public class QuoteConsumer {
	private final Logger logger = LoggerFactory.getLogger(QuoteConsumer.class);

	@KafkaListener(topics = "quote", groupId = "group_id")
	public void consume(String message) throws IOException {
		logger.info(String.format("#### -> Consumed message -> %s", message));
	}

	public CountDownLatch latch = new CountDownLatch(3);
	public CountDownLatch partitionLatch = new CountDownLatch(2);
	public CountDownLatch filterLatch = new CountDownLatch(2);
	public CountDownLatch greetingLatch = new CountDownLatch(1);

	@KafkaListener(topics = "${message.topic.name}", groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory")
	public void listenGroupFoo(String message) {
		System.out.println("Received Message in group 'foo': " + message);
		latch.countDown();
	}

	@KafkaListener(topics = "${message.topic.name}", groupId = "bar", containerFactory = "barKafkaListenerContainerFactory")
	public void listenGroupBar(String message) {
		System.out.println("Received Message in group 'bar': " + message);
		latch.countDown();
	}

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "headersKafkaListenerContainerFactory")
	public void listenWithHeaders(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		System.out.println("Received Message: " + message + " from partition: " + partition);
		latch.countDown();
	}

	@KafkaListener(topicPartitions = @TopicPartition(topic = "${partitioned.topic.name}", partitions = { "0",
			"3" }), containerFactory = "partitionsKafkaListenerContainerFactory")
	public void listenToPartition(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		System.out.println("Received Message: " + message + " from partition: " + partition);
		this.partitionLatch.countDown();
	}

	@KafkaListener(topics = "${filtered.topic.name}", containerFactory = "filterKafkaListenerContainerFactory")
	public void listenWithFilter(String message) {
		System.out.println("Received Message in filtered listener: " + message);
		this.filterLatch.countDown();
	}

	@KafkaListener(topics = "${greeting.topic.name}", containerFactory = "greetingKafkaListenerContainerFactory")
	public void greetingListener(String greeting) {
		System.out.println("Received greeting message: " + greeting);
		this.greetingLatch.countDown();
	}

}
