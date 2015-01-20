// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.storage;
import goryachev.common.util.CKit;


public class StorageEntry
{
	public final String key;
	private String original;
	private String value;

	
	public StorageEntry(String key, String value)
    {
		this.key = key;
		this.original = value;
		this.value = value;
    }
	
	
	public String getKey()
	{
		return key;
	}
	
	
	public String getValue()
	{
		return value;
	}
	
	
	public void setValue(String s)
	{
		this.value = s;
	}
	
	
	public boolean isModified()
	{
		return CKit.notEquals(key, original);
	}
}