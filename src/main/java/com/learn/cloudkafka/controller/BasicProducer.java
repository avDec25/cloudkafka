package com.learn.cloudkafka.controller;

import com.learn.cloudkafka.BusinessCard;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("producer")
public class BasicProducer {

	@Autowired
	KafkaTemplate<String, BusinessCard> kafkaTemplate;

	@PostMapping("produce")
	public ResponseEntity<?> produceToCloudKafka(@RequestParam String topic) {
		if (topic.isEmpty()) {
			topic = "test-topic";
		}

		BusinessCard businessCard = BusinessCard.newBuilder().setId(108).setName("Amar Vashishth").build();
		kafkaTemplate.send(topic, businessCard);
		log.info("Produced Message in " + topic);

		return ResponseEntity.ok("produced to cloud");
	}

	@KafkaListener(topics = "testtopic")
	public void consumerOfMessages(ConsumerRecord<String, BusinessCard> consumerRecord) {
		log.info("Consumed Message from " + consumerRecord.topic());
		System.out.println("===========================");
		System.out.println(consumerRecord.timestamp());
		System.out.println("---------------------------");
		System.out.println(consumerRecord.value());
		System.out.println("===========================");
	}
}
