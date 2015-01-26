// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.image;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.img.jhlabs.GaussianFilter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;


public class ImLayer
{
	private BufferedImage buffer;
	
	
	public ImLayer(int w, int h)
	{
		buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	}
	
	
	protected ImLayer(BufferedImage im)
	{
		buffer = im;
	}
	
	
	public ImLayer copy()
	{
		BufferedImage im = ImTools.copy(buffer);
		return new ImLayer(im);
	}
	
	
	protected Graphics2D graphics()
	{
		return buffer.createGraphics(); 
	}
	
	
	public int getWidth()
	{
		return buffer.getWidth();
	}
	
	
	public int getHeight()
	{
		return buffer.getHeight();
	}
	
	
	public void fill(Shape s, Color c, BasicStroke stroke)
	{
		ImTools.fill(buffer, s, c, stroke);
	}
	
	
	public void fill(Color c)
	{
		ImTools.fill(buffer, c);
	}
	
	
	public void draw(Shape s, Color c, BasicStroke stroke, double offsetx, double offsety)
	{
		Graphics2D g = ImTools.graphics(buffer);
		try
		{
			g.setColor(c);
			g.setStroke(stroke);
			g.translate(offsetx, offsety);
			g.draw(s);
		}
		finally
		{
			g.dispose();
		}
	}


	public void drawText(int x, int y, String text, Font f, Color c)
	{
		// TODO perhaps should use a text pane to handle html formatting and/or newlines
		Graphics2D g = ImTools.graphics(buffer);
		try
		{
			g.setColor(c);
			g.setFont(f);
			
			int a = g.getFontMetrics(f).getMaxAscent();
			g.drawString(text, x, y + a);
		}
		finally
		{
			g.dispose();
		}
	}
	
	
	public void polygon(Path2D.Double p, int corners, float xcenter, float ycenter, float radius, float angle)
	{
		double a0 = ImTools.toRadians(angle);
		
		p.setWindingRule(0);
		
		boolean moveTo = true;
		for(int i=0; i<(corners+1); i++)
		{
			double a = a0 + ((Math.PI * 2 * i) / corners);
			double x = xcenter + radius * Math.cos(a);
			double y = ycenter + radius * Math.sin(a);
			
			if(moveTo)
			{
				p.moveTo(x, y);
				moveTo = false;
			}
			else
			{
				p.lineTo(x, y);
			}
		}
	}
	

	public void star(Path2D.Double p, int spikes, double xcenter, double ycenter, double majorRadius, double minorRadius, double angle)
	{
		double a0 = -ImTools.toRadians(angle);
		
		int steps = spikes * 2;
		boolean moveTo = true;
		boolean major = true;
		for(int i=0; i<steps; i++)
		{
			double a = a0 + ((Math.PI * 2 * i) / steps);
			
			double r = major ? majorRadius : minorRadius;
			major = !major;
				
			double x = xcenter + r * Math.cos(a);
			double y = ycenter + r * Math.sin(a);
			
			if(moveTo)
			{
				p.moveTo(x, y);
				moveTo = false;
			}
			else
			{
				p.lineTo(x, y);
			}
		}
	}
	
	
	public void star2(Path2D.Double p, int spikes, float xcenter, float ycenter, float radius, int skipCount, float angle)
	{
		p.setWindingRule(PathIterator.WIND_NON_ZERO); // produces solid star, while WIND_EVEN_ODD results in a star with a hole
		
		double a0 = -ImTools.toRadians(angle);
		
		boolean moveTo = true;
		int sz = spikes + 1;
		int ix = 0;
		for(int i=0; i<spikes; i++)
		{
			double a = a0 + ((Math.PI * 2 * ix) / spikes);
			
			double x = xcenter + radius * Math.cos(a);
			double y = ycenter + radius * Math.sin(a);
			
			if(moveTo)
			{
				p.moveTo(x, y);
				moveTo = false;
			}
			else
			{
				p.lineTo(x, y);
			}
			
			ix = (ix + 1 + skipCount) % spikes;
		}
	}
	
	
	public void drawImage(Image im, double offsetx, double offsety)
	{
		Graphics2D g = graphics();
		g.translate(offsetx, offsety);
		g.drawImage(im, 0, 0, null);
		g.dispose();
	}


	public BufferedImage getImage()
	{
		return buffer;
	}
	
	
	public void blur(float radius)
	{
		buffer = new GaussianFilter(radius).filter(buffer, null);
	}
	

	public void innerShadow(Color color, float depth, float dx, float dy)
	{
		BufferedImage shadow = ImTools.innerShadowEffect(buffer, color, depth, dx, dy);
		buffer = ImTools.paintOver(buffer, shadow);
	}
	
	
	public void dropShadow(int intensity, float radius, float dx, float dy)
	{
		BufferedImage shadow = ImTools.dropShadowEffect(buffer, intensity, radius, dx, dy);
		buffer = ImTools.paintOver(shadow, buffer);
	}
	

	public void outerShadow(Color color, float depth, float dx, float dy)
	{
		buffer = ImTools.outerShadow(buffer, color, depth, dx, dy);
	}
	
	
	public void androidEffect(Color c)
	{
		buffer = ImTools.androidEffect(buffer, c);
	}
	
	
	public void androidEffectImage()
	{
		buffer = ImTools.androidEffectImage(buffer);
	}
	
	
	public void punchEffect()
	{
		buffer = ImTools.punchEffect(buffer);
	}
}
