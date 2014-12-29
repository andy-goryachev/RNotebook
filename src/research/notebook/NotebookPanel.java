// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package research.notebook;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CFocusMonitor;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.Colors;
import goryachev.common.ui.Gray;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;


public class NotebookPanel
	extends CPanel
{
	public final CAction runCurrentAction = new CAction() { public void action() { actionRunCurrent(); } };

	public final CComboBox typeField;
	private CPanel panel;
	private CodeSections codes;
	private static Border BORDER_LABEL = new CBorder(4, 2, 5, 2);
	private static Border BORDER_TEXT = new CompoundBorder(new CBorder(0, 0, 0, 1, new Color(244, 128, 128)), new CBorder(2, 2, 5, 2));
	private static Dimension LEFT_SIZE = new Dimension(100, -1);
	private static Dimension RIGHT_SIZE = new Dimension(75, -1);
	protected static Color codeColor = new Gray(248);
	protected static Color resultColor = Colors.eclipseGreen;
	protected static Color errorColor = Color.red;
	
	
	public NotebookPanel()
	{
		setBackground(Theme.textBG());
		
		typeField = new CComboBox(new Object[]
		{
			"Code",
			"Markdown",
			"Raw Text",
			"Heading 1",
			"Heading 2",
			"Heading 3",
			"Heading 4",
			"Heading 5",
			"Heading 6",
		});
	}
	
	
	public void setData(DataBook b)
	{
		panel = null;
		setCenter(null);
		
		codes = new CodeSections(this);
		
		if(b != null)
		{
			int sz = b.size();
			for(int i=0; i<sz; i++)
			{
				SectionType type = b.getType(i);
				String text = b.getText(i);
				
				addSection(type, text);
			}
		}
		
		validate();
		repaint();
		
		codes.runAll();
	}


	protected void addSection(SectionType type, String text)
	{
		if(panel == null)
		{
			panel = new CPanel();
			panel.setScrollableTracksViewportWidth(true);
			panel.setOpaque(false);
			panel.setLayout
			(
				new double[]
				{
					PREFERRED, FILL, PREFERRED
				},
				new double[]
				{
					1
				}
			);
			
			setCenter(new CScrollPane(panel, false));
			
			panel.add(0, 0, new Spacer(60));
			panel.add(2, 0, new Spacer(50));
		}
		
		int row = panel.getTableLayoutRowCount();
		panel.getTableLayout().insertRow(row, CPanel.PREFERRED);
		
		switch(type)
		{
		case CODE:
			JLabel left = labelLeft("In (" + row + "):");
			JLabel right = labelRight(" ");
			JTextArea t = code(text);
			JTextArea r = result(null);
			codes.addSection(t, left, right, r);
			
			panel.add(0, row, left);
			panel.add(1, row, t);
			panel.add(2, row, right);
			
			row++;
			panel.getTableLayout().insertRow(row, CPanel.PREFERRED);
			
			panel.add(0, row, labelLeft("Out (" + row + "):"));
			panel.add(1, row, r);
			
			break;
			
		case H1:
		case H2:
		case H3:
			panel.add(0, row, 1, row, h1(text));
			break;
			
		case TEXT:
		default:
			panel.add(1, row, text2(text));
			break;
		}
	}
	

	protected JLabel labelLeft(String text)
	{
		JLabel t = new JLabel(text);
		t.setBorder(BORDER_LABEL);
		t.setMinimumSize(LEFT_SIZE);
		t.setVerticalAlignment(JLabel.TOP);
		t.setHorizontalAlignment(JLabel.RIGHT);
		t.setForeground(Color.lightGray);
		return t;
	}
	
	
	protected JLabel labelRight(String text)
	{
		JLabel t = new JLabel(text);
		t.setBorder(BORDER_LABEL);
		t.setMinimumSize(RIGHT_SIZE);
		t.setVerticalAlignment(JLabel.TOP);
		t.setForeground(Color.lightGray);
		return t;
	}
	
	
	protected JTextArea code(String text)
	{
		JTextArea t = textArea(text);
		t.setFont(Theme.monospacedFont());
		t.setBackground(codeColor);
		UI.whenFocused(t, KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK, runCurrentAction);
		return t;
	}
	
	
	protected JTextArea h1(String text)
	{
		JTextArea t = textArea(text);
		t.setFont(UI.deriveFont(Theme.plainFont(), true, 1.8f));
		return t;
	}
	
	
	protected JTextArea text2(String text)
	{
		JTextArea t = textArea(text);
		t.setFont(Theme.plainFont());
		return t;
	}
	
	
	protected static JTextArea result(String text)
	{
		JTextArea t = textArea(text);
		t.setFont(Theme.monospacedFont());
		t.setForeground(resultColor);
		t.setEditable(false);
		return t;
	}
	
	
	protected static JTextArea textArea(String text)
	{
		JTextArea t = new JTextArea(text);
		t.setWrapStyleWord(true);
		t.setLineWrap(true);
		t.setBorder(BORDER_TEXT);
		return t;
	}
	
	
	protected void actionRunCurrent()
	{
		JComponent c = CFocusMonitor.getLastTextComponent();
		CodeSection s = CodeSections.get(c);
		if(s != null)
		{
			s.runSection();
		}
	}
}
