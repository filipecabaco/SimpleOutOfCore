package exp.app.data.exceptions;

import exp.app.data.MongoObject;

/**
 * Exception thrown if there is an error when saving {@link MongoObject} 
 * @author Filipe
 *
 */
public class ErrorSavingDocumentException extends Exception {

	private static final long serialVersionUID = 774903734908780387L;

	//Message used if the values do not contain any address
	private static final String ERR_MSG_ERROR_SAVING = "An error occured saving the MongoObject";

	public ErrorSavingDocumentException(){
		super(ERR_MSG_ERROR_SAVING);
	}

	public ErrorSavingDocumentException(String msg){
		super(msg);
	}
}
