package exp.app.random;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Create a random TXT file with information for testing
 * @author Filipe
 *
 */
public class RandomFileCreator {
	/*List of random values to be used as names in entry creation*/
	private static final String[] RANDOM_NAME={"SubjectA","SubjectB","SubjectC"};
	/*List of random values to be used as domain names in entry creation*/
	private static final String[] RANDOM_DOMAIN ={"gmail.com","hotmail.com"};
	/*Maximum number of destinations on an email ( set by Gmail External Standart's)*/
	private static final int MAX_DESTINATIONS = 10;
	/*Maximum file size (in Byte)*/
	private static final long FILE_SIZE = 20971520;
	/*File name*/
	private static final String FILE_NAME = "test.json";
	/*Fields Names*/
	private static final String FROM_KEY = "from";
	private static final String DESTINATION_KEY = "destination";
	private static final String CARBON_COPY_KEY = "carbonCopy";
	/**
	 * Creates random file with a predefined size so it can be used for KnowledgeMill testing
	 * @return File created for testing
	 * @throws IOException
	 */
	public static File createRandomFile() throws IOException{
		/*Initialize File*/
		File file = new File(FILE_NAME);
		file.createNewFile();
		/*Create simple writer*/
		BufferedWriter bf = new BufferedWriter(new FileWriter(file));
		
		/*Run random creator*/
		long size = 0;
		int nEntries = 0;
		/*We use the size prediction so we can add a last 
		 * entry and correctly end the entry array*/
		double sizePrediction = 0;
		
		bf.write("[");
		while(size+sizePrediction < FILE_SIZE){
			String randomString = createEntry()+",";
			nEntries++;
			size += randomString.length();
			bf.write(randomString);
			/*Save new size median*/
			sizePrediction = size / nEntries;
		}
		/*One last entry to fill the array*/
		bf.write(createEntry());
		bf.write("]");
		
		/*Close writer*/
		bf.flush();
		bf.close();
		return file;
	}
	/**
	 * Creates a new random JSON entry
	 * @return
	 */
	private static String createEntry(){
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("{");
		/*Create sender*/
		strBuffer.append("\""+FROM_KEY+"\":\""+createRandomField()+"\",");
		Random random = new Random(System.nanoTime());
		
		/*Create primary email recipients*/
		int nRecipient = random.nextInt(MAX_DESTINATIONS);
		strBuffer.append("\""+DESTINATION_KEY+"\":[");
		for(int i = 0 ; i < nRecipient-1 ; i ++){
			strBuffer.append("\""+createRandomField()+"\",");
		}
		strBuffer.append("\""+createRandomField()+"\"");
		strBuffer.append("],");
		
		/*Create carbon copy email*/
		int nCarbonCopy = random.nextInt(MAX_DESTINATIONS);
		strBuffer.append("\""+CARBON_COPY_KEY+"\":[");
		for(int i = 0 ; i < nCarbonCopy-1 ; i ++){
			strBuffer.append("\""+createRandomField()+"\",");
		}
		strBuffer.append("\""+createRandomField()+"\"");
		strBuffer.append("]");
		
		strBuffer.append("}");
		return strBuffer.toString();
	}
	
	/**
	 * Returns a random email using the predefined set of names and domains
	 * @return
	 */
	private static String createRandomField(){
		StringBuffer strBuffer = new StringBuffer();

		Random random;
		random = new Random(System.nanoTime());
		
		/*Get random user name*/
		strBuffer.append(RANDOM_NAME[random.nextInt(RANDOM_NAME.length)]);
		strBuffer.append("@");
		/*Give new seed*/
		random = new Random(System.nanoTime());
		/*Get new domain name*/
		strBuffer.append(RANDOM_DOMAIN[random.nextInt(RANDOM_DOMAIN.length)]);
		return strBuffer.toString();
	}
}
