package exp.app.data.exceptions;

import exp.app.data.MongoObject;

/**
 * Exception thrown if {@link MongoObject} object is invalid 
 * @author Filipe
 *
 */
public class InvalidMongoObjectException extends Exception{


	private static final long serialVersionUID = -1067960821047890381L;
		//Message used if the values do not contain any address
	private static final String ERR_MSG_INVALID_MONGO_OBJECT = "Invalid MongoObject";

	public InvalidMongoObjectException(){
		super(ERR_MSG_INVALID_MONGO_OBJECT);
	}

	public InvalidMongoObjectException(String msg){
		super(msg);
	}
	
}
