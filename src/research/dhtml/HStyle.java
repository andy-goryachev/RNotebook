// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.util.CMap;
import goryachev.common.util.SB;
import goryachev.common.util.UserException;
import goryachev.notebook.js.JsUtil;
import java.awt.Color;


public class HStyle
{
	public final String id;
	private CMap<HAttr,String> attributes = new CMap();
	
	
	public HStyle(String id)
	{
		this.id = id;
	}
	
	
	public HStyle backgroundColor(Object bg)
	{
		Color c = JsUtil.parseColor(bg);
		return attr(HAttr.BACKGROUND_COLOR, c);
	}
	
	
	public HStyle foregroundColor(Object fg)
	{
		Color c = JsUtil.parseColor(fg);
		return attr(HAttr.FOREGROUND_COLOR, c);
	}
	
	
	public HStyle paddingBottom(Object x)
	{
		return attr(HAttr.PADDING_BOTTOM, x);
	}
	
	
	public HStyle paddingLeft(Object x)
	{
		return attr(HAttr.PADDING_LEFT, x);
	}
	
	
	public HStyle paddingRight(Object x)
	{
		return attr(HAttr.PADDING_RIGHT, x);
	}
	
	
	public HStyle paddingTop(Object x)
	{
		return attr(HAttr.PADDING_TOP, x);
	}
	
	
	public HStyle fontFamily(Object x)
	{
		return attr(HAttr.FONT_FAMILY, x);
	}
	
	
	public HStyle fontStyle(Object x)
	{
		return attr(HAttr.FONT_STYLE, x);
	}
	
	
	protected HStyle attr(HAttr a, Object v)
	{
		if(attributes.containsKey(a))
		{
			throw new UserException("stylesheet already contains " + a);
		}
		
		String s = a.type.parseValue(v);
		attributes.put(a, s);
		return this;
	}


	public void emitHtml(SB sb)
	{
		sb.a(".");
		sb.a(id);
		sb.a(" {");
		
		for(HAttr a: attributes.keySet())
		{
			String v = attributes.get(a);
			String name = HtmlStyles.getCssName(a);
			sb.a(" ").a(name).a(":").a(v).a(";");
		}
		
		sb.a(" }").nl();
	}
}
