// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package research.image;
import goryachev.swing.ImageTools;
import goryachev.swing.img.jhlabs.GaussianFilter;
import goryachev.swing.img.jhlabs.InvertFilter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;


public class ImLayer
{
	private BufferedImage image;
	
	
	public ImLayer(int w, int h, boolean alpha)
	{
		image = new BufferedImage(w, h, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
	}
	
	
	protected ImLayer(BufferedImage im)
	{
		image = im;
	}
	
	
	public ImLayer copy()
	{
		BufferedImage im = ImTools.copy(image);
		return new ImLayer(im);
	}
	
	
	public void setImage(Image im)
	{
		image = ImageTools.copyImageRGB(im);
	}
	
	
	protected Graphics2D gr()
	{
		// TODO perhaps these should depend on settings
		Graphics2D g = image.createGraphics();
		// quality
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		// anti aliasing
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// fractional metrics
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		return g;
	}
	
	
	public int getWidth()
	{
		return image.getWidth();
	}
	
	
	public int getHeight()
	{
		return image.getHeight();
	}
	
	
	public void fill(Shape s, Color c, AffineTransform t)
	{
		Graphics2D g = gr();
		try
		{
			g.setColor(c);
			if(t != null)
			{
				g.setTransform(t);
			}
			g.fill(s);
		}
		finally
		{
			g.dispose();
		}
	}
	
	
	public void fill(Color c)
	{
		ImTools.fill(image, c);
	}
	
	
	public void draw(Shape s, Color c, BasicStroke stroke, AffineTransform t)
	{
		Graphics2D g = gr();
		try
		{
			g.setColor(c);
			g.setStroke(stroke);
			if(t != null)
			{
				g.setTransform(t);
			}
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
		Graphics2D g = gr();
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
		double a0 = ImTools.degreesToRadians(angle);
		
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
		double a0 = -ImTools.degreesToRadians(angle);
		
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
		
		double a0 = -ImTools.degreesToRadians(angle);
		
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
		Graphics2D g = gr();
		try
		{
			g.translate(offsetx, offsety);
			g.drawImage(im, 0, 0, null);
		}
		finally
		{
			g.dispose();
		}
	}


	public BufferedImage getImage()
	{
		return image;
	}
	
	
	public void blur(float radius)
	{
		image = new GaussianFilter(radius).filter(image, null);
	}
	

	public void innerShadow(Color color, float depth, float dx, float dy)
	{
		BufferedImage shadow = ImTools.innerShadowEffect(image, color, depth, dx, dy);
		image = ImTools.paintOver(image, shadow);
	}
	
	
	public void dropShadow(int intensity, float radius, float dx, float dy)
	{
		BufferedImage shadow = ImTools.dropShadowEffect(image, intensity, radius, dx, dy);
		image = ImTools.paintOver(shadow, image);
	}
	

	public void outerShadow(Color color, float depth, float dx, float dy)
	{
		image = ImTools.outerShadow(image, color, depth, dx, dy);
	}
	
	
	public void androidEffect(Color c)
	{
		image = ImTools.androidEffect(image, c);
	}
	
	
	// what is it?
	public void androidEffectImage()
	{
		image = ImTools.androidEffectImage(image);
	}
	
	
	public void punchEffect()
	{
		image = ImTools.punchEffect(image);
	}
	
	
	public void invert()
	{
		image = new InvertFilter().filter(image, null);
	}
}
