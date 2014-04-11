package exp.app.data.exceptions;
/**
 * Exception thrown if collection does not exist. 
 * @author Filipe
 *
 */
public class NoSuchCollectionException extends Exception{
	private static final long serialVersionUID = 8378560219147406013L;

	//Message used if the values do not contain any address
	private static final String ERR_MSG_NO_SUCH_COLLECTION = "Selected Collection does not exist in current database";

	public NoSuchCollectionException(){
		super(ERR_MSG_NO_SUCH_COLLECTION);
	}

	public NoSuchCollectionException(String msg){
		super(msg);
	}
}
