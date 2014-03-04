// Copyright (c) 2011-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.HashMap;


/**
 * Static lookup facility.
 */
public class Lookup
{
	private static HashMap<Object,HashMap<String,Object>> map = new HashMap();
	
	
	public synchronized static Object find(Object type, String name)
	{
		HashMap<String,Object> m = map.get(type);
		if(m != null)
		{
			return m.get(name);
		}
		return null;
	}
	
	
	public synchronized static Object register(Object type, String name, Object x)
	{
		HashMap<String,Object> m = map.get(type);
		if(m == null)
		{
			m = new HashMap();
			map.put(type, m);
		}
		
		return m.put(name, x);
	}
}
