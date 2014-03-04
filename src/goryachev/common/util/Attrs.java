// Copyright (c) 2011-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.HashMap;


public class Attrs
	implements Cloneable
{
	private HashMap<AttrDescriptor, Attr> attrs;
	
	
	public Attrs()
	{
		attrs = new HashMap();
	}
	
	
	public Attrs(Attrs x)
	{
		attrs = new HashMap(x.attrs);
	}
	
	
	public void add(Attr a)
	{
		attrs.put(a.getKey(), a);
	}
	
	
	public Attr remove(AttrDescriptor key)
	{
		return attrs.remove(key);
	}
	
	
	public Attr get(AttrDescriptor key)
	{
		return attrs.get(key);
	}
	
	
	public int size()
	{
		return attrs.size();
	}
	
	
	public Attr[] toArray()
	{
		return attrs.values().toArray(new Attr[attrs.size()]);
	}
}
