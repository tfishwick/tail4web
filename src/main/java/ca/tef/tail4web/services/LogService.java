package ca.tef.tail4web.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tef.money.domain.LogMessage;

@Service
public class LogService {
	@Autowired
	AmqpTemplate amqpTemplate;

	/**
	 * Queues a message for processing - returns as quickly as possible.
	 * 
	 * @param message
	 */
	public void queueMessage(LogMessage message) {

	}

	/**
	 * Processes a message from the queue.
	 * - Sends the message to a fanout exchange for distribution for anyone interested in real-time logs.
	 */
	public void processMessage(LogMessage message) {
		// Process?

		// Send to the logs.fanout exchange.
		amqpTemplate.convertAndSend("logs.fanout", message);
	}
}
