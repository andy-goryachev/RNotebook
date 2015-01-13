// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.util.CException;


public class HtmlStyles
{
	public static String getCssName(HAttr a)
	{
		switch(a)
		{
		case BACKGROUND_COLOR: return "background";
		case FOREGROUND_COLOR: return "color";
		default:
			throw new CException("unknown attribute " + a);
		}
	}
}
