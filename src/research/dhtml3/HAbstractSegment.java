// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package research.dhtml3;
import goryachev.common.util.SB;


public abstract class HAbstractSegment
{
	protected abstract void toHtml(SB sb);
	
	//
	
	private final HDocument document;
	
	
	public HAbstractSegment(HDocument parent)
	{
		this.document = parent;
	}
	
	
	public HDocument document()
	{
		return document;
	}
}
