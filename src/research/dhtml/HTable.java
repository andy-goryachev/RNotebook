// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
import goryachev.common.util.SB;


public class HTable
	extends HSegment
{
	public HTable(HDocument d)
	{
		super(d, HSegmentType.TABLE);
	}
	
	
	protected void toHtml(SB sb)
	{
		sb.a("<table>").nl();
		// header
		// rows, columns
		sb.a("<table>").nl();
	}
}
