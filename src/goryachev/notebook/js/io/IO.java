// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.io;
import goryachev.common.ui.ImageTools;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.img.JsImage;
import goryachev.notebook.util.InlineHelp;
import java.awt.image.BufferedImage;
import java.io.File;


/** "io" object in the global context */
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
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp();
		h.a("io provides input/output functionality:");
		h.a("io.loadImage(filename)", "loads image");
		return h.toString();
	}
}
