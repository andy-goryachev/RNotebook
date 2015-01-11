// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;

import goryachev.common.ui.ImageTools;
import goryachev.common.ui.Theme;
import goryachev.notebook.Styles;
import goryachev.notebook.js.JsError;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.classes.DTable;
import goryachev.notebook.js.classes.JImage;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Undefined;


public class Results
{
	/** 
	 * Creates snapshot of javascript object into an immutable data 
	 * suitable for rendering after the execution is completed.
	 * See also createViewer() below.
	 */
	public static Object copyValue(Object x)
	{
		if(x instanceof NativeJavaObject)
		{
			x = ((NativeJavaObject)x).unwrap();
		}
		
		if(x instanceof JImage)
		{
			return ((JImage)x).getBufferedImage();
		}
		else if(x instanceof DTable)
		{
			return ((DTable)x).copy();
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
	
	
	/**
	 * Creates viewer component for the result created by copyValue() above.
	 */
	public static JComponent createViewer(CodePanel p, Object x)
	{
		if(x instanceof BufferedImage)
		{
			JImageViewer v = new JImageViewer((BufferedImage)x);
			v.addMouseListener(p.handler);
			return v;
		}
		else if(x instanceof JsError)
		{
			String text = ((JsError)x).error;
			return createTextViewer(p, text, Styles.errorColor);
		}
		else if(x instanceof DTable)
		{
			return new DTableViewer((DTable)x, p.handler);
		}
		else if(x == null)
		{
			return createTextViewer(p, "null", Styles.numberColor);
		}
		else
		{
			String text = x.toString();
			return createTextViewer(p, text, Styles.resultColor);
		}
	}
	
	
	private static JComponent createTextViewer(CodePanel p, String text, Color c)
	{
		JTextArea t = new JTextArea();
		t.setFont(Theme.monospacedFont());
		t.setForeground(c);
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		t.setEditable(false);
		t.addMouseListener(p.handler);
		t.setText(text);
		return t;
	}
}
