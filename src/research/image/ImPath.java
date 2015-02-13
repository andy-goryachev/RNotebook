// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package research.image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.Serializable;


public class ImPath
	implements Serializable
{
	protected final Path2D.Double path;
	protected AffineTransform transform;
	protected ImSegment last;


	public ImPath()
	{
		path = new Path2D.Double(PathIterator.WIND_NON_ZERO, 32);
	}
	
	
	// returns current point
	protected Point2D point()
	{
		return ImSegment.getPoint(last);
	}
	
	
	public void turn(double degrees)
	{
		double a = -ImTools.degreesToRadians(degrees) + direction();
		last = ImSegment.create(a, last);
	}
	
	
	public void setDirection(double degrees)
	{
		double a = ImTools.degreesToRadians(degrees);
		last = ImSegment.create(a, last);
	}
	
	
	// returns current stroke angle, radians
	protected double direction()
	{
		return ImSegment.getDirection(last);
	}
	
	
	public void arcClockwise(double radius, double angle)
	{
		arc(radius, angle, true);
	}
	
	
	public void arcCounterClockwise(double radius, double angle)
	{
		arc(radius, angle, false);
	}
	
	
	protected void arc(double radius, double angle, boolean clockwise)
	{
		// FIX
		double x = 0;
		double y = 0;
		double w = 0;
		double h = 0;
		double start = 0;
		double extent = 0;
		
		Arc2D.Double s = new Arc2D.Double(x, y, w, h, start, extent, Arc2D.OPEN);
		path.append(s, true);
	}
	
	
	public void move(double dx, double dy)
	{
		Point2D p = point();
		double x0 = p.getX();
		double y0 = p.getY();
		
		double x1 = x0 + dx;
		double y1 = y0 + dy;
		
		path.moveTo(x1, y1);
		
		last = ImSegment.line(x0, y0, x1, y1);
	}
	
	
	/** moves pointer to new coordinates, resetting direction vector to zero angle */
	public void moveTo(double x, double y)
	{
		path.moveTo(x, y);
		
		last = ImSegment.create(x, y, 0);
	}
	
	
	/** adds a line segment from the current point using absolute coordinates */
	public void lineTo(double x, double y)
	{
		path.lineTo(x, y);
		
		last = ImSegment.create(x, y, 0);
	}
	
	
	/* adds a line segment of the specified length from the current point following the current direction */
	public void line(double len)
	{
		Point2D p = point();
		double x0 = p.getX();
		double y0 = p.getY();
		double a = direction();
		
		double x1 = x0 + len * Math.cos(a);
		double y1 = y0 + len * Math.sin(a);
		
		path.lineTo(x1, y1);
		
		last = ImSegment.line(x0, y0, x1, y1);
	}
	
	
	/* adds a line segment relative to the current point */
	public void line(double dx, double dy)
	{
		Point2D p = point();
		double x0 = p.getX();
		double y0 = p.getY();
		
		double x1 = x0 + dx;
		double y1 = y0 + dy;
		
		path.lineTo(x1, y1);
		last = ImSegment.line(x0, y0, x1, y1);
	}


	public void curveTo(double x1, double y1, double x2, double y2, double x3, double y3)
	{
		path.curveTo(x1, y1, x2, y2, x3, y3);
		// FIX last
	}
	
	
	public void quadTo(double x1, double y1, double x2, double y2)
	{
		path.quadTo(x1, y1, x2, y2);
		// FIX last
	}
	
	
	public void quadRel(double dx1, double dy1, double dx2, double dy2)
	{
		Point2D p = point();
		
		double x1 = p.getX() + dx1;
		double y1 = p.getY() + dy1;
		
		double x2 = x1 + dx2;
		double y2 = y1 + dy2;
		
		path.quadTo(x1, y1, x2, y2);
		// FIX last
	}
	
	
	public void closePath()
	{
		path.closePath();
		// FIX last?
	}
	
	
	public void setWindingRule(int r)
	{
		path.setWindingRule(r);
	}


	public Path2D.Double getPath()
	{
		return path;
	}
	
	
	public void setTransform(AffineTransform t)
	{
		transform = t;
	}
	
	
	public AffineTransform getTransform()
	{
		return transform;
	}
	
	
	protected AffineTransform transform()
	{
		if(transform == null)
		{
			transform = new AffineTransform();
		}
		return transform;
	}
	
	
	public void translate(double dx, double dy)
	{
		transform().translate(dx, dy);
	}


	public void rotate(double x, double y, double angle)
	{
		double a = -ImTools.degreesToRadians(angle);
		transform().rotate(a, x, y);
	}
	
	
	public void scale(double scale)
	{
		transform.scale(scale, scale);
	}
}
