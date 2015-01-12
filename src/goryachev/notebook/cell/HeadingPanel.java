// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.notebook.Accelerators;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import java.awt.Component;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import research.dhtml.HDocument;


public class HeadingPanel
	extends CellPanel
{
	private static int INDENT = 8;
	private static float FONT_SIZE_MAX = 1.8f;
	private static float FONT_SIZE_STEP = 0.2f;
	
	public final CellType type;
	public final JTextArea textField;
	
	
	public HeadingPanel(NotebookPanel np, String text, CellType t)
	{
		super(np);
		
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
		
		int indent = INDENT * level;
		float scale = FONT_SIZE_MAX - (level - 1) * FONT_SIZE_STEP;
		
		textField = new JTextArea(text);
		textField.setFont(UI.deriveFont(Theme.plainFont(), true, scale));
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		textField.setOpaque(false);
		textField.addMouseListener(handler);
		textField.setBorder(new CBorder(0, indent, 0, 0));
		
		UI.whenFocused(textField, Accelerators.TO_CODE.getKeyStroke(), np.toCodeAction);
		UI.whenFocused(textField, Accelerators.TO_H1.getKeyStroke(), np.toH1Action);
		UI.whenFocused(textField, Accelerators.TO_H2.getKeyStroke(), np.toH2Action);
		UI.whenFocused(textField, Accelerators.TO_H3.getKeyStroke(), np.toH3Action);		
		UI.whenFocused(textField, Accelerators.TO_TEXT.getKeyStroke(), np.toTextAction);
		
		setEditor(textField, true);
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
	
	
	public void saveCell(HDocument d)
	{
		String s = getText();
		switch(type)
		{
		case H1:
			d.heading1(s);
			break;
		case H2:
			d.heading2(s);
			break;
		case H3:
			d.heading3(s);
			break;
		}
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
