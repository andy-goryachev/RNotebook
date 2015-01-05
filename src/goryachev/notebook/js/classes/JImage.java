// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;
import goryachev.common.ui.ImageScaler;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.Noobfuscate;
import goryachev.common.util.img.jhlabs.InvertFilter;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


@Noobfuscate
public class JImage
{
	private BufferedImage image;
	private transient Graphics2D g;
	
	
	public JImage(BufferedImage im)
	{
		this.image = im;
	}
	
	
	public JImage(int width, int height)
	{
		this(width, height, false);
	}
	
	
	public JImage(int width, int height, boolean alpha)
	{
		image = new BufferedImage(width, height, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
	}
	
	
	public String getClassName()
	{
		return "Image";
	}


	public BufferedImage getBufferedImage()
	{
		return image;
	}


	public int getWidth()
	{
		return image.getWidth();
	}
	

	public int getHeight()
	{
		return image.getHeight();
	}
	
	
	public String toString()
	{
		return "JImage(" + getWidth() + "x" + getHeight() + ")";
	}
	
	
	protected void commit()
	{
		if(g != null)
		{
			g.dispose();
		}
	}
	
	
	public JImage invert()
	{
		commit();
		image = new InvertFilter().filter(image, null);
		return this;
	}
	
	
	public JImage copy()
	{
		commit();
		
		BufferedImage im = ImageTools.copyImageRGB(image);
		return new JImage(im);
	}
	
	
	public JImage scale(double factor)
	{
		commit();
		
		int w = (int)Math.round(getWidth() * factor);
		int h = (int)Math.round(getHeight() * factor);
		image = ImageScaler.resize(image, ImageTools.hasAlpha(image), w, h, true);
		return this;
	}
}
