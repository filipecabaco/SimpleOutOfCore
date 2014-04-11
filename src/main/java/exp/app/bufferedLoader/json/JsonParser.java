package exp.app.bufferedLoader.json;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {
	/*
	 * Group Description
	 * 0 - Full string
	 * 1 - Field name
	 * 2 - String
	 * 3 - List
	 * 4 - Boolean
	 * 5 - Numeric
	 * 6 - JSON Object
	 */
	private static final String JSON_REGEX = "(?:(?:\"(.*?)\"+)\\s*)\\s*:\\s*(?:(?:\"(.*?)\"+)|(\\[.*?\\])|(true|false)|(\\d+)|(?:\\{(.*?)\\})),{0,1}";
	private static final String LIST_REGEX = "\"(.*?)\"";
	
	private Pattern jsonPattern;
	private Pattern listPattern;
	
	public JsonParser(){
		jsonPattern = Pattern.compile(JSON_REGEX);
		listPattern = Pattern.compile(LIST_REGEX);
	}
	
	public Map<String,Object> toMap(String value){
		Matcher matcher = jsonPattern.matcher(value);
		Map<String , Object> map = new HashMap<String, Object>(); // Saves all values
		String field; //Saves field
		while(matcher.find()){
			field = matcher.group(1);
			map.put(field, handleString(matcher));
		}
		return map;
	}
	
	private Object handleString(Matcher matcher){
		if(matcher.group(6) != null){
			return this.toMap(matcher.group(6));
		}else if(matcher.group(5) != null){
			return Double.parseDouble(matcher.group(5));
		}else if(matcher.group(4) != null){
			return Boolean.parseBoolean(matcher.group(4));
		}else if(matcher.group(3) != null){
			return handleList(matcher.group(3));
		}else if(matcher.group(2) != null){
			return matcher.group(2);
		}
		return null;
	}
	
	private List<Object> handleList(String value){
		Matcher matcher = listPattern.matcher(value);
		List<Object> list = new LinkedList<Object>();
		Matcher jsonMatcher;
		while(matcher.find()){
			jsonMatcher = jsonPattern.matcher(matcher.group(0));
			if(jsonMatcher.find()){
				list.add(toMap(matcher.group(0)));
			}else{
				list.add(matcher.group(0).replaceAll("\"", ""));
			}
		}
		return list;
	}
}
