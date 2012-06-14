package ca.tef.tail4web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @see https://github.com/mongodb/mongo-java-driver/blob/master/examples/ReadOplog.java
 * @see http://www.mongodb.org/display/DOCS/Capped+Collections
 * 
 * 
 */
public class TailerThread extends Thread {

	private static final Logger log = Logger.getLogger(TailerThread.class);

	@Autowired
	MongoTemplate mongo;

	public DBCursor cur;

	public DocumentHandler documentHandler;

	public String collectionName;

	@Override
	public void run() {
		log.info("Starting TailerThread: " + this);
		Timer t = new Timer();
		DB db = mongo.getDb();
		log.info("Fetched db in " + t.et());

		log.info("Get collection: " + collectionName);
		DBCollection coll = db.getCollection(collectionName);

		t.reset();

		log.info("Querying.");

		BasicDBObject query = new BasicDBObject();
		query.put("$natural", -1);
		cur = coll.find().sort(query).limit(-5);
		Object lastValue = null;
		while (cur.hasNext()) {
			lastValue = cur.next().get("_id");

			log.info("Last value: " + lastValue);
		}
		cur.close();

		query = new BasicDBObject();
		query.put("_id", new BasicDBObject("$gte", lastValue));
		cur = coll.find(query);

		cur.addOption(Bytes.QUERYOPTION_TAILABLE);
		cur.addOption(Bytes.QUERYOPTION_AWAITDATA);

		log.info("Starting tail.");
		try {
			while (cur.hasNext()) {
				DBObject o = cur.next();
				documentHandler.onDocument(o);
			}
		} catch (Exception e) {
			log.error("Tail failed on collection: " + collectionName, e);
		}
	}

	@Override
	public void interrupt() {
		cur.close();
		super.interrupt();
	}

	interface DocumentHandler {
		public void onDocument(DBObject doc);
	}
}