// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package research.image;
import goryachev.common.ui.UI;
import goryachev.common.util.UserException;
import goryachev.common.util.img.jhlabs.EmbossFilter;
import goryachev.common.util.img.jhlabs.GaussianFilter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import research.image.filters.CrispMaskFilter;
import research.image.filters.InverseMaskFilter;
import research.image.filters.MaskFilter;
import research.image.filters.MultiplyAlphaFilter;


public class ImTools
{
	public static UserException err(String msg)
	{
		return new UserException(msg);
	}
	

	public static double degreesToRadians(double degrees)
	{
		return Math.PI * degrees / 180;
	}
	
	
	public static Graphics2D graphics(BufferedImage im)
	{
		Graphics2D g = im.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		
		return g;
	}


	public static BufferedImage copy(BufferedImage x)
	{
		int w = x.getWidth();
		int h = x.getHeight();
		int type = x.getType();
		BufferedImage im = new BufferedImage(w, h, type);
		Graphics2D g = graphics(im);
		try
		{
			g.drawImage(x, 0, 0, null);
		}
		finally
		{
			g.dispose();
		}
		return im;
	}
	
	
	public static BufferedImage createCompatibleImage(BufferedImage src)
	{
		return new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
	}
	
	
	public static void fill(BufferedImage im, Color c)
	{
		Graphics2D g = graphics(im);
		g.setColor(c);
		g.fillRect(0, 0, im.getWidth(), im.getHeight());
		g.dispose();
	}


//	/** applies mask to the source image */
//	public static BufferedImage applyMask(BufferedImage src, BufferedImage mask)
//	{
//		BufferedImage dst = createCompatibleImage(src);
//		return new PaintThroughMaskFilter(mask, src).filter(dst, null);
//	}
	/** sets transparency of the source image with the values from the red channel of mask */
	public static BufferedImage applyMask(BufferedImage src, BufferedImage mask)
	{
		return new MultiplyAlphaFilter(mask).filter(src, null);
	}
	

	// paints overlay on top of base
	public static BufferedImage paintOver(BufferedImage im, BufferedImage overlay)
	{
		Graphics2D g = graphics(im);
		g.drawImage(overlay, 0, 0, null);
		g.dispose();
		return im;
	}
	
	
	// using opaque center color, not universal.  please use one below
	public static void fillHorizontalBarrel(BufferedImage im, Color center, float fraction)
	{
		Graphics2D g = graphics(im);
		Color topColor = UI.mix(fraction, center, Color.white);
		Color bottomColor = UI.mix(fraction, center, Color.black);
		
		int h = im.getHeight();
		int w = im.getWidth();
		int th = h / 2;
		int bh = h / 2;

		g.setPaint(new GradientPaint(0, 0, topColor, 0, th, center));
		g.fillRect(0, 0, w, th);

		g.setPaint(new GradientPaint(0, bh, center, 0, h, bottomColor));
		g.fillRect(0, bh, w, bh);
		
		g.dispose();
	}
	
	
	/** fills the image with "horizontal barrel effect" */
	public static void fillHorizontalBarrel(BufferedImage im, float intensity)
	{
		Graphics2D g = graphics(im);
		Color topColor = UI.setAlpha(Color.white, intensity);
		Color topCenter = UI.setAlpha(Color.white, 0);
		Color bottomColor = UI.setAlpha(Color.black, intensity);
		Color bottomCenter = UI.setAlpha(Color.black, 0);
		
		int h = im.getHeight();
		int w = im.getWidth();
		int half = h / 2;

		g.setPaint(new GradientPaint(0, 0, topColor, 0, half, topCenter));
		g.fillRect(0, 0, w, half);

		g.setPaint(new GradientPaint(0, half, bottomCenter, 0, h, bottomColor));
		g.fillRect(0, half, w, h);
		
		g.dispose();
	}


	public static BufferedImage translate(BufferedImage im, double dx, double dy)
	{
		BufferedImage rv = createCompatibleImage(im);
		
		Graphics2D g = graphics(rv);
		g.translate(dx, dy);
		g.drawImage(im, 0, 0, null); // or use drawImage with transform
		g.dispose();
		return rv;
	}
	
	
	public static BufferedImage innerShadowEffect(BufferedImage src, Color color, double depth, float dx, float dy)
	{
		BufferedImage mask = new MaskFilter().filter(src);
		BufferedImage outsideMask = new InverseMaskFilter().filter(src);
		BufferedImage blurredOutside = new GaussianFilter((float)depth).filter(outsideMask, null);
		fill(outsideMask, color);
		BufferedImage shadow = applyMask(outsideMask, blurredOutside);
		shadow = translate(shadow, dx, dy);
		BufferedImage rv = applyMask(shadow, mask);
		return rv;
	}
	
	
	public static BufferedImage outerShadow(BufferedImage src, Color color, double depth, double dx, double dy)
	{
		BufferedImage mask = new CrispMaskFilter().filter(src);
		BufferedImage blurred = new GaussianFilter((float)depth).filter(mask, null);
		blurred = translate(blurred, dx, dy);
		fill(mask, color);
		BufferedImage rv = applyMask(mask, blurred);
		rv = paintOver(rv, src);
		return rv;
	}
	
	
	public static BufferedImage paintThroughMask(BufferedImage mask, Color color)
	{
		BufferedImage im = createCompatibleImage(mask);
		fill(im, color);
		
		return applyMask(im, mask);
	}
	
	
	public static BufferedImage dropShadowEffect(BufferedImage src, int intensity, float radius, double dx, double dy)
	{
		BufferedImage mask = new CrispMaskFilter().filter(src);
		
		// FIX increase the size of temporary image
		BufferedImage blurred = new GaussianFilter(radius).filter(mask, null);
		
		// this step clips shadow
		blurred = translate(blurred, dx, dy);
		
		BufferedImage rv = paintThroughMask(blurred, new Color(0, 0, 0, intensity)); 
		
		rv = paintOver(rv, src);
		return rv;
	}


