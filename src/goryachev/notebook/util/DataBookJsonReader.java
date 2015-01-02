// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.json.JsonDecoder;
import goryachev.notebook.DataBook;
import goryachev.notebook.Schema;
import goryachev.notebook.SectionType;
import java.io.Reader;


/** Reads DataBook JSON file */
public class DataBookJsonReader
	extends JsonDecoder
{
	public DataBookJsonReader(Reader rd)
	{
		super(rd);
	}
	
	
	public DataBook parse() throws Exception
	{
		DataBook b = new DataBook();
		String type = null;
		String ver = null;
		
		beginObject();
		while(inObject())
		{
			String s = nextName();
			if(Schema.KEY_SECTIONS.equals(s))
			{
				parseSections(b);
			}
			else if(Schema.KEY_TYPE.equals(s))
			{
				type = nextString();
			}
			else if(Schema.KEY_VERSION.equals(s))
			{
				ver = nextString();
			}
		}
		endObject();
		
		if(!Schema.TYPE.equals(type))
		{
			throw new Exception("Wrong format: expecting " + Schema.TYPE);
		}
		
		if(Schema.VERSION.equals(ver))
		{
			// ok
		}
		else
		{
			throw new Exception("Wrong version: " + ver);
		}
		
		return b;
	}


	protected void parseSections(DataBook b) throws Exception
	{
		beginArray();
		while(inArray())
		{
			parseSection(b);
		}
		endArray();
	}


	protected void parseSection(DataBook b) throws Exception
	{
		String type = null;
		String text = null;

		beginObject();
		while(inObject())
		{
			String s = nextName();
			if(Schema.KEY_SECTION_TYPE.equals(s))
			{
				type = nextString();
			}
			else if(Schema.KEY_TEXT.equals(s))
			{
				text = nextString();
			}
		}
		endObject();
		
		SectionType t = parseSectionType(type);
		b.addSection(t, text);
	}


	protected SectionType parseSectionType(String s) throws Exception
	{
		if(Schema.SECTION_TYPE_CODE.equals(s))
		{
			return SectionType.CODE;
		}
		else if(Schema.SECTION_TYPE_H1.equals(s))
		{
			return SectionType.H1;
		}
		else if(Schema.SECTION_TYPE_TEXT.equals(s))
		{
			return SectionType.TEXT;
		}
		else
		{
			throw new Exception("unknown section type: " + s);
		}
	}
}
