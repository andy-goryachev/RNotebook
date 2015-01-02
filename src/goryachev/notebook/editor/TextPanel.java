// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.Theme;
import goryachev.notebook.DataBook;
import goryachev.notebook.SectionType;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;


public class TextPanel
	extends SectionPanel
{
	public final JTextArea textField;
	
	
	public TextPanel(String text)
	{
		textField = new JTextArea(text);
		textField.setFont(Theme.plainFont());
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		
		setTop(textField);
	}
	
	
	
	public SectionType getType()
	{
		return SectionType.TEXT;
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
		b.addSection(SectionType.TEXT, getText());
	}
	

	public String getText()
	{
		return textField.getText();
	}
}
