// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.util.html.HtmlTools;
import java.awt.Color;


public enum HAttrType
{
	COLOR
	{
		public String parseValue(Object x)
		{
			return HtmlTools.toColorString((Color)x);
		}
	};
	
	//
	
	public abstract String parseValue(Object x);
}