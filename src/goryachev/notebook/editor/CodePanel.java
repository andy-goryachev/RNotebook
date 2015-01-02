// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import goryachev.notebook.Styles;
import goryachev.notebook.js.ScriptBody;
import goryachev.notebook.js.ScriptLogger;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;


public class CodePanel
	extends SectionPanel
	implements ScriptLogger
{
	public final JTextArea textField;
	public final JLabel inField;
	public final JLabel marginField;
	private final JTextArea resultField;
	private final SB result;
	private static CBorder BORDER = new CBorder(2, 4);
	private transient ScriptBody script;
	private transient JComponent resultComponent;
	
	
	public CodePanel(String text)
	{
		result = new SB();
		
		textField = new JTextArea(text);
		textField.setBackground(Styles.codeColor);
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		setTop(textField);
		
		inField = new JLabel("In (*):");
		inField.setBorder(BORDER);
		inField.setForeground(Styles.marginTextColor);
		inField.setHorizontalAlignment(JLabel.RIGHT);
		setLeft(inField);
		
		marginField = new JLabel(">");
		marginField.setBorder(BORDER);
		marginField.setForeground(Styles.marginTextColor);
		setRight(marginField);
		
		resultField = new JTextArea();
		resultField.setFont(Theme.monospacedFont());
		resultField.setForeground(Styles.resultColor);
		resultField.setLineWrap(true);
		resultField.setWrapStyleWord(true);
		resultField.setEditable(false);
	}
	
	
	public void initialize(NotebookPanel np)
	{
		// TODO setup popup menus
	}
	
	
	public String getText()
	{
		return textField.getText();
	}
	
	
	public boolean isRunning()
	{
		return script != null;
	}
	
	
	protected ScriptBody newProcess()
	{
		String script = textField.getText();
		ScriptBody p = new ScriptBody(this, script); 
		return p;
	}
	
	
	public synchronized void print(String s)
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
	
	
	protected void error(ScriptBody p, Throwable e)
	{
		if(script == p)
		{
			marginField.setText("ERR");
			
			resultField.setText(CKit.stackTrace(e));
			resultField.setForeground(Styles.errorColor);
			resultField.setCaretPosition(0);
			
			setResult(resultField);
		}
	}
	
	
	protected void finished(ScriptBody p, Object rv)
	{
		if(script == p)
		{
			marginField.setText("=");
			
			// TODO return result may be anything (chart? table?)
			// decide how to process it
			if(result.isNotEmpty())
			{
				result.nl();
			}
			result.a(rv);

			resultField.setText(result.getAndClear());
			resultField.setForeground(Styles.resultColor);
			setResult(resultField);
		}
	}
	
	
	protected void setResult(JComponent c)
	{
		if(resultComponent != null)
		{
			remove(resultComponent);
		}
		
		script = null;
		
		resultComponent = c;
		add(resultComponent);
		UI.validateAndRepaint(this);
		
		NotebookPanel np = NotebookPanel.get(this);
		if(np != null)
		{
			np.updateActions();
		}
	}
	
	
	protected synchronized void start()
	{
		result.clear();
		marginField.setText("*");
	}
	
	
	public void runScript()
	{
		if(script == null)
		{
			final ScriptBody p = newProcess();
			script = p;
			
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
}
