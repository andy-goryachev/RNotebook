// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.util.SB;
import goryachev.common.util.html.HtmlTools;


public class HSegment
	extends HAbstractSegment
{
	private final HSegmentType type;
	private HStyle style;
	private Object segment;
	
	
	public HSegment(HSegmentType t, HStyle s, Object v)
	{
		this.type = t;
		this.style = s;
		this.segment = v;
	}
	
	
	public HSegment(HSegmentType t, Object v)
	{
		this.type = t;
		this.segment = v;
	}
	
	
	public HSegment(HSegmentType t)
	{
		this.type = t;
	}
	
	
	protected String getHtmlTag()
	{
		switch(type)
		{
		case HEADING1: return "h1";
		case HEADING2: return "h2";
		case HEADING3: return "h3";
		default:       return "span";
		}
	}
	
	
	protected void toHtml(SB sb)
	{
		String tag = getHtmlTag();
		
		sb.a("<").a(tag);

		if(style != null)
		{
			sb.a(" style='").a(style.id).a("'");
		}
		
		sb.a(">");
		sb.a(HtmlTools.safe(segment));
		sb.a("</").a(tag).a(">").nl();
	}
}
