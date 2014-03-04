// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package research.notebook;
import goryachev.common.util.CList;


public class DataBook
{
	public class Section
	{
		public SectionType type;
		public String text;
		
		public Section(SectionType type, String text)
		{
			this.type = type;
			this.text = text;
		}
	}
	
	//
	
	private CList<Section> sections = new CList();

	
	public DataBook()
	{
	}
	
	
	public void addSection(SectionType type, String text)
	{
		sections.add(new Section(type, text));
	}
	
	
	public int size()
	{
		return sections.size();
	}
	
	
	public SectionType getType(int ix)
	{
		return sections.get(ix).type;
	}
	
	
	public String getText(int ix)
	{
		return sections.get(ix).text;
	}
}
