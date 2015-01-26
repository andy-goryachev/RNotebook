// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.io;
import goryachev.common.io.CReader;
import goryachev.common.io.CSVReader;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.CKit;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.classes.DTable;
import goryachev.notebook.util.InlineHelp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;


public class IO
{
	public IO()
	{
	}
	
	
	public Object loadImage(Object x) throws Exception
	{
		File f = JsUtil.parseFile(x);
		BufferedImage im = ImageTools.read(f);
		return JsUtil.wrap(im);
	}
	
	
	public DTable loadTable(Object filename) throws Exception
	{
		File f = JsUtil.parseFile(filename);
		DTable t = new DTable();
		CReader rd = new CReader(f);
		try
		{
			boolean header = true;
			CSVReader csv = new CSVReader(rd);
			String[] ss;
			while((ss = csv.readNext()) != null)
			{
				if(header)
				{
					t.setColumns(ss);
					header = false;
				}
				else
				{
					t.addRow((Object[])ss);
				}
			}
		}
		finally
		{
			CKit.close(rd);
		}
		
		return t;
	}
	
	
	public DTableReader newTableReader()
	{
		return new DTableReader();
	}
	
	
	public void writeText(Object text, File f) throws Exception
	{
		CKit.write(f, text.toString());
	}
	
	
	public void writeText(Object text, String encoding, File f) throws Exception
	{
		Charset cs = Charset.forName(encoding);
		CKit.write(f, text.toString(), cs);
	}
	
	
	public String readText(Object file) throws Exception
	{
		File f = JsUtil.parseFile(file);
		return CKit.readString(f);
	}
	
	
	public String readText(Object file, String encoding) throws Exception
	{
		File f = JsUtil.parseFile(file);
		Charset cs = Charset.forName(encoding);
		return CKit.readString(f, cs);
	}
	
	
	// TODO or elastic byte array?
	public byte[] readBytes(Object file) throws Exception
	{
		File f = JsUtil.parseFile(file);
		return CKit.readBytes(f);
	}
	
	
	public void writeBytes(Object x, Object file) throws Exception
	{
		byte[] b = JsUtil.parseByteArray(x);
		File f = JsUtil.parseFile(file);
		CKit.write(b, f);
	}
	
	
	public String toString()
	{
		return "IO";
	}
	
	
	public InlineHelp getHelp()
	{
		InlineHelp h = new InlineHelp("IO");
		h.a("IO provides input/output functionality:");
		h.a("loadImage(file)", "loads image file");
		h.a("loadTable(file)", "loads table from CSV, XLS, or XLSX file");
		h.a("newTableReader()", "creates a table reader");
		h.a("readBytes(file)", "reads byte array from a file");
		h.a("writeBytes(bytes,file)", "writes byte array to a file");
		h.a("readText(file)", "reads text from a UTF-8 file");
		h.a("readText(file, encoding)", "reads text from a file with specified encoding");
		h.a("writeText(text, file)", "writes text file using UTF-8");
		h.a("writeText(text, encoding, file)", "writes text to file using the specified encoding");
		return h;
	}
}
