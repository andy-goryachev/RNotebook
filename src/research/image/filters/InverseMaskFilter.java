// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package research.image.filters;


/** 
 * Inverts pixel transparency:
 * Transparent pixels become fully opaque (red).
 * Non-transparent pixels change alpha value to (255 - alpha).
 */
public class InverseMaskFilter
    extends MaskFilter
{
	public InverseMaskFilter()
	{
	}
	
	
	protected int getColorFromAlpha(int alpha)
	{
		alpha = 255 - (alpha & 0xff);
		return (alpha << 24) | 0x00ff0000;
	}
}
