// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.util.CKit;
import java.io.File;
import javax.script.ScriptException;


public class JsUtil
{
	public static File parseFile(Object x)
	{
		return new File(x.toString());
	}


	public static String decodeException(Throwable err)
	{
		if(err instanceof ScriptException)
		{
			err = ((ScriptException)err).getCause();
			String s = err.getMessage();
			return s.replace("<Unknown source>#", "Line ");
		}
		else
		{
			return CKit.stackTrace(err);
		}
	}
}
