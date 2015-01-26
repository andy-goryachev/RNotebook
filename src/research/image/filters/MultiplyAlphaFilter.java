// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package research.image.filters;
import goryachev.common.util.img.jhlabs.AbstractBufferedImageOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


/**
 * Multiples alpha channel of the source image by alpha channel of the mask image.
 */
public class MultiplyAlphaFilter
    extends AbstractBufferedImageOp
{
	private BufferedImage maskImage;


	public MultiplyAlphaFilter()
	{
	}


	public MultiplyAlphaFilter(BufferedImage mask)
	{
		setMask(mask);
	}


	public void setMask(BufferedImage im)
	{
		this.maskImage = im;
	}


	public BufferedImage getMask()
	{
		return maskImage;
	}


	public static void processMask(Raster src, Raster mask, WritableRaster dst)
	{
		int x = src.getMinX();
		int y = src.getMinY();
		int w = src.getWidth();
		int h = src.getHeight();

		int rgb[] = null;
		int maskRGB[] = null;

		for(int iy=0; iy<h; iy++)
		{
			rgb = src.getPixels(x, y, w, 1, rgb);
			maskRGB = mask.getPixels(x, y, w, 1, maskRGB);

			// points to alpha value
			int i = (x * 4) + 3; 
			
			for(int ix=0; ix<w; ix++)
			{
				// source alpha
				int a = rgb[i];

				// using RED channel
				int ma = maskRGB[i];
								
				// modifying alpha channel only
				a = (a * ma / 255);
				rgb[i] = a;
				
				i += 4;
			}

			dst.setPixels(x, y, w, 1, rgb);
			y++;
		}
	}


	public BufferedImage filter(BufferedImage src, BufferedImage dst)
	{
		int width = src.getWidth();
		int height = src.getHeight();
		int type = src.getType();

		if(dst == null)
		{
			dst = createCompatibleDestImage(src, null);
		}

		if(maskImage != null)
		{
			processMask(src.getRaster(), maskImage.getRaster(), dst.getRaster());
		}

		return dst;
	}


	public String toString()
	{
		return "AlphaFilter";
	}
}
