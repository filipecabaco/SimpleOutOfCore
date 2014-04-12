package exp.app.data;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;

import exp.app.data.exceptions.ErrorSavingDocumentException;
import exp.app.data.exceptions.InvalidMongoObjectException;

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
	
	/**
	 * 
	 * @param content
	 * @return
	 */
	public boolean find(MongoObject content){
		DBCursor cursor = this.dbCollection.find(this.generateCriteria(content));
		return  cursor != null && cursor.size()>0;
	}
	
	/**
	 * 
	 * @param content
	 * @throws InvalidMongoObjectException
	 * @throws ErrorSavingDocumentException
	 */
	public void saveDocument(MongoObject content) throws InvalidMongoObjectException, ErrorSavingDocumentException {
		if(content == null){
			throw new InvalidMongoObjectException();
		}
		WriteResult writeResult = null;
		
		if(find(content)){
			//UPDATES EXISTING DOCUMENT
			BasicDBObject criteria = generateCriteria(content);
			BasicDBObject update = generateUpdate(content);
			if(criteria == null || update == null){
				throw new InvalidMongoObjectException();
			}
			writeResult = dbCollection.update(criteria,update);
		}else{
			//CREATE NEW DOCUMENT
			writeResult = dbCollection.save(content);
		}
		if(writeResult != null && writeResult.getError() != null){
			throw new ErrorSavingDocumentException(writeResult.getError());
		}
	}
	
	/**
	 * Generates the criteria used on mongo update function
	 * @param content
	 * @return
	 */
	private BasicDBObject generateCriteria(MongoObject content){
		if(content == null || content.getCriteriaFields() == null){
			return null;
		}
		BasicDBObject criteria = new BasicDBObject();
		for(String key : content.getCriteriaFields()){
			criteria.append(key, content.get(key));
		}
		return criteria;
	}
	
	/**
	 * Generates the update used on mongo update function
	 * @param content
	 * @return
	 */
	private BasicDBObject generateUpdate(MongoObject content){
		if(content == null || content.getUpdatableFields() == null){
			return null;
		}
		BasicDBObject update = new BasicDBObject();
		for(String key : content.getUpdatableFields().keySet()){
			//Using the map, we know what kind of modifier we need to use in each field
			update.append(content.getUpdatableFields().get(key).getModifier(),
					new BasicDBObject().append(key, content.get(key)));
		}
		return update;
	}

	
	//GETTERS AND SETTERS
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public DBCollection getDbCollection() {return dbCollection;}
	public void setDbCollection(DBCollection dbCollection) {this.dbCollection = dbCollection;}


}
