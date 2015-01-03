// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.CException;
import goryachev.common.util.CKit;
import goryachev.notebook.js.img.JsImage;
import java.awt.image.BufferedImage;
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


	public static void todo()
	{
		throw new CException("To be implemented...");
	}


	/** 
	 * Creates snapshot of javascript object into an immutable data 
	 * suitable for rendering after the execution is completed.
	 * Tightly linked with CodePanel.createViewer()
	 */
	public static Object makeDatasnapshot(Object x)
	{
		if(x instanceof JsImage)
		{
			BufferedImage im = ((JsImage)x).getBufferedImage();
			return ImageTools.copyImageRGB(im);
		}
		else if(x instanceof Throwable)
		{
			return x;
		}
		else if(x != null)
		{
			return x.toString();
		}
		else
		{
			return null;
		}
	}
}
