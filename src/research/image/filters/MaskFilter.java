// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package research.image.filters;
import java.awt.image.BufferedImage;


/** 
 * This filter generates an alpha-channel "mask" from an image:
 * The alpha channel is preserved, and non-transparent pixels turn red (only for debugging purposes),
 * since only the alpha channel is important.  
 */
public class MaskFilter
    extends ImFilter
{
	public MaskFilter()
	{
	}
	
	
	protected int getColorFromAlpha(int alpha)
	{
		return ((alpha & 0xff) << 24) | 0x00ff0000;
	}
	
	
	public void filter(BufferedImage src, BufferedImage dst)
	{
		int w = src.getWidth();
		int h = src.getHeight();
		
		for(int x=0; x<w; x++)
		{
			for(int y=0; y<h; y++)
			{
				int argb = src.getRGB(x, y);
				int color = getColorFromAlpha(argb >>> 24);
				dst.setRGB(x, y, color);
			}
		}
	}
}
