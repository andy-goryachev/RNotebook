// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml3;
import goryachev.common.util.SB;


public class HBlock
    extends HAbstractBlock
{
	private final HStyle style;


	public HBlock(HDocument d, HStyle s)
	{
		super(d);
		this.style = s;
	}
	

	protected void toHtml(SB sb)
	{
		sb.a("<div class='").a(style.id).a("'>").nl();
		
		for(HAbstractSegment seg: segments())
		{
			seg.toHtml(sb);
		}
		
		sb.a("</div>").nl();
	}
}
