package exp.app.bufferedLoader.obj;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;

import exp.app.data.MongoObject;
import exp.app.data.MongoUpdateType;

/**
 * Representation of an email element and counts associations
 * @author Filipe
 *
 */

public class Email extends BasicDBObject implements MongoObject{
	private static final long serialVersionUID = -6129885976540701959L;

	private String id;
	private final static String FROM_KEY = "_id";

	public Email(String from){
		this.id = from;
		this.put(FROM_KEY, id);
	}
	
	public synchronized void  incrementAssociation(String email){
		/*Point cannot be used as an valid character in a MongoDB Field  
		 *so it's replaced with Unicode equivalent*/
		email = email.replaceAll("\\.", "\\\\uff0");
		
		int value = this.get(email) == null ? 1 : ((Integer)this.get(email))+1;
		this.put(email,value);
	}

	@Override
	public List<String> getCriteriaFields() {
		List<String> criteria = new LinkedList<String>();
		criteria.add(FROM_KEY);
		return criteria;
	}

	@Override
	public Map<String,MongoUpdateType> getUpdatableFields() {
		Map<String,MongoUpdateType> fields = new HashMap<String,MongoUpdateType>();
		for(String key : this.keySet()){
			if(!key.equals(FROM_KEY)){
				fields.put(key,MongoUpdateType.INC);
			}
		}
		return fields;
	}
	
	/*CREATE GETTERS*/
	public String getId() {return id;}
}
