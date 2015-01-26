// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.image;
import goryachev.common.ui.ImageScaler;
import goryachev.common.ui.ImageTools;
import goryachev.common.ui.UI;
import goryachev.common.util.img.jhlabs.InvertFilter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


/** this class encapsulates all the tools for image manipulation */
public class ImageBuilder
{
	private BufferedImage image;
	private Color color;
	private transient Graphics2D graphics;

	
	public ImageBuilder(BufferedImage im)
	{
		image = im;
	}
	
	
	public ImageBuilder(int width, int height, boolean alpha)
	{
		image = new BufferedImage(width, height, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
	}
	
	
	public ImageBuilder(int width, int height)
	{
		this(width, height, false);
	}
	
	
	public void commit()
	{
		if(graphics != null)
		{
			graphics.dispose();
			graphics = null;
		}
	}
	
	
	protected Graphics2D gr()
	{
		if(graphics == null)
		{
			graphics = image.createGraphics();
			
			UI.setAntiAliasingAndQuality(graphics);
			
			// FIX ? or before each painting?
			if(color != null)
			{
				graphics.setColor(color);
			}
		}
		return graphics;
	}
	
	
	public BufferedImage getBufferedImage()
	{
		commit();
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
	
	
	public boolean hasAlpha()
	{
		return image.getColorModel().hasAlpha();
	}
	
	
	public void invert()
	{
		commit();
		image = new InvertFilter().filter(image, null);
	}
	
	
	public void scale(double factor)
	{
		commit();
		
		int w = (int)Math.round(getWidth() * factor);
		int h = (int)Math.round(getHeight() * factor);
		image = ImageScaler.resize(image, ImageTools.hasAlpha(image), w, h, true);
	}


	public void setColor(Color c)
    {
		color = c;
		
		if(graphics != null)
		{
			graphics.setColor(c);
		}
    }
	
	
	public void fill(double x, double y, double w, double h)
	{
		gr().fill(new Rectangle2D.Double(x, y, w, h));
	}
	
	
	public void setStroke()
	{
		// TODO
	}
	
	
	public void moveTo(double x, double y)
	{
		// TODO
	}
	
	
	public void lineTo(double x, double y)
	{
		// TODO
	}
	
	
	public void setDirection(double degrees)
	{
		// TODO
	}
	
	
	public void line(double length)
	{
		// TODO
	}
	
	
	public void turn(double degrees)
	{
		// TODO
	}
	
	
	public void mark()
	{
		// TODO
	}
	
	
	public void lineToMark()
	{
		// TODO
	}
}
