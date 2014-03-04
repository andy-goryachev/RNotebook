// Copyright (c) 2012-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.CSorter;
import goryachev.common.util.Dump;
import goryachev.common.util.Log;
import goryachev.common.util.Parsers;
import goryachev.common.util.SB;
import java.io.Serializable;


public class PRecord
	implements Serializable
{
	/** override this method to pack transient data into attribute(s) */
	protected void commit() throws Exception { }
	
	//
	
	private CMap<String,Object> data;
	private CMap<String,Object> original;


	public PRecord()
	{
	}
	
	
	protected PRecord(CMap data)
	{
		this.data = new CMap(data);
		this.original = new CMap(data);
	}
	
	
	public PRecord copy()
	{
		return new PRecord(data);
	}
	
	
	protected CMap getAttributeMap()
	{
		return data;
	}
	
	
	protected CMap data()
	{
		if(data == null)
		{
			data = new CMap();
			original = new CMap();
		}
		return data;
	}
	
	
	protected void setAttributeMap(CMap<String,Object> m) throws Exception
	{
		this.data = m;
		this.original = (CMap)PTools.deepClone(m);
	}
	
	
	/** reset changes */
	public void reset()
	{
		try
		{
			setAttributeMap(data);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}
	
	
	public void createNew()
	{
		data = new CMap(data);
		original = new CMap();
	}


	public Object getAttribute(String key)
	{
		return data().get(key);
	}
	
	
	public static Object getAttribute(PRecord r, String key)
	{
		if(r != null)
		{
			return r.getAttribute(key);
		}
		return null;
	}
	
	
	public boolean hasAttribute(String key)
	{
		return (data().get(key) != null);
	}
	
	
	public String getString(String key)
	{
		return Parsers.parseString(getAttribute(key));
	}


	public Object setAttribute(String key, Object x)
	{
		if(x == null)
		{
			return data().remove(key);
		}
		else
		{
			return data().put(key, x);
		}
	}
	
	
	public void setString(String key, Object x)
	{
		String s = Parsers.parseString(x);
		setAttribute(key, s);
	}


	public CList<String> getAttributeNames()
	{
		return new CList(data().keySet());
	}

	
	public boolean isModified()
	{
		return !PTools.deepEquals(data(), original);
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof PRecord)
		{
			return PTools.deepEquals(data(), ((PRecord)x).data());
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return PRecord.class.hashCode() ^ data().hashCode();
	}
	
	
	public String dump()
	{
		SB sb = new SB();
		CList<String> names = getAttributeNames();
		CSorter.sort(names);
		
		for(String name: names)
		{
			sb.append(name).append(": ");
			
			Object v = getAttribute(name);
			Dump.describe(v, sb, 0);
		}
		
		return sb.toString();
	}
}
