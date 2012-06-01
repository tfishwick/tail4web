package ca.tef.tail4web.services;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import ca.tef.money.domain.LogMessage;

@Service
public class LogService {
	private static final Logger log = Logger.getLogger(LogService.class);

	@Autowired
	AmqpTemplate rabbit;

	@Autowired
	MongoTemplate mongo;

	/**
	 * Queues a message for processing - returns as quickly as possible.
	 * 
	 * @param message
	 */
	public void saveAndQueue(LogMessage message, String collectionName) {
		// Save the document.
		mongo.save(message, collectionName);
		log.info("Saved document: " + message);

		// Queue a message to process this message.
		Pair<String, String> pair = Pair.of(message.getId().toString(), collectionName);
		rabbit.convertAndSend("new_documents", pair);
		log.info("Queued message to new_documents");
	}

	/**
	 * Processes a message from the queue.
	 * - Sends the message to a fanout exchange for distribution for anyone interested in real-time logs.
	 */
	public void processMessage(LogMessage message) {
		// Process?

		// Send to the logs.fanout exchange.
		//amqpTemplate.convertAndSend("logs.fanout", message);
	}
}
