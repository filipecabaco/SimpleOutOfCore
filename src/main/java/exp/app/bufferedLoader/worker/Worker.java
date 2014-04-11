package exp.app.bufferedLoader.worker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import exp.app.bufferedLoader.json.JsonParser;
import exp.app.bufferedLoader.obj.Email;
import exp.app.bufferedLoader.obj.EmailMap;
import exp.app.data.MongoCollection;
import exp.app.random.RandomFileReader;

public class Worker implements Callable<Boolean>{
	private static final String FROM_KEY = "from";
	private static final String DESTINATION_KEY = "destination";
	private static final String CARBON_COPY_KEY = "carbonCopy";

	private RandomFileReader randomFileReader;
	private JsonParser jsonParser;
	private MongoCollection mongoCollection;
	private EmailMap emailMap;

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
			//TODO FIND WHY NEXT STILL RETURNS EMPTY VALUE
			if(!value.isEmpty()){
				Map<String, Object> map = jsonParser.toMap(value);
				synchronized (map) {

					Email email;
					if(emailMap.containsKey(getInformationForKey(map))){
						email = emailMap.get(getInformationForKey(map));
					}else{
						email = createEmailObject(map);
					}
					if(email != null){
						handleDestination(email,map);
						handleCarbonCopy(email,map);
						this.mongoCollection.saveDocument(email);
					}
				}
			}
		}
		return true;
	}

	private String getInformationForKey(Map<String,Object> json){
		if(!json.containsKey(FROM_KEY) || !(json.get(FROM_KEY) instanceof String)){
			return null;
		}
		return (String) json.get(FROM_KEY);
	}

	private Email createEmailObject(Map<String,Object> json){
		if(!json.containsKey(FROM_KEY) || !(json.get(FROM_KEY) instanceof String)){
			return null;
		}
		Email email = new Email((String) json.get(FROM_KEY));
		return email;
	}

	private void handleDestination(Email email , Map<String,Object> json){
		if(json.get(DESTINATION_KEY) != null && json.get(DESTINATION_KEY) instanceof List){
			List<?> destinations = (List<?>) json.get(DESTINATION_KEY);
			for(Object destination : destinations){
				if(destination instanceof String){
					email.incrementAssociation((String) destination);
				}
			}
		}
	}

	private void handleCarbonCopy(Email email , Map<String,Object> json){
		if(json.get(CARBON_COPY_KEY) != null && json.get(CARBON_COPY_KEY) instanceof List){
			List<?> destinations = (List<?>) json.get(CARBON_COPY_KEY);
			for(Object carbonCopy : destinations){
				if(carbonCopy instanceof String){
					email.incrementAssociation((String) carbonCopy);
				}
			}
		}
	}
}
