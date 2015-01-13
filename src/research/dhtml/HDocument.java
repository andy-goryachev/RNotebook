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
	extends HAbstractBlock
{
	private CMap<String,HStyle> styles = new CMap();
	private HAbstractSegment lastSegment;
	private HTable lastTable;
	
	
	public HDocument()
	{
		super(null);
	}
	
	
	public HDocument document()
	{
		return this;
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
	
	
	protected void setLastSegment(HAbstractSegment s)
	{
		lastSegment = s;
		if(s instanceof HTable)
		{
			lastTable = (HTable)s;
		}
	}
	
	
	public HTable getLastTable()
	{
		return lastTable;
	}
	
	
	public HAbstractSegment getLast()
	{
		return lastSegment;
	}
	
	
	public String toHtml()
	{
		SB sb = new SB();
		toHtml(sb);
		return sb.toString();
	}
	
	
	protected void toHtml(SB sb)
	{
		sb.a("<html>").nl();
		emitHtmlHead(sb);
		emitHtmlBody(sb);
		sb.a("</html>").nl();	
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
		
		for(HAbstractSegment seg: segments())
		{
			seg.toHtml(sb);
		}
		
		sb.a("</body>").nl();
	}
}
