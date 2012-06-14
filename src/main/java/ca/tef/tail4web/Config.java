package ca.tef.tail4web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;

@Configuration
public class Config {

	@Value("${mongo.host}")
	String mongoHost;

	@Value("${mongo.db}")
	String mongoDb;

	@Bean
	public Mongo mongo() throws Exception {
		Mongo b = new Mongo(mongoHost);
		return b;
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate b = new MongoTemplate(mongo(), mongoDb);
		return b;
	}
}