// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.json;
import goryachev.common.util.Base64;
import goryachev.json.gson.JsonReader;
import goryachev.json.gson.JsonToken;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;


/**
 * GSON-based JSON reader, similar to JsonReader.  Contains additional convenience methods.
 */
public class JsonDecoder
	implements Closeable
{
	/**
	 * This method needs to be written to be able to decode the json, starting with the root object (or array).
	 * <p>
	 * To decode an object, use<pre>
	 *    beginObject();
	 *    while(inObject())
	 *    {
	 *        String name = nextName();
	 *        ...
	 *    }
	 *    endObject();</pre>
	 * <p>
	 * To decode an array, use<pre>
 	 *    beginArray();
	 *    while(inArray())
	 *    {
	 *        ...
	 *    }
	 *    endArray();</pre>
	 * 
	 */
	//public abstract void decode() throws Exception;
	
	
	//
	
	
	protected final JsonReader rd;
	
	
	public JsonDecoder(Reader r)
	{
		this.rd = new JsonReader(r);
	}
	
	
	public JsonDecoder(String s)
	{
		this(new StringReader(s));
	}
	
	
	/**
	 * Consumes the next token from the JSON stream and asserts that it is the beginning of a new array.
	 */
	public void beginArray() throws Exception
	{
		rd.beginArray();
	}
	
	
	/**
	 * Consumes the next token from the JSON stream and asserts that it is the
	 * end of the current array.
	 */
	public void endArray() throws Exception
	{
		rd.endArray();
	}
	
	
	/**
	 * Returns true if the next token is not JsonToken.END_ARRAY.
	 */
	public boolean inArray() throws Exception
	{
		return (rd.peek() != JsonToken.END_ARRAY);
	}
	
	
	/**
	 * Consumes the next token from the JSON stream and asserts that it is the
	 * beginning of a new object.
	 */
	public void beginObject() throws Exception
	{
		rd.beginObject();
	}


	/**
	 * Consumes the next token from the JSON stream and asserts that it is the
	 * end of the current object.
	 */
	public void endObject() throws Exception
	{
		rd.endObject();
	}
	
	
	/**
	 * Returns true if the current array or object has another element.
	 */
	public boolean hasNext() throws Exception
	{
		return rd.hasNext();
	}
	
	
	/**
	 * Returns the {@link com.google.gson.stream.JsonToken#BOOLEAN boolean} value of the next token,
	 * consuming it.
	 *
	 * @throws IllegalStateException if the next token is not a boolean or if
	 *     this reader is closed.
	 */
	public boolean nextBoolean() throws Exception
	{
		return rd.nextBoolean();
	}
	
	
	/**
	 * Returns the next token, a {@link com.google.gson.stream.JsonToken#NAME property name}, and
	 * consumes it.
	 *
	 * @throws java.io.IOException if the next token in the stream is not a property
	 *     name.
	 */
	public String nextName() throws Exception
	{
		return rd.nextName();
	}


	/**
	 * Consumes the next token from the JSON stream and asserts that it is a
	 * literal null.
	 *
	 * @throws IllegalStateException if the next token is not null or if this
	 *     reader is closed.
	 */
	public void nextNull() throws Exception
	{
		rd.nextNull();
	}


	/**
	 * Returns the {@link com.google.gson.stream.JsonToken#NUMBER double} value of the next token,
	 * consuming it. If the next token is a string, this method will attempt to
	 * parse it as a double using {@link Double#parseDouble(String)}.
	 *
	 * @throws IllegalStateException if the next token is not a literal value.
	 * @throws NumberFormatException if the next literal value cannot be parsed
	 *     as a double, or is non-finite.
	 */
	public double nextDouble() throws Exception
	{
		return rd.nextDouble();
	}


	/**
	 * Returns the {@link com.google.gson.stream.JsonToken#NUMBER long} value of the next token,
	 * consuming it. If the next token is a string, this method will attempt to
	 * parse it as a long. If the next token's numeric value cannot be exactly
	 * represented by a Java {@code long}, this method throws.
	 *
	 * @throws IllegalStateException if the next token is not a literal value.
	 * @throws NumberFormatException if the next literal value cannot be parsed
	 *     as a number, or exactly represented as a long.
	 */
	public long nextLong() throws Exception
	{
		return rd.nextLong();
	}
	
	
	/**
	 * Returns the {@link com.google.gson.stream.JsonToken#NUMBER long} value of the next token,
	 * consuming it. If the next token is a string, this method will attempt to
	 * parse it as a Long. If the next token's numeric value cannot be exactly
	 * represented by a Java {@code long}, this method throws.
	 * This method accepts null as value.
	 *
	 * @throws IllegalStateException if the next token is not a literal value.
	 * @throws NumberFormatException if the next literal value cannot be parsed
	 *     as a number, or exactly represented as a long.
	 */
	public Long nextLongObject() throws Exception
	{
		return rd.nextLongObject();
	}
	
	
	/**
	 * Returns the {@link com.google.gson.stream.JsonToken#NUMBER int} value of the next token,
	 * consuming it. If the next token is a string, this method will attempt to
	 * parse it as an int. If the next token's numeric value cannot be exactly
	 * represented by a Java {@code int}, this method throws.
	 *
	 * @throws IllegalStateException if the next token is not a literal value.
	 * @throws NumberFormatException if the next literal value cannot be parsed
	 *     as a number, or exactly represented as a int.
	 */
	public int nextInt() throws Exception
	{
		long n = rd.nextLong();
		int rv = (int)n;
		if(n == rv)
		{
			return rv;
		}
		
		throw new NumberFormatException("not an int: " + n);
	}
	
	
	/**
	 * Returns the {@link com.google.gson.stream.JsonToken#STRING string} value of the next token,
	 * consuming it. If the next token is a number, this method will return its
	 * string form.
	 *
	 * @throws Exception
	 */
	public String nextString() throws Exception
	{
		return rd.nextString();
	}
	
	
	/**
	 * Returns the base64-encoded byte array value of the next token,
	 * consuming it.
	 *
	 * @throws Exception
	 */
	public byte[] nextByteArray() throws Exception
	{
		String s = nextString();
		return Base64.decode(s);
	}
	
	
	/**
	 * Returns the type of the next token without consuming it.
	 */
	public JsonToken peek() throws Exception
	{
		return rd.peek();
	}
	
	
	/**
	 * Skips the next value recursively. If it is an object or array, all nested
	 * elements are skipped. This method is intended for use when the JSON token
	 * stream contains unrecognized or unhandled values.
	 */
	public void skipValue() throws Exception
	{
		skipValue();
	}
	
	
	/**
	 * Returns true if the next token is not JsonToken.END_OBJECT.
	 */
	public boolean inObject() throws Exception
	{
		return (rd.peek() != JsonToken.END_OBJECT);
	}
	
	
	public void close() throws IOException
	{
		rd.close();
	}
}