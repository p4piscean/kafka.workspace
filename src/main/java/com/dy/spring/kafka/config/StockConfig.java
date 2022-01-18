package com.dy.spring.kafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StockConfig {

	@Value("${kafka.topics}")
	public String[] topics;
	
	
	@Value("${kafka.tickers}")
	public String[] tickers;
}
