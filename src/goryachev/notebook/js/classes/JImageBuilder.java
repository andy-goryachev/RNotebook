// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.InlineHelp;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
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
	
	
	public JImageBuilder(int width, int height, String backgroundColor)
	{
		Color bg = JsUtil.parseColor(backgroundColor);
		builder = new ImageBuilder(width, height, bg);
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
	
	
	public JImageBuilder draw(Object x)
	{
		if(x instanceof Shape)
		{
			builder.draw((Shape)x);
		}
		else
		{
			builder.draw(x.toString());
		}
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
	
	
	public JImageBuilder setStrokeWidth(float width)
	{
		builder.setStrokeWidth(width);
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
	
	
	public JImageBuilder line(double length)
	{
		builder.line(length);
		return this;
	}
	
	
	public JImageBuilder line(double dx, double dy)
	{
		builder.line(dx, dy);
		return this;
	}
	
	
	public JImageBuilder lineTo(double x, double y)
	{
		builder.lineTo(x, y);
		return this;
	}
	
	
	public JImageBuilder move(double dx, double dy)
	{
		builder.move(dx, dy);
		return this;
	}
	
	
	public JImageBuilder moveTo(double x, double y)
	{
		builder.moveTo(x, y);
		return this;
	}
	
	
	public JImageBuilder closePath()
	{
		builder.closePath();
		return this;
	}
	
	
	public void androidEffect()
	{
		builder.androidEffect();
	}
	
	
	public void translate(double dx, double dy)
	{
		builder.translate(dx, dy);
	}
	
	
	public void rotate(double x, double y, double angle)
	{
		builder.rotate(x, y, angle);
	}
	
	
	public InlineHelp getHelp()
	{
		InlineHelp h = new InlineHelp("");
		h.a("new JImageBuilder(width, height)");
		h.a("new JImageBuilder(width, height, alpha)");
		h.a("new JImageBuilder(width, height, backgroundColor)");
		//
		h.a("blur(radius)", "applies Gaussian blur to the current layer");
		h.a("closePath()", "close the current path");
		h.a("fillRect(x, y, width, height)", "fill the rectangle with the current color");
		h.a("height", "returns the image height");
		h.a("image", "returns the resulting JImage");
		h.a("layer(name)", "sets the current layer");
		h.a("line(length)", "adds a line segnent to the current path, from the current position along the current direction");
		h.a("line(dx, dy)", "adds a line segment to the current path, relative to the current position");
		h.a("lineTo(x, y)", "adds a line segment to the current path using absolute coordinates");
		h.a("move(dx, dy)", "move the current point using relative coordinates");
		h.a("moveTo(x, y)", "move the current point to new absolute coordinates");
		h.a("path(name)", "sets the current path");
		h.a("rotate(x, y, angle)", "rotates the current path");
		h.a("setColor(name)", "sets the current color");
		h.a("setColor(red, green, blue)", "sets the current color by RGB values");
		h.a("setColor(red, green, blue, alpha)", "sets the current color by RGB with alpha");
		h.a("translate(dx, dy)", "translates the current path");
		h.a("width", "returns the image width");
		return h;
	}
}
