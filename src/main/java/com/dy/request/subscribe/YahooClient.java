package com.dy.request.subscribe;

import java.time.Duration;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;
import reactor.netty.transport.ProxyProvider;

public class YahooClient {
	
	 private static final String YAHOO_URL = "https://query1.finance.yahoo.com/v7/finance/";
	
	public  WebClient defaultWebClient() {
		HttpClient httpClient = HttpClient.create()
		          .responseTimeout(Duration.ofSeconds(1));

        return buildWebClient(httpClient);
    }

	 public WebClient proxyTimeoutClient() {
	        HttpClient httpClient = HttpClient.create()
	          .proxy(spec -> spec
	            .type(ProxyProvider.Proxy.HTTP)
	            .host(YAHOO_URL)
	            .port(8080)
	            .connectTimeoutMillis(3000));

	        return buildWebClient(httpClient);
	    }

	    private WebClient buildWebClient(HttpClient httpClient) {
	        return WebClient.builder()
	          .clientConnector(new ReactorClientHttpConnector(httpClient))
	          .build();
	    }
}
