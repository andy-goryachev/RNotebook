// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package research.dhtml3;
import goryachev.common.log.Log;
import goryachev.common.util.Base64;
import goryachev.common.util.SB;
import goryachev.swing.ImageTools;
import java.awt.image.BufferedImage;


public class HSegmentImage
    extends HAbstractSegment
{
	static Log log = Log.get("HSegmentImage");
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
			log.error(e);
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
