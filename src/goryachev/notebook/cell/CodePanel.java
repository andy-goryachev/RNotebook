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
import goryachev.notebook.icons.NotebookIcons;
import goryachev.notebook.js.JsError;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;


public class CodePanel
	extends CellPanel
{
	public final RSyntaxTextArea textField;
	public final JLabel inField;
	public final JLabel marginField;
	private int sequence;
	private CList<Object> results;
	private CList<JComponent> resultComponents;
	private static CBorder BORDER = new CBorder(2, 4);
	
	
	public CodePanel(String text, int seq, CList<Object> results)
	{
		textField = new RSyntaxTextArea(text);
		textField.setFont(Theme.monospacedFont());
		textField.setBackground(Styles.codeColor);
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		textField.addMouseListener(handler);
		//
		textField.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		textField.setCodeFoldingEnabled(false);
		textField.setFont(Theme.monospacedFont());
		textField.setAnimateBracketMatching(false);
		textField.setHighlightCurrentLine(false);
		//
		setEditor(textField, false);
		
		inField = new JLabel();
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
		
		setResult(seq, results);
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
		b.addCell(CellType.CODE, getText(), sequence, rs);
	}
	
	
	public String getText()
	{
		return textField.getText();
	}


	public void setRunning()
	{
		setMargin("...", false);
		
		// TODO clear results or show animation
	}
	
	
	protected void setMargin(String text, boolean error)
	{
		marginField.setText(text);
		marginField.setForeground(error ? Styles.errorColor : Styles.marginTextColor);
	}
	
	
	public void setResult(int seq, CList<Object> results)
	{
		// sequence
		this.sequence = seq;
		inField.setText("In (" + (seq <= 0 ? "*" : seq) + "):");
		
		// results
		this.results = results;
		boolean error = false;
		CList<JComponent> cs = null;
		
		if(results != null)
		{
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
						if(cs == null)
						{
							cs = new CList();
						}
						cs.add(c);
					}
				}
			}
		}
		
		// margin
		setMargin(error ? "ERROR" : "=", error);
		
		// components
		if(resultComponents != null)
		{
			for(JComponent c: resultComponents)
			{
				remove(c);
			}
		}
		
		resultComponents = cs;
		
		if(cs != null)
		{
			for(JComponent c: cs)
			{
				add(c);
			}
		}
		
		UI.validateAndRepaint(this);
		
		NotebookPanel np = NotebookPanel.get(this);
		if(np != null)
		{
			np.setModified(true);
			np.updateActions();
		}
	}
	
	
	protected JPopupMenu createPopupMenu(Component c, JPopupMenu m)
	{
		boolean run = true;
		boolean text = false;
		JImageViewer imgViewer = null;
		boolean copy = false;
		
		if(c == textField)
		{
			text = true;
		}
		
		if(c instanceof JImageViewer)
		{
			imgViewer = (JImageViewer)c;
			run = false;
		}
		
		NotebookPanel np = NotebookPanel.get(c);
		
		if(run)
		{
			//m.add(new CMenuItem("Run", Accelerators.RUN_CELL, np.runCellAction));
			m.add(new CMenuItem(NotebookIcons.Start, "Run in Place", Accelerators.RUN_IN_PLACE, np.runInPlaceAction));
			//m.add(new CMenuItem("Run All", Accelerators.RUN_ALL, np.runAllAction));
			m.add(new CMenuItem("Run in Debugger", CAction.TODO));
		}
		
		addCellMenus(m, np);
		
		if(text)
		{
			UI.separator(m);
			m.add(new CMenuItem("Cut", np.cutAction));
			m.add(new CMenuItem("Copy", np.copyAction));
			m.add(new CMenuItem("Paste", np.pasteAction));
		}
		
		if(imgViewer != null)
		{
			UI.separator(m);
			m.add(new CMenuItem("Copy", np.copyAction));
			m.addSeparator();
			m.add(new CMenuItem("Save Image", imgViewer.saveImageAction));
		}
		
		return m;
	}
}
