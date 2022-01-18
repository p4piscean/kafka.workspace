package com.dy.request.subscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

//@SpringBootApplication
//@Slf4j
public class StockQuoteService implements CommandLineRunner {
	 //private static final Logger log = LoggerFactory.getLogger(StockQuoteService.class);
	 
	@Autowired
	private StockQuoteSubscriber stockQuoteSubscriber;

	public static void main(String[] args) {
		SpringApplication.run(StockQuoteService.class, args);
	}

	@Override
	public void run(final String... args) {
		//String response = stockQuoteSubscriber.getQuoteBySymbolSync("INTC");
		
		Mono<String> response = stockQuoteSubscriber.getQuoteBySymbolAsync("INTC");
		System.out.println("dipak result::" + response);

	}
	
	
    

}
