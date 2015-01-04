// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import java.awt.Component;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;


public class HeaderPanel
	extends CellPanel
{
	public final CellType type;
	public final JTextArea textField;
	
	
	public HeaderPanel(String text, CellType t)
	{
		this.type = t;
		
		int level;
		switch(t)
		{
		case H3:
			level = 3;
			break;
		case H2:
			level = 2;
			break;
		default:
			level = 1;
		}
		
		int indent = 5 * level;
		float scale = 1.8f - (level - 1) * 0.2f;
		
		textField = new JTextArea(text);
		textField.setFont(UI.deriveFont(Theme.plainFont(), true, scale));
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		textField.setOpaque(false);
		textField.addMouseListener(handler);
		textField.setBorder(new CBorder(0, indent, 0, 0));
		
		setCenter(textField);
	}
	
	
	public CellType getType()
	{
		return type;
	}
	
	
	public JTextComponent getEditor()
	{
		return textField;
	}

	
	public void saveCell(DataBook b)
	{
		b.addCell(type, getText());
	}
	
	
	public String getText()
	{
		return textField.getText();
	}
	
	
	protected JPopupMenu createPopupMenu(Component c, JPopupMenu m)
	{
		if(c == textField)
		{
			m.add(new CMenuItem("Cut", CAction.TODO));
			m.add(new CMenuItem("Copy", CAction.TODO));
			m.add(new CMenuItem("Paste", CAction.TODO));
		}
		else
		{
			m.add("header");
		}
		return m;
	}
}
