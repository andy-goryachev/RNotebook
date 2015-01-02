// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.common.util.CKit;
import goryachev.common.util.FileTools;
import goryachev.json.JsonEncoder;
import goryachev.notebook.DataBook;
import goryachev.notebook.Schema;
import goryachev.notebook.SectionType;
import java.io.File;


/** DataBook writer */
public class DataBookJsonWriter
{
	private DataBookJsonWriter()
	{
	}


	private static String toSectionCode(SectionType t) throws Exception
	{
		switch(t)
		{
		case CODE:
			return Schema.SECTION_TYPE_CODE;
		case H1:
			return Schema.SECTION_TYPE_H1;
		case TEXT:
			return Schema.SECTION_TYPE_TEXT;
		default:
			throw new Exception("implement: " + t);
		}
	}


	private static void writeSection(DataBook b, int ix, JsonEncoder wr) throws Exception
	{
		wr.beginObject();
		{
			wr.write(Schema.KEY_SECTION_TYPE, toSectionCode(b.getType(ix)));
			wr.write(Schema.KEY_TEXT, b.getText(ix));
			// TODO result
		}
		wr.endObject();
	}
	
	
	public static void saveJSON(DataBook b, File f) throws Exception
	{
		FileTools.createBackup(f);
		
		// write
		JsonEncoder wr = new JsonEncoder(f);
		try
		{
			wr.beginObject();
			{
				if(b != null)
				{
					// file attributes
					wr.write(Schema.KEY_TYPE, Schema.TYPE);
					wr.write(Schema.KEY_VERSION, Schema.VERSION);

					// sections
					wr.name(Schema.KEY_SECTIONS);
					wr.beginArray();
					{
						int sz = b.size();
						for(int i=0; i<sz; i++)
						{
							writeSection(b, i, wr);
						}
					}
					wr.endArray();
				}
			}
			wr.endObject();
		}
		finally
		{
			CKit.close(wr);
		}
	}
}
