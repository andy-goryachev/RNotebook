// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.CSorter;
import goryachev.common.util.SB;


// idea 1:
//   html specific getHead(), getBody(), getLastTable()
// idea 2:
//   logical structure only, rendered in html, pdf.
//   layout types: segment, table
//   styles: font, colors, borders, padding, margins
public class HDocument
{
	private CList<HAbstractSegment> segments = new CList();
	private CMap<String,HStyle> styles = new CMap();
	private HSegment lastSegment;
	private HTable lastTable;
	
	
	public HDocument()
	{
	}
	
	
	public HStyle style(String id)
	{
		HStyle s = styles.get(id);
		if(s == null)
		{
			s = new HStyle(id);
			styles.put(id, s);
		}
		return s;
	}
	
	
	public HStyle getStyle(Object id)
	{
		return styles.get(id.toString());
	}
	
	
	public HSegment heading1(String text)
	{
		HSegment s = new HSegment(HSegmentType.HEADING1, text);
		return append(s);
	}
	
	
	public HSegment heading2(String text)
	{
		HSegment s = new HSegment(HSegmentType.HEADING2, text);
		return append(s);
	}
	
	
	public HSegment heading3(String text)
	{
		HSegment s = new HSegment(HSegmentType.HEADING3, text);
		return append(s);
	}
	
	
	public HSegment text(Object style, String text)
	{
		HStyle s = getStyle(style);
		return append(new HSegment(HSegmentType.TEXT, s, text));
	}
	
	
	protected HSegment append(HSegment s)
	{
		segments.add(s);
		setLastSegment(s);
		return s;
	}
	
	
	protected void setLastSegment(HSegment s)
	{
		lastSegment = s;
		if(s instanceof HTable)
		{
			lastTable = (HTable)s;
		}
	}
	
	
	public HTable newTable()
	{
		return null;
	}
	
	
	public HTable getLastTable()
	{
		return lastTable;
	}
	
	
	public HSegment getLast()
	{
		return lastSegment;
	}
	
	
	public String toHtml()
	{
		SB sb = new SB();
		sb.a("<html>").nl();
		emitHtmlHead(sb);
		emitHtmlBody(sb);
		sb.a("</html>").nl();
		return sb.toString();
	}
	
	
	protected void emitHtmlHead(SB sb)
	{
		sb.a("<head>").nl();
		if(styles.size() > 0)
		{
			sb.a("<style>").nl();
			
			CList<String> ids = new CList(styles.keySet());
			CSorter.sort(ids);
			
			for(String id: ids)
			{
				HStyle st = styles.get(id);
				st.emitHtml(sb);
			}
			
			sb.a("</style>").nl();
		}
		sb.a("</head>").nl();
	}
	
	
	protected void emitHtmlBody(SB sb)
	{
		sb.a("<body>").nl();
		
		for(HAbstractSegment seg: segments)
		{
			seg.toHtml(sb);
		}
		
		sb.a("</body>").nl();
	}
}
