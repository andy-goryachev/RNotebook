// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.json.gson;


public class JsonException
	extends RuntimeException
{
	private final int line;
	private final int column;
	
	
	public JsonException(JsonReader rd, String msg)
	{
		super(msg);
		
		this.line = rd.getLineNumber() - 1;
		this.column = rd.getColumnNumber() - 1;
	}
	
	
	/** 0-based line number */
	public int getLineNumber()
	{
		return line;
	}
	
	
	/** 0-based column number */
	public int getColumnNumber()
	{
		return column;
	}
}
