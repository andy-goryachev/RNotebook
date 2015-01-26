// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.image;
import goryachev.common.util.CMap;
import goryachev.common.util.UserException;
import java.awt.Color;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


/** this class encapsulates all the tools for image manipulation */
// TODO unclear:
// - how to show multiple layers?
public class ImageBuilder
{
	public static final String BASE_LAYER = "base.layer";
	public static final String CURRENT_PATH = "current.path";
	
	private int width;
	private int height;
	private boolean hasAlpha;
	
	private Color color;
	private double xpos;
	private double ypos;
	private double angle;
	private CMap<String,Object> objects;
	private ImPath currentPath;
	private ImLayer currentLayer;

	
	public ImageBuilder(int width, int height, boolean alpha)
	{
		this.width = width;
		this.height = height;
		this.hasAlpha = alpha;
	}
	
	
	public ImageBuilder(int width, int height)
	{
		this(width, height, false);
	}
	
	
	public ImageBuilder(BufferedImage im)
	{
		this.width = im.getWidth();
		this.height = im.getHeight();
		this.hasAlpha = im.getColorModel().hasAlpha();
		layer().setImage(im);
	}
	
	
	protected CMap<String,Object> objects()
	{
		if(objects == null)
		{
			objects = new CMap();
		}
		return objects;
	}
	
	
	protected Shape shape(String name)
	{
		Object x = objects.get(name);
		if(x == null)
		{
			throw new UserException("Not found: " + name);
		}
		else if(x instanceof Shape)
		{
			return (Shape)x;
		}
		throw new UserException("Object is not a Shape: " + x.getClass());
	}
	
	
	public ImPath path(String name)
	{
		ImPath p;
		Object v = objects().get(name);
		if(v instanceof ImPath)
		{
			p = (ImPath)v;
		}
		else if(v != null)
		{
			throw new UserException("Existing object is not a path: " + name);
		}
		else
		{
			p = new ImPath();
			objects().put(name, p);
		}
		
		currentPath = p;
		return p;
	}
	
	
	protected ImPath path()
	{
		if(currentPath == null)
		{
			path(CURRENT_PATH);
		}
		return currentPath;
	}
	
	
	public ImLayer layer(String name)
	{
		ImLayer la;
		Object v = objects().get(name);
		if(v instanceof ImLayer)
		{
			la = (ImLayer)v;
		}
		else if(v != null)
		{
			throw new UserException("Existing object is not a layer: " + name);
		}
		else
		{
			la = new ImLayer(width, height, hasAlpha);
			objects().put(name, la);
		}
		
		currentLayer = la;
		return la;
	}
	
	
	protected ImLayer layer()
	{
		if(currentLayer == null)
		{
			layer(BASE_LAYER);
		}
		return currentLayer;
	}
	
	
	public BufferedImage getBufferedImage()
	{
		// TODO combine all layers?
		return layer(BASE_LAYER).getImage();
	}
	
	
	public int getWidth()
	{
		return width;
	}
	

	public int getHeight()
	{
		return height;
	}
	
	
	public boolean hasAlpha()
	{
		return hasAlpha;
	}
	
	
	public void invert()
	{
		layer().invert();
	}
	
	
//	public void scale(double factor)
//	{
//		commit();
//		
//		int w = (int)Math.round(getWidth() * factor);
//		int h = (int)Math.round(getHeight() * factor);
//		image = ImageScaler.resize(image, ImageTools.hasAlpha(image), w, h, true);
//	}


	public void setColor(Color c)
    {
		color = c;
    }
	
	
	public Color getColor()
	{
		if(color == null)
		{
			return Color.black;
		}
		return color;
	}
	
	
	public void fillRect(double x, double y, double w, double h)
	{
		layer().fill(new Rectangle2D.Double(x, y, w, h), getColor());
	}
	
	
	public void fill(String name)
	{
		Shape s = shape(name);
		layer().fill(s, getColor());
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
	
	
	public void blur(float radius)
	{
		layer().blur(radius);
	}
	
	
	public void setImage(Image im)
	{
		layer().setImage(im);
	}
}
