// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package research.notebook;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import research.notebook.js.ScriptBody;


public class CodeSections
{
	protected final NotebookPanel parent;
	private final CList<CodeSection> sections = new CList();
	
	
	public CodeSections(NotebookPanel p)
	{
		this.parent = p;
	}


	public void addSection(JTextArea t, JLabel left, JLabel right, JTextArea r)
	{
		CodeSection s = new CodeSection();
		s.textField = t;
		s.left = left;
		s.right = right;
		s.resultField = r;
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
	
	
	protected ScriptBody newProcess(CodeSection s)
	{
		if(s.process != null)
		{
			s.process.cancel();
		}
		
		String script = s.textField.getText();
		ScriptBody p = new ScriptBody(s, script); 
		s.process = p;
		return p;
	}
	
	
	protected void start(CodeSection s)
	{
		s.right.setText("*");
	}
	
	
	protected void stopped(ScriptBody p, CodeSection s)
	{
		if(s.process == p)
		{
			s.right.setText("=");

			s.resultField.setText(s.result.toString());
			s.resultField.setCaretPosition(0);
		}
	}
	
	
	protected void error(CodeSection s, Throwable e)
	{
		// TODO
		Log.err(e);
	}
	
	
	public void runSection(int ix)
	{
		final CodeSection s = sections.get(ix);
		final ScriptBody p = newProcess(s);
		
		start(s);
		
		new BackgroundThread("script")
		{
			public void process() throws Throwable
			{
				// TODO result
				p.process();
			}
			
			public void success()
			{
				// TODO result
				stopped(p, s);
			}
			
			public void onError(Throwable e)
			{
				error(s, e);
			}
		}.start();
	}
}
