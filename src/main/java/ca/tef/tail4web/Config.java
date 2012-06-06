package ca.tef.tail4web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;

@Configuration
public class Config {
	@Bean
	public Mongo mongo() throws Exception {
		Mongo b = new Mongo("localhost");
		return b;
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate b = new MongoTemplate(mongo(), "test");

		return b;
	}
}