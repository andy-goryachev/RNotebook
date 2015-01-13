// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.util.html.HtmlTools;
import goryachev.notebook.js.JsUtil;
import java.awt.Color;


public enum HAttrType
{
	COLOR
	{
		public String parseValue(Object x)
		{
			Color c = JsUtil.parseColor(x);
			return HtmlTools.color(c);
		}
	},
	FONT
	{
		public String parseValue(Object x)
		{
			return x.toString();
		}
	},
	FONT_STYLE
	{
		public String parseValue(Object x)
		{
			// normal, italic, oblique
			return x.toString();
		}
	},
	LIN
	{
		public String parseValue(Object x)
		{
			// px em
			return x.toString();
		}
	};
	
	//
	
	// TODO split checkValue and parseValue (html/pdf)
	public abstract String parseValue(Object x);
}