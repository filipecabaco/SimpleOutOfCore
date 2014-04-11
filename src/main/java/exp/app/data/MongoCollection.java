package exp.app.data;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Creates an Collection were the various documents will be saved
 * @author Filipe
 *
 */
public class MongoCollection {
	private String name;
	private DBCollection dbCollection;

	public MongoCollection(String name, DBCollection dbCollection){
		this.name = name;
		this.dbCollection = dbCollection;
	}
	public synchronized void saveDocument(DBObject content) {
		dbCollection.save(content);
	}
	
	//GETTERS AND SETTERS
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public DBCollection getDbCollection() {return dbCollection;}
	public void setDbCollection(DBCollection dbCollection) {this.dbCollection = dbCollection;}


}
