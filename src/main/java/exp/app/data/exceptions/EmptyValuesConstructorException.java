package exp.app.data.exceptions;
/**
 * Exception thrown if constructor has empty values. 
 * @author Filipe
 *
 */
public class EmptyValuesConstructorException extends Exception{
	private static final long serialVersionUID = 8378560219147406013L;

	//Message used if the values do not contain any address
		private static final String ERR_MSG_EMPTY_VALUES = "Invalid parameter on constructor: Have to be the same size";

	public EmptyValuesConstructorException(){
		super(ERR_MSG_EMPTY_VALUES);
	}

	public EmptyValuesConstructorException(String msg){
		super(msg);
	}
}
