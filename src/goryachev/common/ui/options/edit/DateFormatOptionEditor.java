// Copyright (c) 2013-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.options.DateFormatOption;
import goryachev.common.util.CLookup;
import goryachev.common.util.TXT;
import java.text.SimpleDateFormat;


public class DateFormatOptionEditor
	extends ChoiceOptionEditor<SimpleDateFormat>
{
	public final String FORMAT_DDMMYYYY_DASH = "dd-MM-yyyy";
	public final String FORMAT_DDMMYYYY_DOT = "dd.MM.yyyy";
	public final String FORMAT_DDMMYYYY_SLASH = "dd/MM/yyyy";
	public final String FORMAT_MMDDYYYY_SLASH = "MM/dd/yyyy";
	public final String FORMAT_YYYYMMDD_DASH = "yyyy-MM-dd";
	public final String FORMAT_YYYYMMDD_DOT = "yyyy.MM.dd";
	
	private CLookup lookup;
	
	
	public DateFormatOptionEditor(DateFormatOption op)
	{
		super(op);
		
		String d = TXT.get("DateFormatOptionEditor.abbreviated.day", "Day");
		String m = TXT.get("DateFormatOptionEditor.abbreviated.month", "Month");
		String y = TXT.get("DateFormatOptionEditor.abbreviated.year", "Year");
		
		Long t = System.currentTimeMillis();
		
		String dmyDash = d + "-" + m + "-" + y + " (" + new SimpleDateFormat(FORMAT_DDMMYYYY_DASH).format(t) + ")";
		String dmyDot = d + "." + m + "." + y + " (" + new SimpleDateFormat(FORMAT_DDMMYYYY_DOT).format(t) + ")";
		String dmySlash = d + "/" + m + "/" + y + " (" + new SimpleDateFormat(FORMAT_DDMMYYYY_SLASH).format(t) + ")";
		String mdySlash = m + "/" + d + "/" + y + " (" + new SimpleDateFormat(FORMAT_MMDDYYYY_SLASH).format(t) + ")";
		String ymdDash = y + "-" + m + "-" + d + " (" + new SimpleDateFormat(FORMAT_YYYYMMDD_DASH).format(t) + ")";
		String ymdDot = y + "." + m + "." + d + " (" + new SimpleDateFormat(FORMAT_YYYYMMDD_DOT).format(t) + ")";
		
		lookup = new CLookup
		(
			dmyDash,  FORMAT_DDMMYYYY_DASH,
			dmyDot,   FORMAT_DDMMYYYY_DOT,
			dmySlash, FORMAT_DDMMYYYY_SLASH,
			mdySlash, FORMAT_MMDDYYYY_SLASH,
			ymdDash,  FORMAT_YYYYMMDD_DASH,
			ymdDot,   FORMAT_YYYYMMDD_DOT
		);
		
		setChoices(new String[]
		{
			mdySlash,
			ymdDash,
			ymdDot,
			dmyDash,
			dmyDot,
			dmySlash
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
	
	
	public void setEditorValue(SimpleDateFormat f)
	{
		String v = (f == null ? null : f.toPattern());
		v = (String)lookup.lookup(v);
		combo.setSelectedItem(v);
	}
}
