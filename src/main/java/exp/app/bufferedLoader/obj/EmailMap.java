package exp.app.bufferedLoader.obj;

import java.util.concurrent.ConcurrentHashMap;
/**
 * Used to save various {@link Email} elements so they won't be parsed again
 * @author Filipe
 *
 */
public class EmailMap extends ConcurrentHashMap<String, Email>{

	private static final long serialVersionUID = -5036422079094577176L;
	
}
