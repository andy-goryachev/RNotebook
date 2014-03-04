// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package research.notebook;
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
}