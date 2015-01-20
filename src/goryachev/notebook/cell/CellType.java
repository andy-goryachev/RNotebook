// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.util.TXT;
import goryachev.notebook.Schema;


public enum CellType 
{
	CODE(TXT.get("SectionType.code", "Code")),
	H1(TXT.get("SectionType.heading 1", "Heading 1")), 
	H2(TXT.get("SectionType.heading 2", "Heading 2")), 
	H3(TXT.get("SectionType.heading 3", "Heading 3")),
	TEXT(TXT.get("SectionType.text", "Text"));
	
	//
	
	private String name;
	
	
	CellType(String name)
	{
		this.name = name;
	}
	
	
	public String toString()
	{
		return name;
	}
	
	
	public static String toSectionCode(CellType t) throws Exception
	{
		switch(t)
		{
		case CODE:
			return Schema.CELL_TYPE_CODE;
		case H1:
			return Schema.CELL_TYPE_H1;
		case H2:
			return Schema.CELL_TYPE_H2;
		case H3:
			return Schema.CELL_TYPE_H3;
		case TEXT:
			return Schema.CELL_TYPE_TEXT;
		default:
			throw new Exception("implement: " + t);
		}
	}
	
	
	public static CellType parse(String s) throws Exception
	{
		if(Schema.CELL_TYPE_CODE.equals(s))
		{
			return CellType.CODE;
		}
		else if(Schema.CELL_TYPE_H1.equals(s))
		{
			return CellType.H1;
		}
		else if(Schema.CELL_TYPE_H2.equals(s))
		{
			return CellType.H2;
		}
		else if(Schema.CELL_TYPE_H3.equals(s))
		{
			return CellType.H3;
		}
		else if(Schema.CELL_TYPE_TEXT.equals(s))
		{
			return CellType.TEXT;
		}
		else
		{
			throw new Exception("unknown section type: " + s);
		}
	}
}