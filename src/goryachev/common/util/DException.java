// Copyright (c) 2011-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** 
 * An Exception with an Object problem identifier, 
 * useful for fast exception handling where "exception type" is determined by a fast comparison
 *    <pre>(e.getProblem() == CONSTANT)</pre>
 * and additional parameters can be obtained by
 *    <pre>DException.getPayload(e)</pre>
 *    
 * id, message, throwable
 */
public class DException
	extends Exception
{
	private Object problem;
	
	
	public DException(Object problem, String message, Throwable cause)
	{
		super(message, cause);
		setProblem(problem);
	}
	
	
	public DException(Object problem, Throwable cause)
	{
		super(cause);
		setProblem(problem);
	}
	
	
	public DException(Object problem, String message)
	{
		super(message);
		setProblem(problem);
	}
	
	
	public DException(Object problem)
	{
		super(problem.toString());
		setProblem(problem);
	}
	
	
	private void setProblem(Object x)
	{
		if(x == null)
		{
			throw new NullPointerException();
		}
		this.problem = x;
	}
	
	
	public Object getProblem()
	{
		return problem;
	}
	
	
	public String getProblemString()
	{
		return problem.toString();
	}
		
	
	public static DException get(Throwable e)
	{
		if(e instanceof DException)
		{
			return (DException)e;
		}
		return null;
	}
	
	
	public static Object getProblem(Throwable e)
	{
		DException de = get(e);
		if(de != null)
		{
			return de.getProblem();
		}
		return null;
	}
	
		
	public static String getProblemString(Throwable e)
	{
		Object x = getProblem(e);
		if(x instanceof String)
		{
			return (String)x;
		}
		return null;
	}
}
