// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.notebook.DataBook;
import goryachev.notebook.SectionType;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;


public class HeaderPanel
	extends SectionPanel
{
	public final JTextArea textField;
	
	
	public HeaderPanel(String text)
	{
		textField = new JTextArea(text);
		textField.setFont(UI.deriveFont(Theme.plainFont(), true, 1.8f));
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		textField.setOpaque(false);
		
		setCenter(textField);
	}
	
	
	public SectionType getType()
	{
		return SectionType.H1;
	}
	
	
	public JTextComponent getEditor()
	{
		return textField;
	}
	

	public void initialize(NotebookPanel np)
	{
		// TODO setup popup menus
	}
	
	
	public void saveSection(DataBook b)
	{
		b.addSection(SectionType.H1, getText());
	}
	
	
	public String getText()
	{
		return textField.getText();
	}
}
