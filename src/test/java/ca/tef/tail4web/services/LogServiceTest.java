package ca.tef.tail4web.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.tef.money.domain.LogMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/app-config.xml")
public class LogServiceTest {

	@Autowired
	RabbitTemplate template;

	@Test
	public void testAmqp() {

		// AmqpAdmin admin = new RabbitAdmin(connectionFactory);

		// admin.declareQueue("myqueue");

		// AmqpTemplate template = new RabbitTemplate(connectionFactory);

		LogMessage logMessage = new LogMessage();
		MessageProperties mp = new MessageProperties();
		mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

		Message message = new Message("hello".getBytes(), mp);

		System.out.println("Sending message: vhost=" + template.getConnectionFactory().getVirtualHost());

		// template.convertAndSend("logs2", "hello");
		template.send("logs3", message);

		// String foo = template.receiveAndConvert("myqueue");
	}

	@Test
	public void testReceive() {
		// ConnectionFactory cf = template.getConnectionFactory();
		// Connection conn = cf.createConnection();

		Message message = template.receive("logs2");
		System.out.println(message);
		// template.

	}

	@Test
	public void testQueueMessage() {

	}

}
