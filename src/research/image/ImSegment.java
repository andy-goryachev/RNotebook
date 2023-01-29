// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package research.image;
import java.awt.geom.Point2D;


public abstract class ImSegment
{
	public static enum Type
	{
		ARC,
		LINE,
		MOVE
	}
	
	//
	
	public abstract double getDirection();
	
	public abstract Point2D getPoint();
	
	//	
	
	public static double getDirection(ImSegment seg)
	{
		if(seg == null)
		{
			return 0;
		}
		else
		{
			return seg.getDirection();
		}
	}
	
	
	public static Point2D getPoint(ImSegment seg)
	{
		if(seg == null)
		{
			return new Point2D.Double(0, 0);
		}
		else
		{
			return seg.getPoint();
		}
	}
	
	
	public static ImSegment create(final double angle, ImSegment seg)
	{
		Point2D p = getPoint(seg);
		return create(p.getX(), p.getY(), angle);
	}


	public static ImSegment create(final double x, final double y, final double angle)
	{
		return new ImSegment()
		{
			public double getDirection()
			{
				return angle;
			}


			public Point2D getPoint()
			{
				return new Point2D.Double(x, y);
			}
		};
	}


	public static ImSegment line(final double x0, final double y0, final double x1, final double y1)
	{
		return new ImSegment()
		{
			public double getDirection()
			{
				return Math.atan2(y1 - y0, x1 - x0);
			}


			public Point2D getPoint()
			{
				return new Point2D.Double(x1, y1);
			}
		};
	}
}
