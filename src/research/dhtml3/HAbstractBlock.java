// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package research.dhtml3;
import goryachev.common.util.CList;
import java.awt.image.BufferedImage;


public abstract class HAbstractBlock
	extends HAbstractSegment
{
	private CList<HAbstractSegment> segments = new CList();

	
	public HAbstractBlock(HDocument d)
	{
		super(d);
	}
	
	
	protected CList<HAbstractSegment> segments()
	{
		return segments;
	}
	
	
	public HBlock block(String style)
	{
		HStyle st = document().style(style);
		return block(st);
	}
	
	
	public HBlock block(HStyle s)
	{
		HBlock b = new HBlock(document(), s);
		append(b);
		return b;
	}
	
	
	public HAbstractBlock heading1(String text)
	{
		HSegment s = new HSegment(document(), HSegmentType.HEADING1, text);
		return append(s);
	}
	
	
	public HAbstractBlock heading2(String text)
	{
		HSegment s = new HSegment(document(), HSegmentType.HEADING2, text);
		return append(s);
	}
	
	
	public HAbstractBlock heading3(String text)
	{
		HSegment s = new HSegment(document(), HSegmentType.HEADING3, text);
		return append(s);
	}
	
	
	public HAbstractBlock nl()
	{
		return append(new HSegment(document(), HSegmentType.NEWLINE));
	}
	
	
	public HAbstractBlock image(BufferedImage im)
	{
		return append(new HSegmentImage(document(), im));
	}
	
	
	public HAbstractBlock text(Object style, String text)
	{
		HStyle s = document().getStyle(style);
		HSegment seg = new HSegment(document(), HSegmentType.TEXT, s, text); 
		return append(seg);
	}
	
	
	public HAbstractBlock text(String text)
	{
		HSegment seg = new HSegment(document(), HSegmentType.TEXT, text); 
		return append(seg);
	}
	
	
	protected HAbstractBlock append(HAbstractSegment s)
	{
		segments.add(s);
		document().setLastSegment(s);
		return this;
	}
	
	
	public HTable newTable()
	{
		HTable t = new HTable(document());
		append(t);
		return t;
	}
	
	
	public HTable getLastTable()
	{
		return document().getLastTable();
	}
	
	
	public HAbstractSegment getLast()
	{
		return document().getLast();
	}
}
