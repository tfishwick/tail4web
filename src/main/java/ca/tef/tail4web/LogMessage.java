package ca.tef.tail4web;

import java.util.Date;
import java.util.HashMap;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class LogMessage extends HashMap<String, String> {
	@Id
	private ObjectId id;

	private Date date;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String toString() {
		return "[Document: id=" + id + "]";
	}
}
