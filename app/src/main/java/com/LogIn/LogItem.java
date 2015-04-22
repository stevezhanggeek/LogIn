package com.LogIn;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("LogItem")
public class LogItem extends ParseObject {
	
	public String getTime() {
		return getString("time");
	}
	
	public void setTime(String time) {
		put("time", time);
	}
	
	public String getValue() {
		return getString("value");
	}
	
	public void setValue(String value) {
		put("value", value);
	}

	public static ParseQuery<LogItem> getQuery() {
		return ParseQuery.getQuery(LogItem.class);
	}
}
