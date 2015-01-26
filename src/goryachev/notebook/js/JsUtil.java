// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.CException;
import goryachev.common.util.CKit;
import goryachev.common.util.CancelledException;
import goryachev.common.util.Hex;
import goryachev.common.util.Log;
import goryachev.common.util.Parsers;
import goryachev.common.util.SvgColorNames;
import goryachev.common.util.UserException;
import goryachev.notebook.js.classes.JImage;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.RhinoException;
import research.tools.filesync.RFileFilter;


public class JsUtil
{
	private static final String INTERRUPTED = "Interrupted";
	private static SvgColorNames colorNames;
	

	public static String decodeException(Throwable err)
	{
		if(err instanceof CancelledException)
		{
			return INTERRUPTED;
		}
		else if(err instanceof InterruptedException)
		{
			return INTERRUPTED;
		}
		else if(err instanceof RhinoException)
		{
			Throwable e = err.getCause();
			if(e == null)
			{
				return err.getClass().getSimpleName() + ": " + err.getMessage();
			}
			else if(e instanceof CancelledException)
			{
				return INTERRUPTED;
			}
			else if(e instanceof InterruptedException)
			{
				return INTERRUPTED;
			}
			else
			{
				String msg = e.getMessage();
				if(CKit.isNotBlank(msg))
				{
					return e.getClass().getSimpleName() + ": " + msg;
				}
				else
				{
					return e.getClass().getSimpleName();
				}
			}
		}
		else if(err instanceof UserException)
		{
			return err.getMessage();
		}
		
		return CKit.stackTrace(err);
	}

	
	public static File parseFile(Object x)
	{
		if(x instanceof File)
		{
			return (File)x;
		}
		
		String s = x.toString();
		if(s.startsWith("~"))
		{
			if(s.equals("~") || (s.equals("~/")) || s.equals("~\\"))
			{
				s = System.getProperty("user.home");
			}
		}
		return new File(s);
	}


	public static RuntimeException todo()
	{
		return new CException("To be implemented...");
	}


	public static Color parseColor(Object x)
	{
		if(x == null)
		{
			throw new CException("expecting color");
		}
		
		if(x instanceof Color)
		{
			return (Color)x;
		}
		
		String s = x.toString().trim();
		if(s.startsWith("#"))
		{
			try
			{
				if(s.length() == 4)
				{
					// #rgb
					int r = Hex.parseHexChar(s.charAt(1));
					int g = Hex.parseHexChar(s.charAt(2));
					int b = Hex.parseHexChar(s.charAt(3));
					
					return new Color(r + r<<4, g + g<<4, b + b<<4); 
				}
				else if(s.length() == 7)
				{
					// #rrggbb
					int r = Hex.parseHexChar(s.charAt(1)) << 4;
					r += Hex.parseHexChar(s.charAt(2));
					
					int g = Hex.parseHexChar(s.charAt(3)) << 4;
					g += Hex.parseHexChar(s.charAt(4));
					
					int b = Hex.parseHexChar(s.charAt(5)) << 4;
					b += Hex.parseHexChar(s.charAt(6));
					
					return new Color(r, g, b);
				}
				else if(s.length() == 9)
				{
					// #rrggbbaa
					int r = Hex.parseHexChar(s.charAt(1)) << 4;
					r += Hex.parseHexChar(s.charAt(2));
					
					int g = Hex.parseHexChar(s.charAt(3)) << 4;
					g += Hex.parseHexChar(s.charAt(4));
					
					int b = Hex.parseHexChar(s.charAt(5)) << 4;
					b += Hex.parseHexChar(s.charAt(6));
					
					int a = Hex.parseHexChar(s.charAt(7)) << 4;
					b += Hex.parseHexChar(s.charAt(8));
					
					return new Color(r, g, b, a);
				}
			}
			catch(Exception e)
			{ }
			
			throw new UserException("expecting color #RGB or #RRGGBB or #RRGGBBAA");
		}
		
		if(colorNames == null)
		{
			colorNames = new SvgColorNames();
		}
		
		Color c = colorNames.lookupColor(s);
		if(c == null)
		{
			// try with spaces removed
			String s2 = s.replace(" ", "");
			c = colorNames.lookupColor(s2);
		}
		
		if(c == null)
		{
			throw new UserException("invalid color: " + s);
		}
		
		return c;
	}
	
	
	private static boolean isZeroToOne(double x)
	{
		return ((x >= 0.0) && (x <= 1.0));
	}
	
	
	private static int toEightBit(double x)
	{
		if(x < 0)
		{
			return 0;
		}
		else if(x > 255)
		{
			return 255;
		}
		else
		{
			return (int)x;
		}
	}
	
	
	public static Color parseColor(double red, double green, double blue, Double alpha)
	{
		if(isZeroToOne(red) && isZeroToOne(green) && isZeroToOne(blue))
		{
			if(alpha == null)
			{
				return new Color((float)red, (float)green, (float)blue);
			}
			else if(isZeroToOne(alpha))
			{
				return new Color((float)red, (float)green, (float)blue, Parsers.parseFloat(alpha));
			}
		}
		
		float r = toEightBit(red);
		float g = toEightBit(green);
		float b = toEightBit(blue);
		
		if(alpha == null)
		{
			return new Color(r, g, b);
		}
		else
		{
			float a = toEightBit(alpha);
			return new Color(r, g, b, a);
		}
	}


	public static URL parseURL(Object url) throws Exception
	{
		return new URL(url.toString());
	}


