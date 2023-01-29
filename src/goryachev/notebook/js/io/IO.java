// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.io;
import goryachev.common.io.CReader;
import goryachev.common.io.CSVReader;
import goryachev.common.util.CKit;
import goryachev.notebook.js.JsObjects;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.classes.DTable;
import goryachev.notebook.util.Arg;
import goryachev.notebook.util.Doc;
import goryachev.notebook.util.InlineHelp;
import goryachev.swing.ImageTools;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;


@Doc("provides input/output functionality")
public class IO
{
	public IO()
	{
	}
	
	
	@Doc("loads image file")
	@Arg("file")
	public Object loadImage(Object file) throws Exception
	{
		File f = JsUtil.parseFile(file);
		BufferedImage im = ImageTools.read(f);
		return JsUtil.wrap(im);
	}
	
	
	@Doc("loads table from CSV, XLS, or XLSX file")
	@Arg("file")
	public DTable loadTable(Object file) throws Exception
	{
		File f = JsUtil.parseFile(file);
		DTable t = new DTable();
		CReader rd = new CReader(f);
		try
		{
			boolean header = true;
			CSVReader csv = new CSVReader(rd);
			try
			{
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
				CKit.close(csv);
			}
		}
		finally
		{
			CKit.close(rd);
		}
		
		return t;
	}
	
	
	@Doc("creates a table reader")
	@Arg("")
	public DTableReader newTableReader()
	{
		return new DTableReader();
	}
	
	
	@Doc("writes text file using UTF-8 encoding")
	@Arg({"text", "file"})
	public void writeText(Object text, File file) throws Exception
	{
		CKit.write(file, text.toString());
	}
	
	
	@Doc("writes text to file using the specified encoding")
	@Arg({"text", "file", "encoding"})
	public void writeText(Object text, String encoding, File file) throws Exception
	{
		Charset cs = Charset.forName(encoding);
		CKit.write(file, text.toString(), cs);
	}
	
	
	@Doc("reads text from a UTF-8 file")
	@Arg("file")
	public String readText(Object file) throws Exception
	{
		File f = JsUtil.parseFile(file);
		return CKit.readString(f);
	}
	
	
	@Doc("reads text from a file with specified encoding")
	@Arg({"file", "encoding"})
	public String readText(Object file, String encoding) throws Exception
	{
		File f = JsUtil.parseFile(file);
		Charset cs = Charset.forName(encoding);
		return CKit.readString(f, cs);
	}
	

	@Doc("reads byte array from a file")
	@Arg("file")
	// TODO or elastic byte array?
	public byte[] readBytes(Object file) throws Exception
	{
		File f = JsUtil.parseFile(file);
		return CKit.readBytes(f);
	}
	
	
	@Doc("writes byte array to a file")
	@Arg({"bytes", "file"})
	public void writeBytes(Object bytes, Object file) throws Exception
	{
		byte[] b = JsUtil.parseByteArray(bytes);
		File f = JsUtil.parseFile(file);
		CKit.write(b, f);
	}
	
	
	public String toString()
	{
		return getHelp().toString();
	}
	
	
	@Doc("writes image to file")
	@Arg({"image", "file"})
	public void writeImage(Object image, String file) throws Exception
	{
		BufferedImage im = JsUtil.parseImage(image);
		ImageTools.write(im, new File(file));
	}
	
	
	public InlineHelp getHelp()
	{
		return InlineHelp.create(JsObjects.IO, getClass());
	}
}
