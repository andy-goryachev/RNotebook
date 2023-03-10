// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package research.dhtml3;
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
