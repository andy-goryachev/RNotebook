// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.util.CList;
import goryachev.notebook.Accelerators;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import goryachev.notebook.Styles;
import goryachev.notebook.js.JsError;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;


public class CodePanel
	extends CellPanel
{
	public final JTextArea textField;
	public final JLabel inField;
	public final JLabel marginField;
	private static CBorder BORDER = new CBorder(2, 4);
	private CList<Object> results;
	private CList<JComponent> resultComponents;
	
	
	public CodePanel(String text, CList<Object> results)
	{
		textField = new JTextArea(text);
		textField.setFont(Theme.monospacedFont());
		textField.setBackground(Styles.codeColor);
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		textField.addMouseListener(handler);
		setTop(textField);
		
		inField = new JLabel("In (*):");
		inField.setBorder(BORDER);
		inField.setForeground(Styles.marginTextColor);
		inField.setHorizontalAlignment(JLabel.RIGHT);
		inField.addMouseListener(handler);
		setLeft(inField);
		
		marginField = new JLabel(">");
		marginField.setBorder(BORDER);
		marginField.setForeground(Styles.marginTextColor);
		marginField.addMouseListener(handler);
		setRight(marginField);
		
		if(results != null)
		{
			setResult("*", results);
		}
	}
	
	
	public CellType getType()
	{
		return CellType.CODE;
	}
	
	
	public JTextComponent getEditor()
	{
		return textField;
	}
	
	
	public void saveCell(DataBook b)
	{
		CList<Object> rs = (results == null ? null : new CList(results));
		b.addCell(CellType.CODE, getText(), rs);
	}
	
	
	public String getText()
	{
		return textField.getText();
	}

	
	public void setRunning()
	{
		marginField.setText(">>>");
		
		// TODO clear results or show animation
	}
	
	
	public void setResult(Object count, CList<Object> results)
	{
		NotebookPanel np = NotebookPanel.get(this);
		
		// run count
		inField.setText("In (" + count + "):");
		
		// results
		this.results = results;
		boolean error = false;
		CList<JComponent> cs = new CList();
		for(Object rv: results)
		{
			if(rv != null)
			{
				if(rv instanceof JsError)
				{
					error = true;
				}
				
				JComponent c = Results.createViewer(this, rv);
				if(c != null)
				{
					cs.add(c);
				}
			}
		}
		
		// margin
		marginField.setText(error ? "ERROR" : "=");
		
		// components
		if(resultComponents != null)
		{
			for(JComponent c: resultComponents)
			{
				remove(c);
			}
		}
		
		resultComponents = cs;
		
		for(JComponent c: cs)
		{
			add(c);
		}
		
		UI.validateAndRepaint(this);
		
		if(np != null)
		{
			np.updateActions();
		}
	}
	
	
	protected JPopupMenu createPopupMenu(Component c, JPopupMenu m)
	{
		boolean run = true;
		boolean text = false;
		boolean img = false;
		boolean copy = false;
		
		if(c == textField)
		{
			text = true;
		}
		
		if(c instanceof JsImageViewer)
		{
			img = true;
			run = false;
		}
		
		NotebookPanel np = NotebookPanel.get(c);
		
		if(run)
		{
			m.add(new CMenuItem("Run", Accelerators.RUN_CELL, np.runCellAction));
			m.add(new CMenuItem("Run in Place", Accelerators.RUN_IN_PLACE, np.runInPlaceAction));
			m.add(new CMenuItem("Run All", Accelerators.RUN_ALL, np.runAllAction));
		}
		
		if(text)
		{
			UI.separator(m);
			m.add(new CMenuItem("Cut", CAction.TODO));
			m.add(new CMenuItem("Copy", CAction.TODO));
			m.add(new CMenuItem("Paste", CAction.TODO));
		}
		
		if(img)
		{
			UI.separator(m);
			m.add(new CMenuItem("Copy", CAction.TODO));
			m.addSeparator();
			m.add(new CMenuItem("Save Image", CAction.TODO));
		}
		
		return m;
	}
}
