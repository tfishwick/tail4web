package ca.tef.tail4web.services;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/app-config.xml")
public class PortfolioServiceTest {

	@Autowired
	MongoTemplate template;

	@Test
	public void testQueueMessage() {
		Map<String, Object> doc = new HashMap<String, Object>();
		doc.put("name", "timmy");
		template.save(doc);
	}
}
