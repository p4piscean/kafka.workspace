package com.dy.spring.kafka.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;

import com.dy.request.subscribe.StockTicker;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableAutoConfiguration
@EnableKafka
@ComponentScan(basePackages = { "com.dy" })
@PropertySource("application.yml")
@EnableConfigurationProperties
@Slf4j
@Configuration
public class KafkaProducerApplication implements CommandLineRunner {

	@Autowired
	private StockTicker stockTicker;

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(KafkaProducerApplication.class, args);

		log.info("main - Simple log statement with inputs {}, {} and {}", 1, 2, 3);

		// MarketDataProducer marketDataProducer =
		// context.getBean(MarketDataProducer.class);
		// QuoteConsumer listener = context.getBean(QuoteConsumer.class);

		/*
		 * Sending a Hello World message to topic 'baeldung'. Must be received by both
		 * listeners with group foo and bar with containerFactory
		 * fooKafkaListenerContainerFactory and barKafkaListenerContainerFactory
		 * respectively. It will also be received by the listener with
		 * headersKafkaListenerContainerFactory as container factory.
		 */
		// marketDataProducer.sendFixedIncomeData("Hello NASDAQ, How are you?");
		// listener.latch.await(10, TimeUnit.SECONDS);
		/*
		 * Sending message to a topic with 5 partitions, each message to a different
		 * partition. But as per listener configuration, only the messages from
		 * partition 0 and 3 will be consumed.
		 */

		// listener.partitionLatch.await(10, TimeUnit.SECONDS);

		context.close();
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("run - run method been called");
		stockTicker.publish();
	}

}
