// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import javax.swing.JTextArea;


public class HeaderPanel
	extends CPanel
{
	public final JTextArea textField;
	
	
	public HeaderPanel(String text)
	{
		textField = new JTextArea(text);
		textField.setFont(UI.deriveFont(Theme.plainFont(), true, 1.8f));
		
		setNorth(textField);
	}
}
