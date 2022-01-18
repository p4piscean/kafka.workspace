package com.dy.request.subscribe;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dy.request.config.WebClientConfiguration;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@Slf4j
public class StockQuoteSubscriber {
	// private static final Logger log = LoggerFactory.getLogger(StockQuoteSubscriber.class);

	private static final String YAHOO_URL = "https://query1.finance.yahoo.com/v7/finance/";
    public static final int DELAY_MILLIS = 500;
    public static final int MAX_RETRY_ATTEMPTS = 3;
    
    //@Autowired
    //private WebClientConfiguration webClientConfiguration;
    
    private final WebClient webClient = WebClient.create(YAHOO_URL);
    
    public String getQuoteBySymbolSync(final String symbol) {
        return webClient
                .get()
                .uri("quote?symbols={symbol}", symbol)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    
    public String getQuoteWithRetry(final String symbol) {
        return webClient
                .get()
                .uri("quote?symbols={symbol}", symbol)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block();
    }
    
    
    public Mono<String> getQuoteBySymbolAsync(final String symbol) {
        return webClient
                .get()
                .uri("quote?symbols={symbol}", symbol)
                .retrieve()
                .bodyToMono(String.class);
    }
    
    
    public String getQuoteWithErrorHandling(final String symbol) {
        return webClient
                .get()
                .uri("quote?symbols={symbol}", symbol)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(String.class)
                .block();
    }
    
    public List<String> processDataFromObjectArray() {
    	Mono<Object[]> response = webClient.get()
    	          .accept(MediaType.APPLICATION_JSON)
    	          .retrieve()
    	          .bodyToMono(Object[].class).log();
    	        Object[] objArray = response.block();
    	        
    	        List<String> result = objArray == null ? Collections.emptyList() :
    	        	 Arrays.asList(Arrays.copyOf(objArray, objArray.length, String[].class));
    	        
    	        return result;
    	        
    }
}