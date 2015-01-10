// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.UserException;


// idea 1:
//   html specific getHead(), getBody(), getLastTable()
// idea 2:
//   logical structure only, rendered in html, pdf.
//   layout types: segment, table
//   styles: font, colors, borders, padding, margins
public class HDocument
{
	private CList<HSegment> segments = new CList();
	private CMap<String,HStyle> styles = new CMap();
	private HSegment lastSegment;
	private HTable lastTable;
	
	
	public HDocument()
	{
	}
	
	
	public HStyle newStyle(String id)
	{
		if(styles.containsKey(id))
		{
			throw new UserException("style already exists: " + id);
		}
		
		return styles.put(id, new HStyle(id));
	}
	
	
	public HStyle getStyle(String id)
	{
		return styles.get(id);
	}
	
	
	public HSegment heading1(Object v)
	{
		HSegment s = new HSegment(null, v);
		return append(s);
	}
	
	
	public HSegment append(HStyle s, Object v)
	{
		HSegment seg;
		if(v instanceof HSegment)
		{
			seg = (HSegment)v;
		}
		else
		{
			seg = new HSegment(s, v);
		}
		
		return append(seg);
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
		// TODO emit head, stylesheet, body
		return null;
	}
}
