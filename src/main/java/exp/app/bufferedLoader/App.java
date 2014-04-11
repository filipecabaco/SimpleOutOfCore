package exp.app.bufferedLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import exp.app.bufferedLoader.json.JsonParser;
import exp.app.bufferedLoader.obj.EmailMap;
import exp.app.bufferedLoader.worker.Worker;
import exp.app.data.MongoCollection;
import exp.app.data.MongoConnector;
import exp.app.data.exceptions.NoSuchCollectionException;
import exp.app.data.exceptions.NullValuesConstructorException;
import exp.app.random.RandomFileCreator;
import exp.app.random.RandomFileReader;

public class App 
{
	private static final String ADDR = "localhost";	// Stores address for mongoDb connection 
	private static final int PORT =  27017;	// Stores ports for mongoDb connection
	private static final String DB_NAME = "test";
	private static final int N_THREADS = 10; // Set Fixed threadpool size
	private static final String COLLECTION_NAME = "associations";
	
	public static void main( String[] args ) throws InterruptedException, NullValuesConstructorException, NoSuchCollectionException
	{
		try {
			/*Create file with random values*/
			File rndFile = RandomFileCreator.createRandomFile();
			RandomFileReader randomFileReader = new RandomFileReader(rndFile);
			
			/*Create a new instance of Parser were regex are compiled*/
			JsonParser jsonParser = new JsonParser();
			
			/*Create connection and collection to be used*/
			MongoConnector mongoConnector = new MongoConnector(ADDR, PORT, DB_NAME);
			MongoCollection mongoCollection = mongoConnector.createCollection(COLLECTION_NAME);
			
			/*Create the Map that will be used to update information by all workers*/
			EmailMap emailMap = new EmailMap();
			
			/*Initialize Executor Service*/
			ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
			
			/*Initialize Workers*/
			List<Worker> threadList = new ArrayList<Worker>();
			for(int i = 0 ; i < N_THREADS ; i++){
				threadList.add(new Worker(randomFileReader,
						jsonParser,
						mongoCollection,
						emailMap));
			}
			/*Retrieve all Workers*/
			List<Future<Boolean>> threadStatus = executorService.invokeAll(threadList);
			
			/*Wait for every worker to finish it's work*/
			while(!verifyWorkerStatus(threadStatus));
			
			randomFileReader.close();
			executorService.shutdownNow();
			System.out.println("DONE READING");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifies the status of all workers in threadpool
	 * @param threadStatus
	 * @return
	 */
	private static boolean verifyWorkerStatus(List<Future<Boolean>> threadStatus){
		boolean allDone = true;
		for(Future<Boolean> future : threadStatus){
			allDone = allDone && future.isDone();
		}
		return allDone;
	}
}
