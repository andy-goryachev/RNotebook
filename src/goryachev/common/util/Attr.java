// Copyright (c) 2011-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class Attr
	implements Cloneable
{
	public final AttrDescriptor key;
	public final Object raw;
	private Object value;
	
	
	public Attr(AttrDescriptor key, Object raw)
	{
		this.key = key;
		this.raw = raw;
	}
	
	
	public AttrDescriptor getKey()
	{
		return key;
	}
	
	
	public Object getRaw()
	{
		return raw;
	}
	
	
	public Object getValue()
	{
		if(value == null)
		{
			value = key.parse(raw);
		}
		return value;
	}
	
	
	public String getName()
	{
		return key.getName();
	}
	
	
	public String getStringValue()
	{
		return key.getStringValue(getValue());
	}
	
	
	public Object clone() throws CloneNotSupportedException
	{
		return this;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof Attr)
		{
			Attr a = (Attr)x;
			return key.equals(a.key) && raw.equals(a.raw);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return Attr.class.hashCode() ^ key.hashCode() ^ raw.hashCode(); 
	}
}
