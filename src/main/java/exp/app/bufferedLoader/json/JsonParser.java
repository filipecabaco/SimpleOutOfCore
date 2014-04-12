package exp.app.bufferedLoader.json;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * JSON parser that transforms String into Map
 * @author Filipe
 *
 */
public class JsonParser {
	/*
	 * Used to parse the String and retrieve JSON Elements
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
	/*Retrieve on group 0 all the characters within between inverted quotes*/
	private static final String LIST_REGEX = "\"(.*?)\"";
	
	private Pattern jsonPattern;
	private Pattern listPattern;
	/**
	 * Initializes parser by compiling regular expressions
	 */
	public JsonParser(){
		jsonPattern = Pattern.compile(JSON_REGEX);
		listPattern = Pattern.compile(LIST_REGEX);
	}
	
	/**
	 * Transform string into a Map
	 * @param value
	 * @return
	 */
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
	
	/**
	 * Handles string and return a object depending on the group it matches
	 * @param matcher
	 * @return
	 */
	private Object handleString(Matcher matcher){
		if(matcher.group(6) != null){
			//JSON Object
			return this.toMap(matcher.group(6));
		}else if(matcher.group(5) != null){
			//Numeric
			return Double.parseDouble(matcher.group(5));
		}else if(matcher.group(4) != null){
			//Boolean
			return Boolean.parseBoolean(matcher.group(4));
		}else if(matcher.group(3) != null){
			//List
			return handleList(matcher.group(3));
		}else if(matcher.group(2) != null){
			//String
			return matcher.group(2);
		}
		return null;
	}
	
	/**
	 * Handles list of elements
	 * @param value
	 * @return
	 */
	private List<Object> handleList(String value){
		Matcher matcher = listPattern.matcher(value);
		List<Object> list = new LinkedList<Object>();
		Matcher jsonMatcher;
		while(matcher.find()){
			//Verify if it's a list of JSON Element
			jsonMatcher = jsonPattern.matcher(matcher.group(0));
			if(jsonMatcher.find()){
				//If it's JSON Element, Map it...
				list.add(toMap(matcher.group(0)));
			}else{
				//... otherwise add the string
				list.add(matcher.group(0).replaceAll("\"", ""));
			}
		}
		return list;
	}
}
