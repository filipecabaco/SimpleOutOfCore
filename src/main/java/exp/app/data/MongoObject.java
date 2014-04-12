package exp.app.data;

import java.util.List;
import java.util.Map;

import com.mongodb.DBObject;
/**
 * Interfaced used by elements that will be saved in MongoDB
 * @author Filipe
 *
 */
public interface MongoObject extends DBObject{
	
	/**
	 * Gets the search criteria to be used for this object
	 * @return
	 */
	public List<String> getCriteriaFields();
	
	/**
	 * Gets the fields that will be updated and the modifier to be used
	 * @return
	 */
	public  Map<String,MongoUpdateType> getUpdatableFields();
}
