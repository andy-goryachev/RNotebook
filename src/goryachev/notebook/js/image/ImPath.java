// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.Serializable;


public class ImPath
	implements Serializable
{
	protected Path2D.Double path = new Path2D.Double();


	public ImPath()
	{
		path.setWindingRule(PathIterator.WIND_NON_ZERO);
	}
	
	
	public void moveTo(double x, double y)
	{
		path.moveTo(x, y);
	}
	
	
	public void lineTo(double x, double y)
	{
		path.lineTo(x, y);
	}
	
	
	public void lineRel(double dx, double dy)
	{
		Point2D p = path.getCurrentPoint();
		path.lineTo(p.getX() + dx, p.getY() + dy);
	}


	public void curveTo(double x1, double y1, double x2, double y2, double x3, double y3)
	{
		path.curveTo(x1, y1, x2, y2, x3, y3);
	}
	
	
	public void quadTo(double x1, double y1, double x2, double y2)
	{
		path.quadTo(x1, y1, x2, y2);
	}
	
	
	public void quadRel(double dx1, double dy1, double dx2, double dy2)
	{
		Point2D p = path.getCurrentPoint();
		
		double x1 = p.getX() + dx1;
		double y1 = p.getY() + dy1;
		
		double x2 = x1 + dx2;
		double y2 = y1 + dy2;
		
		path.quadTo(x1, y1, x2, y2);
	}
	
	
	public void closePath()
	{
		path.closePath();
	}
	
	
	public void translate(double dx, double dy)
	{
		AffineTransform t = new AffineTransform();
		t.translate(dx, dy);
		path.transform(t);
	}


	public void rotate(double x, double y, double angle)
	{
		AffineTransform t = new AffineTransform();
		t.rotate(angle, x, y);
		path.transform(t);
	}
	
	
	public void scale(double scale)
	{
		AffineTransform t = new AffineTransform();
		t.scale(scale, scale);
		path.transform(t);
	}
	
	
	public void setWindingRule(int r)
	{
		path.setWindingRule(r);
	}


	public Path2D.Double getPath()
	{
		return path;
	}
}
