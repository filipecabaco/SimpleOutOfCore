package exp.app.data.exceptions;
/**
 * Exception thrown if constructor has unequal sized values. 
 * @author Filipe
 *
 */
public class UnequalValuesConstructorException extends Exception{
	private static final long serialVersionUID = 8378560219147406013L;

	//Message used if the constructor receives values with non equal sizes
	private static final String ERR_MSG_UNEQUAL_SIZE_VALUES = "Invalid parameter on constructor: Have to be the same size";

	public UnequalValuesConstructorException(){
		super(ERR_MSG_UNEQUAL_SIZE_VALUES);
	}

	public UnequalValuesConstructorException(String msg){
		super(msg);
	}
}
