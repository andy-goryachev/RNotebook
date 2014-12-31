// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.Theme;
import javax.swing.JTextArea;


public class TextPanel
	extends SectionPanel
{
	public final JTextArea textField;
	
	
	public TextPanel(String text)
	{
		textField = new JTextArea(text);
		textField.setFont(Theme.plainFont());
		
		setNorth(textField);
	}
}
