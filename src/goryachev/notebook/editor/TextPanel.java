// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.CPanel;
import javax.swing.JTextArea;


public class TextPanel
	extends CPanel
{
	public final JTextArea textField;
	
	
	public TextPanel(String text)
	{
		textField = new JTextArea(text);
		setNorth(textField);
	}
}
