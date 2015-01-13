// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;


public enum HAttr
{
	BACKGROUND_COLOR(HAttrType.COLOR),
	FOREGROUND_COLOR(HAttrType.COLOR);
	
	//
	
	public final HAttrType type;
	
	
	HAttr(HAttrType t)
	{
		this.type = t;
	}
}
