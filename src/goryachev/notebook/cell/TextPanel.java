// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.Theme;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import java.awt.Component;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;


public class TextPanel
	extends CellPanel
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
	
	
	
	public CellType getType()
	{
		return CellType.TEXT;
	}
	
	
	public JTextComponent getEditor()
	{
		return textField;
	}
	

	protected void initialize(NotebookPanel np)
	{
		textField.addMouseListener(handler);
	}
	
	
	public void saveCell(DataBook b)
	{
		b.addCell(CellType.TEXT, getText());
	}
	

	public String getText()
	{
		return textField.getText();
	}
	
	
	protected JPopupMenu createPopupMenu(Component c, JPopupMenu m)
	{
		if(c == textField)
		{
			m.add(new CMenuItem("Cut", CAction.DISABLED));
			m.add(new CMenuItem("Copy", CAction.DISABLED));
			m.add(new CMenuItem("Paste", CAction.DISABLED));
		}
		else
		{
			m.add("text");
		}
		return m;
	}
}
