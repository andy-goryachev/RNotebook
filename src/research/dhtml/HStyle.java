// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.util.SB;
import goryachev.common.util.html.HtmlTools;
import goryachev.notebook.js.JsUtil;
import java.awt.Color;


public class HStyle
{
	public final String id;
	private Color foreground;
	
	
	public HStyle(String id)
	{
		this.id = id;
	}
	
	
	public HStyle useForeground(Object c)
	{
		foreground = JsUtil.parseColor(c);
		return this;
	}


	public void emitHtml(SB sb)
	{
		sb.a(".");
		sb.a(id);
		sb.a(" {");
		
		if(foreground != null)
		{
			sb.a(" color:").a(HtmlTools.color(foreground)).a(";");
		}
		
		sb.a(" }").nl();
	}
}
