// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;
import goryachev.common.ui.ImageScaler;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.CKit;
import goryachev.common.util.Noobfuscate;
import goryachev.common.util.img.SepiaFilter;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.InlineHelp;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


@Noobfuscate
public class JImage
{
	private BufferedImage image;
	
	
	public JImage(BufferedImage im)
	{
		this.image = im;
	}
	
	
	public JImage(int width, int height)
	{
		this(width, height, false);
	}
	
	
	public JImage(int width, int height, Object arg)
	{
		if(arg instanceof Boolean)
		{
			boolean alpha = Boolean.TRUE.equals(arg);
			image = new BufferedImage(width, height, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		}
		else
		{
			Color c = JsUtil.parseColor(arg);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			try
			{
				g.setColor(c);
				g.fillRect(0, 0, width, height);
			}
			finally
			{
				g.dispose();
			}
		}
	}
	

	public BufferedImage getBufferedImage()
	{
		return ImageTools.copyImageRGB(image);
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
	
	
	public JImage copy()
	{
		BufferedImage im = ImageTools.copyImageRGB(image);
		return new JImage(im);
	}
	
	
	public JImage scale(double factor)
	{
		int w = CKit.round(getWidth() * factor);
		int h = CKit.round(getHeight() * factor);
		image = ImageScaler.resize(image, ImageTools.hasAlpha(image), w, h, true);
		return this;
	}
	
	
	public JImage reduce(int width, int height)
	{
		image = ImageTools.scaleImage(image, width, height);
		return this;
	}
	
	
	public JImage sepia()
	{
		image = new SepiaFilter().filter(image, null);
		return this;
	}
	
	
	public InlineHelp getHelp()
	{
		InlineHelp h = new InlineHelp("JImage");
		h.a("new JImage(width, height)");
		h.a("new JImage(width, height, alpha)");
		h.a("new JImage(width, height, color)");
		//
		h.a("bufferedImage", "returns a copy of underlying BufferedImage object");
		h.a("height", "returns image height");
		h.a("reduce(width, height)", "resizes the image to the specified size, only if larger");
		h.a("sepia()", "apply sepia filter");
		h.a("scale(factor)", "scales the image");
		h.a("width", "returns image width");
		return h;
	}
}
