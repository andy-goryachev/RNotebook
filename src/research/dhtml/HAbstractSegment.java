// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.dhtml;
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
