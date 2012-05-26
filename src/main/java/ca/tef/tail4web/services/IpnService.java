package ca.tef.tail4web.services;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class IpnService {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	IpnProcessor ipnProcessor;

	private static final Logger log = Logger.getLogger(IpnService.class);

	/**
	 * Processes an IPN request.
	 * - Save to Mongo.
	 * - Send an event to notify system of an IPN request.
	 */
	public void save(Ipn ipn) {
		mongoTemplate.save(ipn.params, "ipns");
		log.info("Saved IPN: " + ipn);

	}

	public void validate(Ipn ipn) {

	}

	/**
	 * A time consuming method to process an IPN. (To be called from a worker thread).
	 */
	public void process(Ipn ipn) {
		ipnProcessor.process(ipn);
	}
}