	public static Charset parseCharset(String enc)
	{
		try
		{
			if(enc != null)
			{
				return Charset.forName(enc);
			}
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		return CKit.CHARSET_UTF8;
	}
	
	
	public static Object wrap(Object x)
	{
		if(x == null)
		{
			return null;
		}
		else if(x instanceof String)
		{
			return x;
		}
		else if(x instanceof BufferedImage)
		{
			return new JImage((BufferedImage)x);
		}
		else if(x instanceof Image)
		{
			BufferedImage im = ImageTools.toBufferedImage((Image)x);
			return new JImage(im);
		}
		else
		{
			return x;
		}
	}

	
	public static DateFormat parseDateFormat(Object f)
	{
		if(f == null)
		{
			return null;
		}
		
		if(f instanceof DateFormat)
		{
			return (DateFormat)f;
		}
		
		return new SimpleDateFormat(f.toString());
	}
	
	
	public static final String JSON_BIGINT = "I";
	public static final String JSON_BIGDECIMAL = "D";
	public static final String JSON_BOOL = "b";
	public static final String JSON_DATE = "a";
	public static final String JSON_DOUBLE = "d";
	public static final String JSON_INTEGER = "i";
	public static final String JSON_LONG = "l";
	public static final String JSON_NULL = "n";
	public static final String JSON_STRING = "s";
	
	
	/** encode object for writing to a JSON document */
	public static String encodeTableCell(Object x)
	{
		if(x == null)
		{
			return JSON_NULL;
		}
		
		if(x instanceof IdScriptableObject)
		{
			x = decodeIdScriptableObject((IdScriptableObject)x);
		}
		
		if(x instanceof Double)
		{
			return JSON_DOUBLE + x;
		}
		else if(x instanceof Long)
		{
			return JSON_LONG + x;
		}
		else if(x instanceof BigInteger)
		{
			return JSON_BIGINT + x;
		}
		else if(x instanceof BigDecimal)
		{
			return JSON_BIGDECIMAL + x;
		}
		else if(x instanceof Number)
		{
			return JSON_INTEGER + x;
		}
		else if(x instanceof Boolean)
		{
			return JSON_BOOL + x;
		}
		else if(x instanceof Date)
		{
			return JSON_DATE + ((Date)x).getTime();
		}
		else
		{
			return JSON_STRING + x;
		}
	}
	

	/** decode object read from a JSON document */
	public static Object decodeTableCell(String s) throws Exception
	{
		if(s == null)
		{
			return null;
		}
		
		if(s.startsWith(JSON_DOUBLE))
		{
			return Double.parseDouble(s.substring(1));
		}
		if(s.startsWith(JSON_LONG))
		{
			return Long.parseLong(s.substring(1));
		}
		if(s.startsWith(JSON_BIGINT))
		{
			return new BigInteger(s.substring(1));
		}
		if(s.startsWith(JSON_BIGDECIMAL))
		{
			return new BigDecimal(s.substring(1));
		}
		if(s.startsWith(JSON_INTEGER))
		{
			return Integer.parseInt(s.substring(1));
		}
		else if(s.equals(JSON_NULL))
		{
			return null;
		}
		if(s.startsWith(JSON_BOOL))
		{
			return Boolean.parseBoolean(s.substring(1));
		}
		if(s.startsWith(JSON_DATE))
		{
			return new Date(Long.parseLong(s.substring(1)));
		}
		else if(s.length() >= 1)
		{
			return s.substring(1);
		}
		else
		{
			// fallback
			return s;
		}
	}


	public static Object decodeIdScriptableObject(IdScriptableObject val)
	{
		String className = val.getClassName();
		if("Date".equals(className))
		{
			return Context.jsToJava(val, Date.class);
		}
		else //if("String".equals(className))
		{
			return Context.jsToJava(val, String.class);
		}
	}
	
	
	public static RFileFilter parseRFileFilter(Object x) throws Exception
	{
		if(x == null)
		{
			return null;
		}
		else if(x instanceof NativeArray)
		{
			Object[] a = ((NativeArray)x).toArray();
			return RFileFilter.parse(a);
		}
		else
		{
			return RFileFilter.parse(x.toString());
		}
	}


	public static String[] parseStringArray(Object[] arg)
	{
		int sz = arg.length;
		String[] ss = new String[sz];
		for(int i=0; i<sz; i++)
		{
			Object v = arg[i];
			String s = Parsers.parseString(v);
			ss[i] = s;
		}
		return ss;
	}


	public static byte[] parseByteArray(Object x)
	{
		if(x == null)
		{
			return null;
		}
		else if(x instanceof byte[])
		{
			return (byte[])x;
		}
		else if(x instanceof NativeArray)
		{
			NativeArray a = (NativeArray)x;
			int sz = a.size();
			byte[] b = new byte[sz];
			
			for(int i=0; i<sz; i++)
			{
				Object v = a.get(i);
				b[i] = parseByte(v);
			}
			
			return b;
		}
		else
		{
			throw new UserException("can't convert to byte array: " + x.getClass());
		}
	}
	
	
	public static byte parseByte(Object x)
	{
		if(x instanceof Number)
		{
			return ((Number)x).byteValue();
		}
		else
		{
			throw new UserException("can't convert to byte: " + x);
		}
	}


	public static BufferedImage parseImage(Object x)
	{
		if(x == null)
		{
			return null;
		}
		else if(x instanceof BufferedImage)
		{
			return (BufferedImage)x;
		}
		else if(x instanceof JImage)
		{
			return ((JImage)x).getBufferedImage();
		}
		else if(x instanceof Image)
		{
			return ImageTools.copyImageRGB((Image)x);	
		}
		
		throw new UserException("Not an image: " + x.getClass());
	}
}
