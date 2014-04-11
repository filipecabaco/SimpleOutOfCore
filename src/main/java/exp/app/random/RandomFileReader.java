package exp.app.random;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
/**
 * Quick and not so clean iterator for Random Out-of-core test
 * <p><b>Warning: </b>This class has a lot of imprecision and may not work with other files 
 * @author Filipe
 *
 */
public class RandomFileReader implements Iterator<String>{

	private final int BUFFER_SIZE = 1;

	private RandomAccessFile raf;
	private FileChannel inputChannel;
	private ByteBuffer byteBuffer;
	private long unread;
	/**
	 * Initiate a new File Reader for the test file
	 * @param file Test file to be reed
	 * @throws IOException
	 */
	public RandomFileReader(File file) throws IOException{
		raf = new RandomAccessFile(file, "r");
		inputChannel = raf.getChannel();
		byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		unread = raf.length();
	}

	public synchronized  boolean hasNext() {
		return unread>0;
	}

	public synchronized String next() {
		if(!hasNext()){
			throw new NoSuchElementException();
		}
		return getBlock();
	}

	/**
	 * Reads the next block inside the file
	 * @return New Entry block
	 */
	private synchronized String getBlock(){
		StringBuffer strBuffer = new StringBuffer();
		char current = readChar();
		if(current=='{'){
			/*We use the stack to control when the main structure will end*/
			Stack<Character> stack = new Stack<Character>();
			stack.push(current);
			strBuffer.append(current);
			while(!stack.empty()){
				current = readChar();
				switch (current) {
				case '{':
					stack.push(current);
					strBuffer.append(current);
					break;
				case '}':
					stack.pop();
					strBuffer.append(current);
					break;
				default:
					strBuffer.append(current);
					break;
				}
			}
		}else if(current == '['){
			/*If we found the [ char, it means there is a block inside, so we retrieve it*/
			return getBlock();
		}
		return strBuffer.toString();
	}
	/**
	 * Reads a single char from the inputChannel provided 
	 * by {@link RandomAccessFile} using a {@link ByteBuffer}
	 * @return
	 */
	private synchronized Character readChar(){
		char elem;
		try {
			inputChannel.read(byteBuffer);
			unread -= byteBuffer.capacity();
			byteBuffer.rewind();
			elem = (char) byteBuffer.get();
			byteBuffer.flip();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return elem;
	}
	/**
	 * Cleans and closes the reader
	 * @throws IOException If the reader could not be closed
	 */
	public synchronized void close() throws IOException{
		byteBuffer.clear();
		inputChannel.close();
		raf.close();
	}
	/**
	 * This function is not supported
	 */
	public synchronized void remove() {
		throw new UnsupportedOperationException("No need to remove from read");
	}
}
