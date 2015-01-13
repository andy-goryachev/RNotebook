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
	
	
	public HSegment(HDocument d, HSegmentType t, HStyle s, Object v)
	{
		super(d);
		this.type = t;
		this.style = s;
		this.segment = v;
	}
	
	
	public HSegment(HDocument d, HSegmentType t, Object v)
	{
		super(d);
		this.type = t;
		this.segment = v;
	}
	
	
	public HSegment(HDocument d, HSegmentType t)
	{
		super(d);
		this.type = t;
	}
	
	
	protected String getHtmlTag()
	{
		switch(type)
		{
		case HEADING1: return "h1";
		case HEADING2: return "h2";
		case HEADING3: return "h3";
		case NEWLINE: return "br";
		default:       return null;
		}
	}
	
	
	protected void toHtml(SB sb)
	{
		String tag = getHtmlTag();
		if(tag == null)
		{
			if(style != null)
			{
				sb.a("<div class='").a(style.id).a("'>");
			}
			
			sb.a(HtmlTools.safe(segment));
			
			if(style != null)
			{
				sb.a("</div>");
			}
		}
		else
		{
			if(segment == null)
			{
				sb.a("<").a(tag).a(" />");
			}
			else
			{
				sb.a("<").a(tag);
	
				if(style != null)
				{
					sb.a(" class='").a(style.id).a("'");
				}
				
				sb.a(">");
				sb.a(HtmlTools.safe(segment));
				sb.a("</").a(tag).a(">");
			}
		}
		sb.nl();
	}
}
