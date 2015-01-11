// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.notebook.Accelerators;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import goryachev.notebook.Styles;
import java.awt.Component;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;


public class TextPanel
	extends CellPanel
{
	public final JTextArea textField;
	
	
	public TextPanel(NotebookPanel np, String text)
	{
		super(np);
		
		textField = new JTextArea(text);
		textField.setFont(Theme.plainFont());
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		textField.addMouseListener(handler);
		textField.setForeground(Styles.textColor);
		//
		UI.whenFocused(textField, Accelerators.TO_CODE.getKeyStroke(), np.toCodeAction);
		UI.whenFocused(textField, Accelerators.TO_H1.getKeyStroke(), np.toH1Action);
		UI.whenFocused(textField, Accelerators.TO_H2.getKeyStroke(), np.toH2Action);
		UI.whenFocused(textField, Accelerators.TO_H3.getKeyStroke(), np.toH3Action);		
		UI.whenFocused(textField, Accelerators.TO_TEXT.getKeyStroke(), np.toTextAction);
		
		setEditor(textField, false);
	}
	
	
	
	public CellType getType()
	{
		return CellType.TEXT;
	}
	
	
	public JTextComponent getEditor()
	{
		return textField;
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
		addCellMenus(m, np);
		
		if(c == textField)
		{
			m.add(new CMenuItem("Cut", np.cutAction));
			m.add(new CMenuItem("Copy", np.copyAction));
			m.add(new CMenuItem("Paste", np.pasteAction));
		}

		return m;
	}
}
