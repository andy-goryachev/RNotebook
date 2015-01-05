// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.io;
import goryachev.common.ui.ImageTools;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.classes.JImage;
import goryachev.notebook.js.table.JsTable;
import goryachev.notebook.util.InlineHelp;
import java.awt.image.BufferedImage;
import java.io.File;


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
		InlineHelp h = new InlineHelp("IO");
		h.a("provides input/output functionality:");
		h.a("loadImage(filename)", "loads image file");
		h.a("loadTable(filename)", "loads table from CSV, XLS, or XLSX file");
		return h.toString();
	}
}
