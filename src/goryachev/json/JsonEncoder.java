// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.json;
import goryachev.common.io.CWriter;
import goryachev.common.util.Base64;
import goryachev.json.gson.JsonWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Writer;


/** 
 * GSON-based writer, similar to JsonWriter.  Contains additonal convenience methods.
 */
public class JsonEncoder
	implements Closeable
{
	protected final JsonWriter wr;
	
	
	public JsonEncoder(File f) throws Exception
	{
		this(new CWriter(f));
	}

	
	public JsonEncoder(Writer w)
	{
		wr = new JsonWriter(w);
		wr.setIndent("  ");
	}


	public void close() throws IOException
	{
		wr.close();
	}
	
	
	public void beginObject() throws Exception
	{
		wr.beginObject();
	}
	
	
	public void endObject() throws Exception
	{
		wr.endObject();
	}

	
	public void beginArray() throws Exception
	{
		wr.beginArray();
	}
	
	
	public void endArray() throws Exception
	{
		wr.endArray();
	}
	
	
	public void name(String name) throws Exception
	{
		wr.name(name);
	}
	
	
	public void write(String name, String value) throws Exception
	{
		wr.name(name);
		wr.value(value);
	}
	
	
	public void write(String name, boolean value) throws Exception
	{
		wr.name(name);
		wr.value(value);
	}
	
	
	public void write(String name, Number value) throws Exception
	{
		wr.name(name);
		wr.value(value);
	}
	
	
	public void writeByteArray(String name, byte[] data) throws Exception
	{
		wr.name(name);
		String value = Base64.encode(data);
		wr.value(value);
	}
	
	
	public void value(Object x) throws Exception
	{
		if(x == null)
		{
			wr.value((String)null);
		}
		else if(x instanceof String)
		{
			wr.value(String.valueOf(x));
		}
		else if(x instanceof Number)
		{
			wr.value((Number)x);
		}
		else
		{
			wr.value(String.valueOf(x));
		}
	}
}
