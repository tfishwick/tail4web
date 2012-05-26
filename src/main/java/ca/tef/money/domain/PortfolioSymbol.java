package ca.tef.money.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PortfolioSymbol {

	@Id
	private ObjectId id;

	String symbol;

	// @DBRef
	// Portfolio portfolio;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	// public Portfolio getPortfolio() {
	// return portfolio;
	// }

	// public void setPortfolio(Portfolio portfolio) {
	// this.portfolio = portfolio;
	// }

}
