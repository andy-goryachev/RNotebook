// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package research.notebook;
import goryachev.common.util.CList;
import goryachev.common.util.Obj;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;


public class CodeSections
{
	protected final NotebookPanel parent;
	private final CList<CodeSection> sections = new CList();
	private final static Obj KEY_SECTION = new Obj("section");
	
	
	public CodeSections(NotebookPanel p)
	{
		this.parent = p;
	}


	public void addSection(JTextArea t, JLabel left, JLabel right, JTextArea r)
	{
		CodeSection s = new CodeSection();
		s.textField = t;
		t.putClientProperty(KEY_SECTION, s);
		s.left = left;
		s.right = right;
		s.resultField = r;
		r.putClientProperty(KEY_SECTION, s);
		
		sections.add(s);
	}
	
	
	public static CodeSection get(JComponent c)
	{
		if(c != null)
		{
			Object v = c.getClientProperty(KEY_SECTION);
			if(v instanceof CodeSection)
			{
				return (CodeSection)v;
			}
		}
		return null;
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
			CodeSection s = sections.get(ix);
			s.runSection();
			ix++;
		}
	}
}
