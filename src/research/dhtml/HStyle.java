// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.util.SB;
import goryachev.common.util.html.HtmlTools;
import goryachev.notebook.js.JsUtil;
import java.awt.Color;


public class HStyle
{
	public final String id;
	// TODO or use hashmap
	private Color backgroundColor;
	private Color foregroundColor;
	
	
	public HStyle(String id)
	{
		this.id = id;
	}
	
	
	public HStyle backgroundColor(Object c)
	{
		backgroundColor = JsUtil.parseColor(c);
		return this;
	}
	
	
	public HStyle foregroundColor(Object c)
	{
		foregroundColor = JsUtil.parseColor(c);
		return this;
	}


	public void emitHtml(SB sb)
	{
		sb.a(".");
		sb.a(id);
		sb.a(" {");
		
		if(backgroundColor != null)
		{
			sb.a(" background:").a(HtmlTools.color(backgroundColor)).a(";");
		}
		
		if(foregroundColor != null)
		{
			sb.a(" color:").a(HtmlTools.color(foregroundColor)).a(";");
		}
		
		sb.a(" }").nl();
	}
}