	public static BufferedImage outerShadowEffect(BufferedImage src, Color color, double depth, double dx, double dy)
	{
		BufferedImage mask = new MaskFilter().filter(src);
		
		BufferedImage outsideMask = new InverseMaskFilter().filter(src);
		
		BufferedImage blurred = new GaussianFilter((float)depth).filter(mask, null);
		
		blurred = translate(blurred, dx, dy);
		
		BufferedImage shadow = createCompatibleImage(src);
		fill(shadow, color);
		
		BufferedImage rv = applyMask(shadow, blurred);
		
		// FIX apply mask blends with (0,0,0,0 ?)
		
		rv = applyMask(rv, outsideMask);
		
		return rv;
	}
	
	
	public static void fill(BufferedImage im, Shape s, Color c)
	{
		Graphics2D g = graphics(im);
		g.setColor(c);
		g.fill(s);
		g.dispose();
	}	
	
	
	public static void draw(BufferedImage im, Shape s, Color c, BasicStroke stroke)
	{
		Graphics2D g = graphics(im);
		g.setColor(c);
		g.setStroke(stroke);
		g.fill(s);
		g.dispose();
	}	
	
	
	public static BufferedImage embossEffect(BufferedImage src)
	{
		float height = 0.5f;
		float azimuth = 25f;
		float elevation = 45f;
		
		EmbossFilter f = new EmbossFilter();
		f.setBumpHeight(height);
		f.setAzimuth((float)ImTools.degreesToRadians(azimuth));
		f.setElevation((float)ImTools.degreesToRadians(elevation));
		return f.filter(src, null);
	}
	
	
	public static BufferedImage positiveChamferEffect(BufferedImage src, double size)
	{
		double depth = size + size;
		
		BufferedImage white = outerShadowEffect(src, Color.white, depth, -size, -size);
		
		BufferedImage black = outerShadowEffect(src, Color.black, depth, size, size);
		
		BufferedImage im = paintOver(white, black);
		
		return im;
	}
	
	
	public static BufferedImage negativeChamferEffect(BufferedImage src, double size)
	{
		double depth = size + size;
		
		BufferedImage white = outerShadowEffect(src, Color.white, depth, size, size);
		
		BufferedImage black = outerShadowEffect(src, Color.black, depth, -size, -size);
		
		BufferedImage im = paintOver(black, white);
		
		return im;
	}
	
	
	/** android effect - punch with camfer */
	public static BufferedImage androidEffect(BufferedImage src, Color c)
	{
		// scaling effect parameters to the source image height
		double h = src.getHeight();
		double chamferSize = h / 100;
		double innerShadowSize = h / 33; // 3
		
		// positive mask
		//BufferedImage mask = new CrispMaskFilter().filter(src);
		BufferedImage mask = new MaskFilter().filter(src);
		
		// barrel gradient background for the inside of the icon
		BufferedImage background = createCompatibleImage(src);
		fillHorizontalBarrel(background, c, 0.7f);
		
		background = applyMask(background, mask);
		
		BufferedImage chamfer = negativeChamferEffect(background, chamferSize);
		
		BufferedImage rv = paintOver(background, chamfer);
		
		BufferedImage innerShadow = innerShadowEffect(mask, Color.black, innerShadowSize, 0, 0);
		
		rv = paintOver(rv, innerShadow);
		
		return rv;
	}
	
	
	/** android effect - punch with camfer, using the layer's pixels */
	public static BufferedImage androidEffectImage(BufferedImage src)
	{
		// scaling effect parameters to the source image height
		double h = src.getHeight();
		double chamferSize = h / 100;
		double innerShadowSize = h / 33; // 3
		
		// positive mask
		BufferedImage mask = new CrispMaskFilter().filter(src);
		
		// barrel gradient background for the inside of the icon
		BufferedImage background = createCompatibleImage(src);
		fillHorizontalBarrel(background, 0.5f);
		
		background = applyMask(background, mask);
		
		background = paintOver(src, background);
		
		BufferedImage chamfer = negativeChamferEffect(background, chamferSize);
		
		BufferedImage rv = paintOver(background, chamfer);
		
		BufferedImage innerShadow = innerShadowEffect(mask, Color.black, innerShadowSize, 0, 0);
		
		rv = paintOver(src, background);
		
		rv = paintOver(rv, innerShadow);
		
		return rv;
	}
	
	
	/** pure punch effect */
	public static BufferedImage punchEffect(BufferedImage src)
	{
		// scaling effect parameters to the source image height
		double h = src.getHeight();
		double innerShadowSize = 13 * h / 100;
		
		// positive mask
		BufferedImage mask = new CrispMaskFilter().filter(src);
		
		BufferedImage innerShadow = innerShadowEffect(mask, Color.black, innerShadowSize, 0, 0);
		
		BufferedImage rv = paintOver(src, innerShadow);
		
		return rv;
	}
}
