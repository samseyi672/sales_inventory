package com.minty.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfig {
	@Bean(name = "cacheOrderRequest")
	public Cache<Integer, Boolean> cachePurchaseRequest() {
		return Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(2)).maximumSize(1000).build();
	}

//	@Bean(name = "cachePayRequest")
//	public Cache<PaymentRequestCacheKey, Boolean> cachePaymentRequest() {
//		return Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(2)).maximumSize(1000).build();
//	}


}
