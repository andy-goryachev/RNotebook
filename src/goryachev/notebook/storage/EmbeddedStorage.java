// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.storage;
import goryachev.common.util.CList;


public abstract class EmbeddedStorage
{
	public abstract String getValue(String key);
	
	public abstract void setValue(String key, String value);
	
	public abstract CList<String> getKeys();
	
	//
	
	public static EmbeddedStorage getStorage()
	{
		// TODO
		return null;
	}
}
