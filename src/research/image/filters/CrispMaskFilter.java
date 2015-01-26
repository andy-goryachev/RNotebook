// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package research.image.filters;
import java.awt.image.BufferedImage;


/** 
 * This filter generates an alpha-channel "mask" from an image:
 * a fully transparent pixels remain transparent, any other (partially transparent or opaque) pixels
 * become opaque (in red color, for visual debugging purposes).
 */
public class CrispMaskFilter
    extends MaskFilter
{
	public CrispMaskFilter()
	{
	}
	
	
	protected int getColorFromAlpha(int alpha)
	{
		alpha = (alpha == 0) ? 0 : 255;
		return (alpha << 24) | 0x00ff0000;
	}
}
