// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.util.Rex;


public class HtmlStyles
{
	public static String getCssName(HAttr a)
	{
		switch(a)
		{
		case BACKGROUND_COLOR: return "background";
		case FONT_FAMILY: return "font-family";
		case FONT_STYLE: return "font-style";
		case FOREGROUND_COLOR: return "color";
		case PADDING_BOTTOM: return "padding-bottom";
		case PADDING_LEFT: return "padding-left";
		case PADDING_RIGHT: return "padding-right";
		case PADDING_TOP: return "padding-top";
		default:
			throw new Rex("unknown attribute " + a);
		}
	}
}
