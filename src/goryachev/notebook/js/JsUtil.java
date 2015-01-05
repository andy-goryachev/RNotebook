// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.util.CException;
import goryachev.common.util.CKit;
import goryachev.common.util.CancelledException;
import java.io.File;
import org.mozilla.javascript.RhinoException;


public class JsUtil
{
	public static File parseFile(Object x)
	{
		return new File(x.toString());
	}


	public static String decodeException(Throwable err)
	{
		if(err instanceof CancelledException)
		{
			return "Interrupted";
		}
		else if(err instanceof RhinoException)
		{
			Throwable e = err.getCause();
			if(e != null)
			{
				String msg = e.getMessage();
				if(CKit.isNotBlank(msg))
				{
					return msg;
				}
				else
				{
					err = e;
				}
			}
			else
			{
				return ((RhinoException)err).getMessage();
			}
		}
		
		return CKit.stackTrace(err);
	}


	public static RuntimeException todo()
	{
		return new CException("To be implemented...");
	}
}
