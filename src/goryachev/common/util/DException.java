// Copyright (c) 2011-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** 
 * An Exception with an Object payload, 
 * useful for fast exception handling where "exception type" is determined by a fast comparison
 *    <pre>(e.getMessage() == CONSTANT)</pre>
 * and additional parameters can be obtained by
 *    <pre>DException.getPayload(e)</pre>
 */
public class DException
	extends Exception
{
	private Object payload;
	
	
	public DException(String message, Object payload, Throwable cause)
	{
		super(message, cause);
		this.payload = payload;
	}
	
	
	public DException(String message, Object payload)
	{
		this.payload = payload;
	}
	
	
	public Object getPayload()
	{
		return payload;
	}
		
	
	public static DException get(Throwable e)
	{
		if(e instanceof DException)
		{
			return (DException)e;
		}
		return null;
	}
	
	
	public static Object getPayload(Throwable e)
	{
		DException de = get(e);
		if(de != null)
		{
			return de.getPayload();
		}
		return null;
	}
	
		
	public static String getStringPayload(Throwable e)
	{
		Object x = getPayload(e);
		if(x instanceof String)
		{
			return (String)x;
		}
		return null;
	}
}
