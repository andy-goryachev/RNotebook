// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.Noobfuscate;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.image.ImageBuilder;
import goryachev.notebook.util.InlineHelp;
import java.awt.Color;
import java.awt.image.BufferedImage;


@Noobfuscate
public class JImage
{
	private final ImageBuilder builder;
	
	
	public JImage(BufferedImage im)
	{
		builder = new ImageBuilder(im);
	}
	
	
	public JImage(int width, int height)
	{
		builder = new ImageBuilder(width, height, false);
	}
	
	
	public JImage(int width, int height, Object arg)
	{
		if(arg instanceof Boolean)
		{
			boolean alpha = Boolean.TRUE.equals(arg);
			builder = new ImageBuilder(width, height, alpha);
		}
		else
		{
			Color c = JsUtil.parseColor(arg);
			builder = new ImageBuilder(width, height, false);
			builder.setColor(c);
			builder.fill(0, 0, width, height);
		}
	}
	

	public BufferedImage getBufferedImage()
	{
		return ImageTools.copyImageRGB(builder.getBufferedImage());
	}


	public int getWidth()
	{
		return builder.getWidth();
	}
	

	public int getHeight()
	{
		return builder.getHeight();
	}
	
	
//	protected boolean hasAlpha()
//	{
//		return image.getColorModel().hasAlpha();
//	}
	
	
	public String toString()
	{
		return "JImage(" + getWidth() + "x" + getHeight() + ")";
	}
	
	
	public JImage invert()
	{
		builder.invert();
		return this;
	}
	
	
	public JImage copy()
	{
		return new JImage(getBufferedImage());
	}
	
	
	public JImage scale(double factor)
	{
		builder.scale(factor);
		return this;
	}
	
	
	public JImage setColor(Object color)
	{
		Color c = JsUtil.parseColor(color);
		builder.setColor(c);
		return this;
	}
	
	
	public JImage setColor(double r, double g, double b)
	{
		Color c = JsUtil.parseColor(r, g, b, null);
		builder.setColor(c);
		return this;
	}
	
	
	public JImage setColor(double r, double g, double b, double a)
	{
		Color c = JsUtil.parseColor(r, g, b, a);
		builder.setColor(c);
		return this;
	}
	
	
	public JImage setStroke()
	{
		builder.setStroke();
		return this;
	}
	
	
	public JImage moveTo(double x, double y)
	{
		builder.moveTo(x, y);
		return this;
	}
	
	
	public JImage lineTo(double x, double y)
	{
		builder.lineTo(x, y);
		return this;
	}
	
	
	public JImage setDirection(double degrees)
	{
		builder.setDirection(degrees);
		return this;
	}
	
	
	public JImage line(double length)
	{
		builder.line(length);
		return this;
	}
	
	
	public JImage turn(double degrees)
	{
		builder.turn(degrees);
		return this;
	}
	
	
	public JImage mark()
	{
		builder.mark();
		return this;
	}
	
	
	public JImage lineToMark()
	{
		builder.lineToMark();
		return this;
	}
	
	
	public JImage fillRect(double x, double y, double w, double h)
	{
		builder.fill(x, y, w, h);
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
		h.a("invert()", "inverts RGB channels");
		h.a("scale(factor)", "scales the image");
		h.a("setColor(name)", "sets the painting color by name");
		h.a("setColor(red,green,blue)", "sets the painting color by RGB values");
		h.a("setColor(red,green,blue,alpha)", "sets the painting color by RGB with alpha");
		h.a("setColor(name)", "sets the current color");
		h.a("width", "returns image width");
		return h;
	}
}
