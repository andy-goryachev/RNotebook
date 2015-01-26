// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.image.filters;
import goryachev.notebook.js.image.ImTools;
import java.awt.image.BufferedImage;


public abstract class ImFilter
{
	public abstract void filter(BufferedImage src, BufferedImage dst);
	
	
	//
	
	
	public BufferedImage filter(BufferedImage src)
	{
		BufferedImage dst = ImTools.copy(src);
		filter(src, dst);
		return dst;
	}
}
