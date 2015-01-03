// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.io;
import goryachev.common.ui.ImageTools;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.img.JsImage;
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
	
	
	public JsImage loadImage(Object x) throws Exception
	{
		File f = JsUtil.parseFile(x);
		BufferedImage im = ImageTools.read(f);
		return new JsImage(im);
	}
	
	
	public JsTable loadTable(Object x) throws Exception
	{
		File f = JsUtil.parseFile(x);
		JsUtil.todo(); // FIX
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
