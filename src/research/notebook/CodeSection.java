// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package research.notebook;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import research.notebook.js.ScriptBody;
import research.notebook.js.ScriptLogger;


public class CodeSection
	implements ScriptLogger
{
	public JTextArea textField;
	public JLabel left;
	public JLabel right;
	public JTextArea resultField;
	public ScriptBody process;
	public SB result = new SB();
	public int row;
	
	
	public synchronized void print(String s)
	{
		if(result.isNotEmpty())
		{
			result.nl();
		}
		result.append(s);
	}

	
	public synchronized void printSystem(String s)
	{
		if(result.isNotEmpty())
		{
			result.nl();
		}
		result.append(s);
	}

	
	public synchronized void printError(String s)
	{
		if(result.isNotEmpty())
		{
			result.nl();
		}
		result.append(s);
	}
	
	
	protected ScriptBody newProcess()
	{
		if(process != null)
		{
			process.cancel();
		}
		
		String script = textField.getText();
		ScriptBody p = new ScriptBody(this, script); 
		process = p;
		return p;
	}
	
	
	protected synchronized void start()
	{
		result.clear();
		right.setText("*");
	}
	
	
	protected void setWaiting()
	{
		right.setText("?");
	}
	
	
	protected void finished(ScriptBody p, Object rv)
	{
		if(process == p)
		{
			right.setText("=");
			
			// TODO return result may be anything (chart? table?)
			// decide how to process it
			if(result.isNotEmpty())
			{
				result.nl();
			}
			result.a(rv);

			resultField.setText(result.getAndClear());
			resultField.setForeground(NotebookPanel.resultColor);
		}
	}
	
	
	protected void error(ScriptBody p, Throwable e)
	{
		if(process == p)
		{
			right.setText("ERR");
			
			resultField.setText(CKit.stackTrace(e));
			resultField.setForeground(NotebookPanel.errorColor);
		}
	}
	
	
	public void runSection()
	{
		final ScriptBody p = newProcess();
		
		start();
		
		new BackgroundThread("script")
		{
			private Object rv;
			
			public void process() throws Throwable
			{
				rv = p.process();
			}
			
			public void success()
			{
				finished(p, rv);
			}
			
			public void onError(Throwable e)
			{
				error(p, e);
			}
		}.start();
	}
}