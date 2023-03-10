// Copyright © 2005-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options;
import goryachev.common.util.CSettings;
import java.util.Collection;


public class IntegerOption
	extends COption<Integer>
{
	private Integer defaultValue;


	public IntegerOption(String key, CSettings options, Collection<COption<?>> list)
	{
		super(key, options, list);
	}


	public IntegerOption(String key, int defaultValue)
	{
		super(key);
		this.defaultValue = defaultValue;
	}


	public Integer defaultValue()
	{
		return defaultValue;
	}


	public Integer parseProperty(String s)
	{
		try
		{
			return Integer.parseInt(s);
		}
		catch(Exception e)
		{ }

		return defaultValue;
	}


	public String toProperty(Integer val)
	{
		return String.valueOf(val);
	}
}
