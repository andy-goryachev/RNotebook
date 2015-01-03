// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.util.CKit;
import goryachev.notebook.Accelerators;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import goryachev.notebook.Styles;
import goryachev.notebook.js.img.JsImage;
import java.awt.Component;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
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
	private final JTextArea resultField;
	private static CBorder BORDER = new CBorder(2, 4);
	private transient JComponent resultComponent;
	
	
	public CodePanel(String text)
	{
		textField = new JTextArea(text);
		textField.setFont(Theme.monospacedFont());
		textField.setBackground(Styles.codeColor);
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		setTop(textField);
		
		inField = new JLabel("In (*):");
		inField.setBorder(BORDER);
		inField.setForeground(Styles.marginTextColor);
		inField.setHorizontalAlignment(JLabel.RIGHT);
		setLeft(inField);
		
		marginField = new JLabel(">");
		marginField.setBorder(BORDER);
		marginField.setForeground(Styles.marginTextColor);
		setRight(marginField);
		
		resultField = new JTextArea();
		resultField.setFont(Theme.monospacedFont());
		resultField.setForeground(Styles.resultColor);
		resultField.setLineWrap(true);
		resultField.setWrapStyleWord(true);
		resultField.setEditable(false);
	}
	
	
	public CellType getType()
	{
		return CellType.CODE;
	}
	
	
	public JTextComponent getEditor()
	{
		return textField;
	}
	
	
	protected void initialize(NotebookPanel np)
	{
		textField.addMouseListener(handler);
		inField.addMouseListener(handler);
		marginField.addMouseListener(handler);
		resultField.addMouseListener(handler);
	}
	
	
	public void saveCell(DataBook b)
	{
		b.addCell(CellType.CODE, getText());
		// TODO result
	}
	
	
	public String getText()
	{
		return textField.getText();
	}

	
	public void setRunning()
	{
		marginField.setText("*");
	}
	
	
	// TODO multiple results of different kind + interspersed with logged output
	public void setResult(int count, Object rv, Throwable err, String logged)
	{
		inField.setText("In (" + count + "):");
		
		if(err == null)
		{
			marginField.setText("=");
			
			if(rv != null)
			{
				// FIX multiple results
				if(CKit.isNotBlank(logged))
				{
					logged = logged + "\n" + rv;
				}
				else
				{
					logged = rv.toString();
				}
			}
			
			JComponent v = createViewer(rv, logged);
			setResultComponent(v);
		}
		else
		{
			// TODO decode error, cancelled
			marginField.setText("ERR");
			
			// FIX logged in different color?
			
			resultField.setText(CKit.stackTrace(err));
			resultField.setForeground(Styles.errorColor);
			resultField.setCaretPosition(0);
			
			setResultComponent(resultField);
		}
	}
	
	
	protected void setResultComponent(JComponent c)
	{
		if(resultComponent != null)
		{
			remove(resultComponent);
		}
		
		resultComponent = c;
		add(resultComponent);
		UI.validateAndRepaint(this);
		
		NotebookPanel np = NotebookPanel.get(this);
		if(np != null)
		{
			np.updateActions();
		}
	}
	
	
	@Deprecated
	protected JComponent createViewer(Object r, String logged)
	{
		// FIX logged
		
		if(r instanceof JsImage)
		{
			// FIX logged!
			
			BufferedImage im = ((JsImage)r).getBufferedImage();
			
			JLabel t = new JLabel();
			t.setIcon(new ImageIcon(im));
			return t;
		}
		else
		{
//			if(log.isNotEmpty())
//			{
//				log.nl();
//			}
//			log.a(r);

			resultField.setText(logged);
			resultField.setForeground(Styles.resultColor);
			return resultField;
		}
	}
	
	
	protected JPopupMenu createPopupMenu(Component c, JPopupMenu m)
	{
		NotebookPanel np = NotebookPanel.get(c);
		m.add(new CMenuItem("Run", Accelerators.RUN_CELL, np.runCellAction));
		m.add(new CMenuItem("Run in Place", Accelerators.RUN_IN_PLACE, np.runInPlaceAction));
		m.add(new CMenuItem("Run All", Accelerators.RUN_ALL, np.runAllAction));
		m.addSeparator();
		
		if(c == textField)
		{
			m.add(new CMenuItem("Cut", CAction.DISABLED));
			m.add(new CMenuItem("Copy", CAction.DISABLED));
			m.add(new CMenuItem("Paste", CAction.DISABLED));
		}
		else
		{
			m.add("code");
		}
		return m;
	}
}
