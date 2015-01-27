// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.InlineHelp;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import research.image.ImageBuilder;


/** ImageBuilder wrapper for the scripting framework */
public class JImageBuilder
{
	private final ImageBuilder builder;
	
	
	public JImageBuilder(int width, int height, boolean alpha)
	{
		builder = new ImageBuilder(width, height, alpha);
	}
	
	
	public JImageBuilder(int width, int height)
	{
		this(width, height, false); 
	}
	
	
	public JImageBuilder(Object x)
	{
		BufferedImage im = JsUtil.parseImage(x);
		builder = new ImageBuilder(im);
	}
	
	
	public JImage getImage()
	{
		return new JImage(builder.getBufferedImage());
	}
	
	
	public int getWidth()
	{
		return builder.getWidth();
	}
	

	public int getHeight()
	{
		return builder.getHeight();
	}
	
	
	public JImageBuilder setColor(Object color)
	{
		Color c = JsUtil.parseColor(color);
		builder.setColor(c);
		return this;
	}
	
	
	public JImageBuilder setColor(double r, double g, double b)
	{
		Color c = JsUtil.parseColor(r, g, b, null);
		builder.setColor(c);
		return this;
	}
	
	
	public JImageBuilder setColor(double r, double g, double b, double a)
	{
		Color c = JsUtil.parseColor(r, g, b, a);
		builder.setColor(c);
		return this;
	}
	
	
	public JImageBuilder fillRect(double x, double y, double w, double h)
	{
		builder.fillRect(x, y, w, h);
		return this;
	}
	
	
	public JImageBuilder blur(float radius)
	{
		builder.blur(radius);
		return this;
	}
	
	
	public JImageBuilder setStroke(Object ... a)
	{
		BasicStroke s = JsUtil.parseStroke(a);
		builder.setStroke(s);
		return this;
	}
	
	
	public JImageBuilder path(String name)
	{
		builder.path(name);
		return this;
	}
	
	
	public JImageBuilder layer(String name)
	{
		builder.layer(name);
		return this;
	}
	
	
	public InlineHelp getHelp()
	{
		InlineHelp h = new InlineHelp("JImageBuilder");
		h.a("new JImageBuilder(width, height)");
		h.a("new JImageBuilder(width, height, alpha)");
		//
		h.a("blur(radius)", "apply Gaussian blur to the current layer");
		h.a("image", "returns the resulting JImage");
		h.a("height", "returns image height");
		h.a("layer(name)", "identify current layer");
		h.a("path(name)", "identify current path");
		//h.a("scale(factor)", "scales the image");
		h.a("setColor(name)", "sets current color");
		h.a("setColor(red, green, blue)", "sets current color by RGB values");
		h.a("setColor(red, green, blue, alpha)", "sets current color by RGB with alpha");
		h.a("width", "returns image width");
		return h;
	}
}
