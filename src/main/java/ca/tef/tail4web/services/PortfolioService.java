package ca.tef.tail4web.services;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import ca.tef.money.domain.Portfolio;
import ca.tef.money.domain.PortfolioSymbol;

@Service
public class PortfolioService {

	@Autowired
	MongoTemplate template;

	public Portfolio save(Portfolio portfolio) {

		// Save any new symbols
		for (PortfolioSymbol ps : portfolio.getSymbols()) {
			if (ps.getId() == null) {
				template.save(ps);
			}
		}
		template.save(portfolio);
		return portfolio;
	}

	public PortfolioSymbol save(PortfolioSymbol ps) {

		template.save(ps);
		return ps;
	}

	public List<Portfolio> findAll() {
		return template.findAll(Portfolio.class);
	}

	public Portfolio findById(int id) {
		return template.findOne(query(where("id").is(id)), Portfolio.class);
	}

	public Portfolio findByName(String name) {
		return template.findOne(query(where("name").is(name)), Portfolio.class);
	}
}
