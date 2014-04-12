package exp.app.data;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import exp.app.data.exceptions.EmptyValuesConstructorException;
import exp.app.data.exceptions.NullValuesConstructorException;
import exp.app.data.exceptions.UnequalValuesConstructorException;
/**
 * MongoConnector to be used on MongoDB Interactions
 * @author Filipe
 *
 */
public class MongoConnector {

	private MongoClient mongo;	//Mongo Client to be used on Mongo transactions
	private DB db;
	private Map<String,MongoCollection> collections;
	
	
	/**
	 * Connects to given MongoDB Server address
	 * @param addr Mongo server addresses
	 * @param port Mongo port
	 * @param dbName Database name that will be used
	 * @throws UnknownHostException
	 * @throws NullValuesConstructorException
	 */
	public MongoConnector(String addr, int port , String dbName) throws UnknownHostException, NullValuesConstructorException{
		/*Verify if any of the values is Null*/
		if(addr == null){
			throw new NullValuesConstructorException();
		}

		/*Initialize Mongo Client*/
		this.mongo = new MongoClient(addr, port);
		this.db = mongo.getDB(dbName);
	}
	
	/**
	 * Connects to given MongoDB Server addresses
	 * @param addrs List of mongo server addresses
	 * @param ports List of mongo server ports
	 * @param dbName Database name that will be used
	 * @throws UnknownHostException Error initializing MongoClient
	 * @throws NullValuesConstructorException Error on Null values in the parameters
	 * @throws UnequalValuesConstructorException Error on UnequalSized values in the parameters
	 * @throws EmptyValuesConstructorException Error on Empty values in the parameters
	 */
	public MongoConnector(List<String> addrs, List<Integer> ports , String dbName) throws UnknownHostException, NullValuesConstructorException, UnequalValuesConstructorException, EmptyValuesConstructorException{
		/*Verify if any of the values is Null*/
		if(addrs == null || ports == null){
			throw new NullValuesConstructorException();
		}
		/*Verify if any of the values is Empty*/
		if(addrs.isEmpty() || ports.isEmpty()){
			throw new EmptyValuesConstructorException();
		}
		/*Verify if they both have the same size*/
		if(addrs.size() !=  ports.size()){
			throw new UnequalValuesConstructorException();
		}
		
		List<ServerAddress> serverAddrs = new LinkedList<ServerAddress>(); //LinkedList so we do not fill space unnecessarily
		int pos = 0;
		for(String addr : addrs){
			serverAddrs.add(new ServerAddress(addr, ports.get(pos)));
			pos++;
		}
		
		/*Initialize Mongo Client*/
		this.mongo = new MongoClient(serverAddrs);
		this.db = mongo.getDB(dbName);
	}
	
	/**
	 * Creates collection with given name
	 * @param collectionName
	 * @return
	 * @throws CollectionAlreadyExistsException 
	 */
	public MongoCollection createCollection(String collectionName){
		if(collections == null){
			collections = new HashMap<String, MongoCollection>();
		}
		if(collections.containsKey(collectionName) && collections.get(collectionName) != null){
			return collections.get(collectionName);
		}
		
		DBCollection newCollection= db.createCollection(collectionName, null);
		MongoCollection mongoCollection = new MongoCollection(collectionName, newCollection);
		collections.put(collectionName, mongoCollection);
		return mongoCollection;
	}
	
	/**
	 * Validates if collection exists
	 * @param collectionName
	 * @return
	 */
	public boolean hasCollection(String collectionName){
		return db.getCollection(collectionName) != null;
	}
	
}
