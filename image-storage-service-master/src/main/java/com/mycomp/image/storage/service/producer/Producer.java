package com.mycomp.image.storage.service.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycomp.image.storage.service.constants.Constants;
import com.mycomp.image.storage.service.event.Event;
import com.mycomp.image.storage.service.exception.StorageServiceException;
import com.mycomp.image.storage.service.message.Message;
import com.mycomp.image.storage.service.properties.StorageServiceProperties;

/**
 * 
 * @author vat
 *
 */
public class Producer {
	private final StorageServiceProperties storageServiceProperties;

	public Producer(final StorageServiceProperties storageServiceProperties) {
		this.storageServiceProperties = storageServiceProperties;
	}

	/**
	 * 
	 * @param event
	 * @param message
	 */
	public void sendMessage(final Event event, final String message) {
		final Properties properties = new Properties();
		properties.put("bootstrap.servers", this.storageServiceProperties.getKafkaBroker());
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.setProperty("value.serializer", "org.apache.kafka.connect.json.JsonSerializer");
		final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
		try {

			final ObjectMapper objectMapper = new ObjectMapper();
			final String timeStamp = String.valueOf(System.currentTimeMillis());
			final Message album = new Message(timeStamp, event.name(), message);
			final JsonNode jsonNode = objectMapper.valueToTree(album);
			final ProducerRecord rec = new ProducerRecord("image-service-topic", jsonNode);
			kafkaProducer.send(rec);

		} catch (final Exception e) {
			throw new StorageServiceException("Unable to send message to kakfa server , check kafka server connectivity"
					+ this.storageServiceProperties.getKafkaBroker());
		} finally {
			kafkaProducer.close();
		}

	}

}
