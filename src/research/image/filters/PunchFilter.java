// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package research.image.filters;
import goryachev.swing.img.jhlabs.AbstractBufferedImageOp;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class PunchFilter
    extends AbstractBufferedImageOp
{
	private float radius;
	private Color color;


	public PunchFilter(float radius, Color color)
	{
		this.radius = radius;
		this.color = color;
	}


	public String toString()
	{
		return "PunchFilter";
	}
	
	
	protected boolean hasPixel(BufferedImage im, int x, int y)
	{
		int rgb = im.getRGB(x, y);
		return ((rgb & 0xff000000) != 0);
	}


	public BufferedImage filter(BufferedImage src, BufferedImage dst)
	{
		int w = src.getWidth();
		int h = src.getHeight();
		
		Graphics2D g = dst.createGraphics();
		BasicStroke stroke = new BasicStroke(radius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g.setStroke(stroke);
		g.setColor(color);
		
		// horizontal lines first
		for(int y=0; y<h; y++)
		{
			int start = -1;
			for(int x=0; x<w; x++)
			{
				if(hasPixel(src, x, y))
				{
					start = x;
					break;
				}
			}
			
			int end = start;
			if(start >= 0)
			{
				for(int x=w-1; x>start; --x)
				{
					if(hasPixel(src, x, y))
					{
						end = x;
						break;
					}
				}
			}
			
			if(end >= 0)
			{
				g.drawLine(start, y, end, y);
			}
		}
		
		// then vertical lines
		for(int x=0; x<w; x++)
		{
			int start = -1;
			for(int y=0; y<h; y++)
			{
				if(hasPixel(src, x, y))
				{
					start = y;
					break;
				}
			}
			
			int end = start;
			if(start >= 0)
			{
				for(int y=h-1; y>start; --y)
				{
					if(hasPixel(src, x, y))
					{
						end = y;
						break;
					}
				}
			}
			
			if(end >= 0)
			{
				g.drawLine(x, start, x, end);
			}
		}
		
		g.dispose();

		return dst;
	}
}
