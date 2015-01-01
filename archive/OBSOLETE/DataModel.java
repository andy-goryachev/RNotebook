// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.OBSOLETE;
import goryachev.common.util.CList;
import goryachev.notebook.SectionType;
import javax.swing.JComponent;


/** keeps track of UI elements, processing threads and coordinates */
// TODO is this needed?
public class DataModel
{
	protected class Section
	{
		public SectionType type;
		public String text;
		public JComponent left;
		public JComponent center;
		public JComponent right;
		public JComponent result;
		
		
		public Section(SectionType type, String text)
		{
			this.type = type;
			this.text = text;
		}
	}
	
	//
	
	private CList<Section> sections = new CList();
	
	
	public DataModel()
	{
	}
	
	
	public void addSection(SectionType t, String text)
	{
		sections.add(new Section(t, text));
	}
	
	
	public int size()
	{
		return sections.size();
	}
}
