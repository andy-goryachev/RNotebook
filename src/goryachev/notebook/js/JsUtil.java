// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.CException;
import goryachev.common.util.CKit;
import goryachev.common.util.CancelledException;
import goryachev.notebook.js.img.JsImage;
import java.awt.image.BufferedImage;
import java.io.File;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Undefined;


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
				return e.getMessage();
			}
			else
			{
				return ((RhinoException)err).getMessage();
			}
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
		if(x instanceof NativeJavaObject)
		{
			x = ((NativeJavaObject)x).unwrap();
		}
		
		if(x instanceof JsImage)
		{
			BufferedImage im = ((JsImage)x).getBufferedImage();
			return ImageTools.copyImageRGB(im);
		}
		else if(x instanceof Throwable)
		{
			String msg = JsUtil.decodeException((Throwable)x);
			return new JsError(msg);
		}
		else if(x instanceof Undefined)
		{
			return "undefined";
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
