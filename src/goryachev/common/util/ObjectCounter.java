// Copyright (c) 2013-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Collection;


/** counts the number of objects in a map, as determined by hashCode() and equals() */ 
public class ObjectCounter
{
	private CMap<Object,Integer> counts;
	
	
	public ObjectCounter()
	{
		this(32);
	}
	
	
	public ObjectCounter(int size)
	{
		counts = new CMap(size);
	}
	
	
	protected ObjectCounter(CMap<Object,Integer> m)
	{
		this.counts = new CMap(m);
	}
	
	
	public ObjectCounter copy()
	{
		return new ObjectCounter(counts);
	}
	
	
	public void add(Object x)
	{
		Integer ct = counts.get(x);
		if(ct == null)
		{
			counts.put(x, Integer.valueOf(1));
		}
		else
		{
			int n = Integer.valueOf(ct) + 1;
			if(n < Integer.MAX_VALUE)
			{
				counts.put(x, Integer.valueOf(n));
			}
		}
	}
	
	
	public void remove(Object x)
	{
		Integer ct = counts.get(x);
		if(ct == null)
		{
			// do nothing
		}
		else
		{
			int n = Integer.valueOf(ct) - 1;
			if(n >= 0)
			{
				counts.put(x, n);
			}
		}
	}
	
	
	public void addAll(Object[] xs)
	{
		if(xs != null)
		{
			for(Object x: xs)
			{
				add(x);
			}
		}
	}
	
	
	public void addAll(Collection<?> xs)
	{
		if(xs != null)
		{
			for(Object x: xs)
			{
				add(x);
			}
		}
	}
	
	
	public CList<Object> values()
	{
		return new CList(counts.values());
	}
	
	
	public int getCount(Object x)
	{
		return Parsers.parseInt(counts.get(x), 0);
	}
	
	
	public int size()
	{
		return counts.size();
	}
	
	
	public int getTotalCount()
	{
		int ct = 0;
		for(Integer x: counts.values())
		{
			ct += x;
		}
		return ct;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof ObjectCounter)
		{
			return counts.equals(((ObjectCounter)x).counts);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return CKit.hashCode(ObjectCounter.class, counts);
	}


	public Object getTop()
	{
		Object top = null;
		int count = 0;
		
		for(Object x: counts.keySet())
		{
			int ct = counts.get(x);
			if(ct > count)
			{
				count = ct;
				top = x;
			}
		}

		return top;
	}
}
