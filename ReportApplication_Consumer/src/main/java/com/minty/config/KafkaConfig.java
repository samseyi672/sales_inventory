package com.minty.config;

import com.minty.dtorequest.OrderRequest;
import com.minty.entity.Order;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.util.backoff.FixedBackOff;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class KafkaConfig {
	@Autowired
	private KafkaProperties kafkaProperties;
	@Autowired
	private ObjectMapper objectMapper;
	@Bean
	public ConsumerFactory<Object, Object> consumerFactory() {
		var properties = kafkaProperties.buildConsumerProperties();
		properties.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "120000");
		return new DefaultKafkaConsumerFactory<>(properties);
	}
	@Bean(name = "farLocationContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<Object, Object> farLocationContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer) {
		var factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		configurer.configure(factory, consumerFactory());
		factory.setRecordFilterStrategy(new RecordFilterStrategy<Object, Object>() {
			@Override
			public boolean filter(ConsumerRecord<Object, Object> consumerRecord) {  // This is to help filter duplicate order
				try {
					//String record =consumerRecord.value().toString();
					OrderRequest order = objectMapper.readValue(consumerRecord.value().toString(),
							OrderRequest.class);
					return order.getStatus().equalsIgnoreCase("successful"); // this is a dummy filter
				} catch (Exception e) {
					return false;
				}
				//return false ;
			}
		});
		return factory;
	}

//	@Bean(name = "kafkaListenerContainerFactory")
//	public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
//			ConcurrentKafkaListenerContainerFactoryConfigurer configurer) {
//		var factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
//		configurer.configure(factory, consumerFactory());
//
//		factory.setCommonErrorHandler(new GlobalErrorHandler());
//
//		return factory;
//	}

	@Bean(name = "orderRetryContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<Object, Object> imageRetryContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer) {
		var factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		configurer.configure(factory, consumerFactory());
		factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(10_000, 3)));
		return factory;
	}

	@Bean(name = "invoiceDltContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<Object, Object> invoiceDltContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer, KafkaTemplate<String, String> kafkaTemplate) {
		var factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		configurer.configure(factory, consumerFactory());
		var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
		factory.setCommonErrorHandler(new DefaultErrorHandler(recoverer, new FixedBackOff(1000, 5)));
		return factory;
	}
}
