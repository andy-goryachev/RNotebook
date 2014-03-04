// Copyright (c) 2011-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.ui.Theme;
import java.util.Date;


/** 
 * AttrDescriptor can be used as a map key which knows how to interpret
 * the corresponding binary value stored in that hashtable.
 */
public abstract class AttrDescriptor
{	
	public abstract String getStringValue(Object x);
	
	public abstract Object parse(Object x);
	
	//
	
	private String name;
	
	
	public AttrDescriptor(String name)
	{
		this.name = name;
	}
	
	
	public final String getName()
	{
		return name;
	}
	
	
	//
	
	
	public static class UNDEFINED extends AttrDescriptor
	{
		public UNDEFINED(String name)
		{
			super(name);
		}


		public String getStringValue(Object x)
		{
			return x.toString();
		}


		public Object parse(Object x)
		{
			return x;
		}
	}
	
	
	//
	
	
	public static class STRING extends AttrDescriptor
	{
		public STRING(String name)
		{
			super(name);
		}
		
				
		public String getStringValue(Object x)
		{
			return x.toString();
		}
		
		
		public Object parse(Object x)
		{
			return String.valueOf(x);
		}
	}
	
	
	//
	
	
	public static class NUMBER extends AttrDescriptor
	{
		public NUMBER(String name)
		{
			super(name);
		}
		
		
		public String getStringValue(Object x)
		{
			if(x instanceof Number)
			{
				Number n = (Number)x;
				return Theme.formatNumber(n);
			}
			else
			{
				return null;
			}
		}
		
		
		public Object parse(Object x)
		{
			if(x instanceof Number)
			{
				return x;
			}
			return null;
		}
	}
	
	
	//
	
	
	public static class DATE extends AttrDescriptor
	{
		public DATE(String name)
		{
			super(name);
		}
		
		
		public String getStringValue(Object x)
		{
			if(x instanceof Date)
			{
				Date n = (Date)x;
				return Theme.formatDateTime(n);
			}
			else if(x instanceof Long)
			{
				Long n = (Long)x;
				return Theme.formatDateTime(n);
			}
			else
			{
				return null;
			}
		}
		
		
		public Object parse(Object x)
		{
			if(x instanceof Long)
			{
				return x;
			}
			return null;
		}
	}
}
