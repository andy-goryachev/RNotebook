// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package research.image.filters;
import java.awt.image.BufferedImage;
import research.image.ImTools;


public abstract class ImFilter
{
	public abstract void filter(BufferedImage src, BufferedImage dst);
	
	
	//
	
	
	public BufferedImage filter(BufferedImage src)
	{
		BufferedImage dst = ImTools.copy(src);
		filter(src, dst);
		return dst;
	}
}
