package exp.app.bufferedLoader.worker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import exp.app.bufferedLoader.json.JsonParser;
import exp.app.bufferedLoader.obj.Email;
import exp.app.bufferedLoader.obj.EmailMap;
import exp.app.data.MongoCollection;
import exp.app.data.exceptions.ErrorSavingDocumentException;
import exp.app.data.exceptions.InvalidMongoObjectException;
import exp.app.random.RandomFileReader;

/**
 * Class responsible for doing the grunt worke, reading and preparing the information to be saved
 * @author Filipe
 *
 */
public class Worker implements Callable<Boolean>{
	
	private static final String FROM_KEY = "from"; //Indicates the field of FROM
	private static final String DESTINATION_KEY = "destination"; //Indicates the field of DESTINATION 
	private static final String CARBON_COPY_KEY = "carbonCopy"; // Indicates the field of CARBON COPY
	
	private RandomFileReader randomFileReader; //Iterator used to fetch information from file
	private JsonParser jsonParser; //The JSON Parser with already compiled regular expressions at work
	private MongoCollection mongoCollection; // The collection were the information will be saved
	private EmailMap emailMap; // Map were all information is saved and updated by all threads

	/**
	 * Initializes the worker
	 * @param randomFileReader Iterator used to fetch information from file
	 * @param jsonParser The JSON Parser with already compiled regular expressions at work
	 * @param mongoCollection The collection were the information will be saved
	 * @param emailMap Map were all information is saved and updated by all threads
	 */
	public Worker(RandomFileReader randomFileReader , 
			JsonParser jsonParser , 
			MongoCollection mongoCollection, 
			EmailMap emailMap){
		this.randomFileReader = randomFileReader;
		this.jsonParser = jsonParser;
		this.mongoCollection = mongoCollection;
		this.emailMap = emailMap;
	}

	public Boolean call() throws Exception{
		while(randomFileReader.hasNext()){
			String value = randomFileReader.next();
			//TODO FIND WHY NEXT STILL REsTURNS EMPTY VALUE
			if(!value.isEmpty()){
				Map<String, Object> map = jsonParser.toMap(value);
				synchronized (map) {

					Email email;
					/*Verify if the element exists and use it, otherwise parse and use it*/
					if(emailMap.containsKey(getInformationForKey(map))){
						email = emailMap.get(getInformationForKey(map));
					}else{
						email = createEmailObject(map);
					}
					if(email != null){
						handleDestination(email,map);
						handleCarbonCopy(email,map);
						try{
							//Save the element
							this.mongoCollection.saveDocument(email);
						}catch(InvalidMongoObjectException e){
							e.printStackTrace();
						}catch(ErrorSavingDocumentException e){
							e.printStackTrace();
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns FROM value from the JSON Map
	 * @param json
	 * @return
	 */
	private String getInformationForKey(Map<String,Object> json){
		if(!json.containsKey(FROM_KEY) || !(json.get(FROM_KEY) instanceof String)){
			return null;
		}
		return (String) json.get(FROM_KEY);
	}

	/**
	 * Creates a new {@link Email} object
	 * @param json
	 * @return
	 */
	private Email createEmailObject(Map<String,Object> json){
		if(!json.containsKey(FROM_KEY) || !(json.get(FROM_KEY) instanceof String)){
			return null;
		}
		Email email = new Email((String) json.get(FROM_KEY));
		return email;
	}

	/**
	 * Handles the 'Destination' field elements
	 * @param email
	 * @param json
	 */
	private void handleDestination(Email email , Map<String,Object> json){
		if(json.get(DESTINATION_KEY) != null && json.get(DESTINATION_KEY) instanceof List){
			List<?> destinations = (List<?>) json.get(DESTINATION_KEY);
			for(Object destination : destinations){
				if(destination instanceof String){
					//Increment the association found
					email.incrementAssociation((String) destination);
				}
			}
		}
	}
	
	/**
	 * Handles the 'Carbon Copy' field elements
	 * @param email
	 * @param json
	 */
	private void handleCarbonCopy(Email email , Map<String,Object> json){
		if(json.get(CARBON_COPY_KEY) != null && json.get(CARBON_COPY_KEY) instanceof List){
			List<?> destinations = (List<?>) json.get(CARBON_COPY_KEY);
			for(Object carbonCopy : destinations){
				if(carbonCopy instanceof String){
					//Increment the association found
					email.incrementAssociation((String) carbonCopy);
				}
			}
		}
	}
}
