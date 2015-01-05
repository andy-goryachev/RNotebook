// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;
import goryachev.common.ui.ImageScaler;
import goryachev.common.ui.ImageTools;
import goryachev.common.ui.UI;
import goryachev.common.util.Noobfuscate;
import goryachev.common.util.img.jhlabs.InvertFilter;
import goryachev.notebook.js.JsUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


@Noobfuscate
public class JImage
{
	private BufferedImage image;
	private transient Graphics2D graphics;
	
	
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
		if(graphics != null)
		{
			graphics.dispose();
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
	
	
	public JImage setColor(Object color)
	{
		Color c = JsUtil.parseColor(color);
		gr().setColor(c);
		return this;
	}
	
	
	public JImage setColor(double r, double g, double b)
	{
		Color c = JsUtil.parseColor(r, g, b, null);
		gr().setColor(c);
		return this;
	}
	
	
	public JImage setColor(double r, double g, double b, double a)
	{
		Color c = JsUtil.parseColor(r, g, b, a);
		gr().setColor(c);
		return this;
	}
	
	
	protected Graphics2D gr()
	{
		if(graphics == null)
		{
			graphics = image.createGraphics();
			UI.setAntiAliasingAndQuality(graphics);
		}
		return graphics;
	}
	
	
	public JImage setStroke()
	{
		// TODO
		return this;
	}
	
	
	public JImage moveTo(double x, double y)
	{
		// TODO
		return this;
	}
	
	
	public JImage lineTo(double x, double y)
	{
		// TODO
		return this;
	}
	
	
	public JImage setDirection(double degrees)
	{
		// TODO
		return this;
	}
	
	
	public JImage line(double length)
	{
		// TODO
		return this;
	}
	
	
	public JImage turn(double degrees)
	{
		// TODO
		return this;
	}
	
	
	public JImage mark()
	{
		// TODO
		return this;
	}
	
	
	public JImage lineToMark()
	{
		// TODO
		return this;
	}
	
	
	public JImage fillRect(double x, double y, double w, double h)
	{
		gr().fill(new Rectangle2D.Double(x, y, w, h));
		return this;
	}
}
