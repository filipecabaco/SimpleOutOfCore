package exp.app.data.exceptions;
/**
 * Exception thrown if constructor has null values. 
 * @author Filipe
 *
 */
public class NullValuesConstructorException extends Exception{
	private static final long serialVersionUID = 8378560219147406013L;

	//Message used if the constructor receives one of the values as null
	private static final String ERR_MSG_NULL_VALUES = "Invalid parameter on constructor: Cannot be null";

	public NullValuesConstructorException(){
		super(ERR_MSG_NULL_VALUES);
	}

	public NullValuesConstructorException(String msg){
		super(msg);
	}
}
