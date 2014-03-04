// Copyright (c) 2010-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;


public class TimeFormatOption
	extends COption<SimpleDateFormat>
{
	public TimeFormatOption(String id, CSettings settings, Collection<COption<?>> list)
	{
		super(id, settings, list);
	}


	public TimeFormatOption(String id)
	{
		super(id);
	}


	public SimpleDateFormat defaultValue()
	{
		DateFormat f = DateFormat.getTimeInstance(DateFormat.SHORT);
		if(f instanceof SimpleDateFormat)
		{
			return (SimpleDateFormat)f;
		}
		else
		{
			return new SimpleDateFormat("HH:mm");
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
