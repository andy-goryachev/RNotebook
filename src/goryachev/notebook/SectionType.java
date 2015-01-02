// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.util.TXT;


public enum SectionType 
{
	CODE(TXT.get("SectionType.code", "Code")),
	H1(TXT.get("SectionType.heading 1", "Heading 1")), 
	H2(TXT.get("SectionType.heading 2", "Heading 2")), 
	H3(TXT.get("SectionType.heading 3", "Heading 3")),
	TEXT(TXT.get("SectionType.text", "Text"));
	
	//
	
	private String name;
	
	
	SectionType(String name)
	{
		this.name = name;
	}
	
	
	public String toString()
	{
		return name;
	}
}