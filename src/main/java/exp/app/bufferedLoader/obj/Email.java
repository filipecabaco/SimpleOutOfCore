package exp.app.bufferedLoader.obj;

import com.mongodb.BasicDBObject;

public class Email extends BasicDBObject {
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
		email = email.replaceAll("\\.", "\\uff0");
		
		int value = this.get(email) == null ? 1 : ((Integer)this.get(email))+1;
		this.put(email,value);
	}

	/*CREATE GETTERS*/
	public String getId() {return id;}
}
