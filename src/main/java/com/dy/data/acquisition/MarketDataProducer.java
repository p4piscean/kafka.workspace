package com.dy.data.acquisition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.dy.spring.kafka.config.KafkaProducerConfig;
import com.dy.spring.kafka.config.KafkaTopicConfig;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MarketDataProducer {
	//private static final Logger logger = LoggerFactory.getLogger(MarketDataProducer.class);
	
	@Autowired
	private KafkaTopicConfig kafkaTopicConfig;
	
	@Autowired
	private KafkaProducerConfig kafkaProducerConfig;
	

	//@PostConstruct
	public void runAfterObjectCreated() {
		log.info("runAfterObjectCreated: PostContruct method called");
	}
	
    public void sendMessageWithCallback(String message, String topicName) {
    	log.info("sendMessage: sending message - {}", message);

        ListenableFuture<SendResult<String, String>> future = kafkaProducerConfig.kafkaTemplate().send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata()
                    .offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendStockData(String message, int partition) {    	
    	kafkaProducerConfig.kafkaTemplate().send(kafkaTopicConfig.topics[0], partition, null, message);
    }

    public void sendFixedIncomeData(String message) {
    	kafkaProducerConfig.kafkaTemplate().send(kafkaTopicConfig.topics[1], message);
    }

    public void sendForeignExchangeData(String message) {
    	kafkaProducerConfig.kafkaTemplate().send(kafkaTopicConfig.topics[0], message);
    }
}

