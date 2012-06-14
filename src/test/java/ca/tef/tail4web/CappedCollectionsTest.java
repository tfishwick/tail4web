package ca.tef.tail4web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.tef.tail4web.ApplicationContextProvider;
import ca.tef.tail4web.TailerThread;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/app-config.xml")
public class CappedCollectionsTest {

	@Autowired
	MongoTemplate mongo;

	@Test
	public void testTail() throws Exception {

		// start a thread to tail a capped collection and interrupt it when asked
		TailerThread tailer = ApplicationContextProvider.getContext().getAutowireCapableBeanFactory()
				.createBean(TailerThread.class);

		tailer.start();

		System.out.println("sleeping .1 seconds");
		Thread.sleep(100);

		System.out.println("Interrupting");
		tailer.interrupt();

		Thread.sleep(100);

		System.out.println(tailer.isAlive());
	}
}
