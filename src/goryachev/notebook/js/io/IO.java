// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.io;
import goryachev.common.io.CReader;
import goryachev.common.io.CSVReader;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.CKit;
import goryachev.common.util.D;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.classes.DTable;
import goryachev.notebook.js.classes.JImage;
import goryachev.notebook.util.InlineHelp;
import java.awt.image.BufferedImage;
import java.io.File;


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
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp("IO");
		h.a("provides input/output functionality:");
		h.a("loadImage(file)", "loads image file");
		h.a("loadTable(file)", "loads table from CSV, XLS, or XLSX file");
		return h.toString();
	}
}
