// Copyright (c) 2013-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.options.TimeFormatOption;
import goryachev.common.util.CLookup;
import java.text.SimpleDateFormat;


public class TimeFormatOptionEditor
	extends ChoiceOptionEditor<SimpleDateFormat>
{
	public final static String FORMAT_12H = "h:mm a";
	public final static String FORMAT_12H_SECONDS = "h:mm:ss a";
	public final static String FORMAT_24H = "HH:mm";
	public final static String FORMAT_24H_SECONDS = "HH:mm:ss";
	
	private CLookup lookup;
	
	
	public TimeFormatOptionEditor(TimeFormatOption op)
	{
		super(op);
		
		Long t = System.currentTimeMillis();
		
		String time12 = new SimpleDateFormat(FORMAT_12H).format(t);
		String time12s = new SimpleDateFormat(FORMAT_12H_SECONDS).format(t);
		String time24 = new SimpleDateFormat(FORMAT_24H).format(t);
		String time24s = new SimpleDateFormat(FORMAT_24H_SECONDS).format(t);
		
		lookup = new CLookup
		(
			time12,  FORMAT_12H,
			time12s, FORMAT_12H_SECONDS,
			time24,  FORMAT_24H,
			time24s, FORMAT_24H_SECONDS
		);
		
		setChoices(new String[]
		{
			time24,
			time24s,
			time12,
			time12s
		});
	}


	protected SimpleDateFormat parseEditorValue(String s)
	{
		String spec = (String)lookup.lookup(s);
		return spec == null ? null : new SimpleDateFormat(spec);
	}


	protected String toEditorValue(SimpleDateFormat f)
	{
		return f.toPattern();
	}
	
	
	public String getSearchString()
	{
		return null;
	}
}
