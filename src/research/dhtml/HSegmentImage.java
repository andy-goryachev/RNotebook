// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.Base64;
import goryachev.common.util.Log;
import goryachev.common.util.SB;
import java.awt.image.BufferedImage;


public class HSegmentImage
    extends HAbstractSegment
{
	private final byte[] bytes;
	
	
	public HSegmentImage(HDocument d, BufferedImage im)
	{
		super(d);
		this.bytes = toPNG(im);
	}


	public static byte[] toPNG(BufferedImage im)
	{
		try
		{
			return ImageTools.toPNG(im);
		}
		catch(Exception e)
		{
			Log.err(e);
			return null;
		}
	}


	protected void toHtml(SB sb)
	{
		sb.a("<img src=data:image/png;base64,");
		sb.a(Base64.encode(bytes));
		sb.a(" />").nl();
	}
}
