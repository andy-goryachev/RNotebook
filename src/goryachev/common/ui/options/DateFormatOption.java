// Copyright (c) 2010-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;


public class DateFormatOption
	extends COption<SimpleDateFormat>
{
	public DateFormatOption(String id, CSettings settings, Collection<COption<?>> list)
	{
		super(id, settings, list);
	}


	public DateFormatOption(String id)
	{
		super(id);
	}


	public SimpleDateFormat defaultValue()
	{
		DateFormat f = DateFormat.getDateInstance(DateFormat.SHORT);
		if(f instanceof SimpleDateFormat)
		{
			return (SimpleDateFormat)f;
		}
		else
		{
			return new SimpleDateFormat("yyyy/MM/DD");
		}
	}


	public SimpleDateFormat parseProperty(String s)
	{
		return new SimpleDateFormat(s);
	}


	public String toProperty(SimpleDateFormat f)
	{
		return f.toPattern();
	}
}
