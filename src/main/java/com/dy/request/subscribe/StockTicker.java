package com.dy.request.subscribe;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dy.data.acquisition.MarketDataProducer;
import com.dy.spring.kafka.config.StockConfig;

import lombok.extern.slf4j.Slf4j;

@Service
@EnableScheduling
@Slf4j
public class StockTicker {

	@Autowired
	private StockQuoteSubscriber stockQuoteSubscriber;
	
	@Autowired
	private StockConfig stockConfig;
	
	@Autowired
	private MarketDataProducer marketDataProducer;
    
    @Scheduled(initialDelay = 1000, fixedRate = 100000)
    public void publish() {
        final LocalDateTime start = LocalDateTime.now();
        int randomIndex = ThreadLocalRandom.current().nextInt(stockConfig.tickers.length);
        String symbol = stockConfig.tickers[randomIndex];    
        
        log.info("Current time is :: {}, and ticker index is {}", start, randomIndex);
      
        String ticker = stockQuoteSubscriber.getQuoteBySymbolSync(symbol);    
        randomIndex = ThreadLocalRandom.current().nextInt(stockConfig.topics.length);
        String topic = stockConfig.topics[randomIndex];
      
        marketDataProducer.sendMessageWithCallback(ticker, topic);
        log.info("Stock ticker sent to {}", topic);
    }
    
    
}
