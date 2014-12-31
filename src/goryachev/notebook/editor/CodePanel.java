// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.CPanel;
import goryachev.notebook.Styles;
import javax.swing.JTextArea;


public class CodePanel
	extends SectionPanel
{
	public final JTextArea textField;
	
	
	public CodePanel(String text)
	{
		textField = new JTextArea(text);
		textField.setBackground(Styles.codeColor);
			
		setNorth(textField);
	}
}
