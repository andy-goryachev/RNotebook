// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.io;
import goryachev.common.ui.ImageTools;
import goryachev.notebook.js.JsEngine;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.img.JImage;
import goryachev.notebook.js.table.JsTable;
import goryachev.notebook.util.InlineHelp;
import java.awt.image.BufferedImage;
import java.io.File;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;


/** "IO" object in the global context */
public class IO
{
	public IO()
	{
	}
	
	
	public Object loadImage(Object x) throws Exception
	{
		File f = JsUtil.parseFile(x);
		BufferedImage im = ImageTools.read(f);
		JImage r = new JImage(im);
		
		// FIX does it need to be wrapped?
		//return new NativeJavaObject(JsEngine.get().getGlobalScope(), r, null);
		return r;
	}
	
	
	public JsTable loadTable(Object filename) throws Exception
	{
		File f = JsUtil.parseFile(filename);
		return new JsTable();
	}
	
	
	public JsTable loadTable(Object filename, Object type) throws Exception
	{
		File f = JsUtil.parseFile(filename);
		return new JsTable();
	}
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp();
		h.a("IO provides input/output functionality:");
		h.a("IO.loadImage(filename)", "loads image file");
		h.a("IO.loadTable(filename)", "loads table from CSV, XLS, or XLSX file");
		return h.toString();
	}
}
