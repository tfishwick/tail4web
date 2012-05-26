package ca.tef.money.domain;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Portfolio {

	@Id
	private ObjectId id;

	private String name;

	@DBRef
	List<PortfolioSymbol> symbols = new ArrayList<PortfolioSymbol>();

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PortfolioSymbol> getSymbols() {
		return symbols;
	}

	public void setSymbols(List<PortfolioSymbol> symbols) {
		this.symbols = symbols;
	}

}
