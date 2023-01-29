// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package research.dhtml3;


public enum HAttr
{
	BACKGROUND_COLOR(HAttrType.COLOR),
	FONT_FAMILY(HAttrType.FONT),
	FONT_STYLE(HAttrType.FONT_STYLE),
	FOREGROUND_COLOR(HAttrType.COLOR),
	PADDING_BOTTOM(HAttrType.LIN),
	PADDING_LEFT(HAttrType.LIN),
	PADDING_RIGHT(HAttrType.LIN),
	PADDING_TOP(HAttrType.LIN),
	;
	
	//
	
	public final HAttrType type;
	
	
	HAttr(HAttrType t)
	{
		this.type = t;
	}
}
