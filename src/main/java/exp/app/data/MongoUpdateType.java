package exp.app.data;
/**
 * Element used to specify the type of modifier used in MongoDB Updates
 * @author Filipe
 *
 */
public enum MongoUpdateType {
	SET("$set"),
	INC("$inc");
	
	private String modifier;
	private MongoUpdateType(String modifier){
		this.modifier = modifier;
	}
	public String getModifier() {
		return modifier;
	}
}
