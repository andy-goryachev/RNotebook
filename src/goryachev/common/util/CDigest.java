// Copyright (c) 2012-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.security.MessageDigest;


/** Wrapped MessageDigest with a few convenience methods */
public class CDigest
{
	public static class SHA512 extends CDigest
	{
		public SHA512() { super("SHA-512"); }
	}
	
	//
	
	public static class SHA256 extends CDigest
	{
		public SHA256() { super("SHA-256"); }
	}
	
	//
	
	public static class SHA1 extends CDigest
	{
		public SHA1() { super("SHA-1"); }
	}
	
	//
	
	public static class MD5 extends CDigest
	{
		public MD5() { super("MD5"); }
	}
	
	//
	
	private static final byte NULL = 0;
	private static final byte STRING = 1;
	private static final byte BYTES = 2;
	private static final byte INT = 3;
	private static final byte LONG = 4;
	private static final byte FLOAT = 5;
	private static final byte DOUBLE = 6;
	private MessageDigest md;
	
	
	public CDigest(String algorithm)
	{
		try
		{
			md = MessageDigest.getInstance(algorithm);
		}
		catch(Exception e)
		{
			throw new CException(e);
		}
	}
	
	
	public void update(Object x)
	{
		if(x == null)
		{
			md.update(NULL);
		}
		else if(x instanceof byte[])
		{
			md.update(BYTES);
			md.update((byte[])x);
		}
		else if(x instanceof String)
		{
			md.update(STRING);
			md.update(((String)x).getBytes(CKit.CHARSET_UTF8));
		}
		else if(x instanceof Integer)
		{
			md.update(INT);
			md((Integer)x);
		}
		else if(x instanceof Long)
		{
			md.update(LONG);
			md((Long)x);
		}
		else if(x instanceof Float)
		{
			md.update(FLOAT);
			md(Float.floatToIntBits((Float)x));
		}
		else if(x instanceof Double)
		{
			md.update(DOUBLE);
			md(Double.doubleToRawLongBits((Double)x));
		}
		else
		{
			throw new CException("unsupported type: " + x.getClass());
		}
	}
	
	
	public void updateBytes(byte[] b)
	{
		md.update(b);
	}
	
	
	protected void md(int x)
	{
		md.update((byte)(x >> 24));
		md.update((byte)(x >> 16));
		md.update((byte)(x >>  8));
		md.update((byte)(x      ));
	}
	
	
	protected void md(long x)
	{
		md.update((byte)(x >> 56));
		md.update((byte)(x >> 48));
		md.update((byte)(x >> 40));
		md.update((byte)(x >> 32));
		md.update((byte)(x >> 24));
		md.update((byte)(x >> 16));
		md.update((byte)(x >>  8));
		md.update((byte)(x      ));
	}
	
	
	public byte[] digest()
	{
		return md.digest();
	}
	
	
	public byte[] digest(Object x)
	{
		update(x);
		return digest();
	}
	
	
	public static byte[] sha512(Object x)
	{
		return new SHA512().digest(x);
	}
	
	
	public static byte[] sha256(Object x)
	{
		return new SHA256().digest(x);
	}
	
	
	public static byte[] md5(Object x)
	{
		return new MD5().digest(x);
	}
}
