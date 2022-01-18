package com.dy.spring.kafka.app;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import lombok.extern.slf4j.Slf4j;

//@SpringBootApplication
//@Slf4j
public class ConsumerApplication implements CommandLineRunner {
	 //private static final Logger logger = LoggerFactory.getLogger(ConsumerApplication.class);
	 
	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
	
	@KafkaListener(topics = "topicName", groupId = "foo")
	public void listenGroupFoo(String message) {
	    System.out.println("Received Message in group foo: " + message);
	}
	
	@KafkaListener(topics = "topicName")
	public void listenWithHeaders(
	  @Payload String message, 
	  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
	      System.out.println(
	        "Received Message: " + message + "from partition: " + partition);
	}

	
	@KafkaListener(
			  topicPartitions = @TopicPartition(topic = "topicName",
			  partitionOffsets = {
			    @PartitionOffset(partition = "0", initialOffset = "0"), 
			    @PartitionOffset(partition = "3", initialOffset = "0")}),
			  containerFactory = "partitionsKafkaListenerContainerFactory")
			public void listenToPartition(
			  @Payload String message, 
			  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
			      System.out.println(
			        "Received Message: " + message + "from partition: " + partition);
			}
	@Bean
	public NewTopic topic() {
		return TopicBuilder.name("quote").partitions(10).replicas(1).build();
	}

	@KafkaListener(id = "myId", topics = "quote")
	public void listen(String in) {
		System.out.println(in);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
