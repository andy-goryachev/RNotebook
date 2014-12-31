// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.CPanel;
import javax.swing.JTextArea;


public class CodePanel
	extends CPanel
{
	public final JTextArea textField;
	
	
	public CodePanel(String text)
	{
		textField = new JTextArea(text);
		setNorth(textField);
	}
}
