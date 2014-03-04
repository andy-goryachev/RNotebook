// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package research.notebook;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import javax.swing.JLabel;
import javax.swing.JTextArea;


public class CodeSections
{
	public static class Section
	{
		public JTextArea textField;
		public JLabel left;
		public JLabel right;
		private String text;
		private SB result;
	}
	
	//
	
	protected final NotebookPanel parent;
	private final CList<Section> sections = new CList();
	
	
	public CodeSections(NotebookPanel p)
	{
		this.parent = p;
	}


	public void addSection(JTextArea t, JLabel left, JLabel right)
	{
		Section s = new Section();
		s.textField = t;
		s.left = left;
		s.right = right;
		sections.add(s);
	}
	
	
	public int size()
	{
		return sections.size();
	}


	public void runAll()
	{
		int ix = 0;
		
		while(ix < size())
		{
			runSection(ix);
			ix++;
		}
	}
	
	
	public void runSection(int ix)
	{
		
	}
}
