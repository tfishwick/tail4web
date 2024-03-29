package ca.tef.tail4web;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.DBCollection;

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
		// Add a date field if it doesn't exist.
		if (!message.containsKey("date")) {
			message.put("date", new Date());
		}

		// Save the document.
		mongo.save(message, collectionName);
		log.info("Saved document to " + collectionName + ": " + message);

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

	public Set<String> findCollections() {
		Set<String> colls = mongo.getDb().getCollectionNames();
		Set<String> logCollections = new HashSet<String>();
		for (String collName : colls) {
			DBCollection collection = mongo.getCollection(collName);
			if (collection.isCapped()) {
				logCollections.add(collection.getName());
			}
		}
		System.out.println("returning collections: " + logCollections);
		return logCollections;
	}

	public DBCollection findCollection(String collectionName) {
		return mongo.getCollection(collectionName);
	}
}
