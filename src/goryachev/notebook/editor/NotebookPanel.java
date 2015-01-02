// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.notebook.DataBook;
import goryachev.notebook.SectionType;
import goryachev.notebook.Styles;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.JViewport;


// FIX margin line
public class NotebookPanel
	extends CPanel
{
	public final CAction runCurrentAction = new CAction() { public void action() { actionRunCurrent(); } };
	public final CAction toCodeAction = new CAction() { public void action() { actionToCode(); } };
	public final CAction toTextAction = new CAction() { public void action() { actionToText(); } };
	public final CAction toH1Action = new CAction() { public void action() { actionToH1(); } };
	public final CComboBox typeField;

	protected final JPanel panel;
	protected final CScrollPane scroll;
	private static PropertyChangeListener focusListener;
	private SectionPanel activeSection;
	
	
	public NotebookPanel()
	{
		panel = new SectionContainer();
		panel.setBackground(Theme.textBG());
		
		scroll = new CScrollPane(panel)
		{
//			public void paint(Graphics g)
//			{
//				super.paint(g);
//				
//				int x = getWidth() - SectionLayout.getRightMargin();
//				g.setColor(Styles.marginLineColor);
//				g.drawLine(x, 0, x, getHeight());
//			}
			
//			protected JViewport createViewport()
//			{
//				return new JViewport()
//				{
//					public void paintComponent(Graphics g)
//					{
//						super.paintComponent(g);
//						
//						int x = getWidth() - SectionLayout.getRightMargin();
//						g.setColor(Styles.marginLineColor);
//						g.drawLine(x, 0, x, getHeight());
//					}
//				};
//			}
		};
		scroll.setBackground2(Theme.textBG());
		
		setCenter(scroll);
		
		setBackground(Theme.textBG());
		
		typeField = new CComboBox(new Object[]
		{
			"Code",
			//"Markdown",
			"Raw Text",
			"Heading 1",
			//"Heading 2",
			//"Heading 3",
			//"Heading 4",
			//"Heading 5",
			//"Heading 6",
		});
		typeField.setEnabled(false);
		
		initStaticListener();
		
		UI.whenInFocusedWindow(this, KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK, runCurrentAction);
	}
	
	
	// or move this functionality to section panel mouse handler?
	private void initStaticListener()
	{
		if(focusListener == null)
		{
			focusListener = new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent ev)
				{
					Object x = ev.getNewValue();
					SectionPanel p = SectionPanel.findParent(x);
					if(p != null)
					{
						NotebookPanel np = get(p); 
						if(np != null)
						{
							np.setActiveSection(p);
						}
					}
				}
			};
			
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", focusListener);
		}
	}
	
	
	public static NotebookPanel get(Component c)
	{
		return UI.getAncestorOfClass(NotebookPanel.class, c);
	}
	
	
	protected void setActiveSection(SectionPanel p)
	{
		if(p != activeSection)
		{
			if(activeSection != null)
			{
				activeSection.setActive(false);
			}
			
			activeSection = p;
			activeSection.setActive(true);
			
			updateActions();
		}
	}
	
	
	public void setDataBook(DataBook b)
	{
		panel.removeAll();
		
		if(b != null)
		{
			int sz = b.size();
			for(int i=0; i<sz; i++)
			{
				SectionType type = b.getType(i);
				String text = b.getText(i);
				
				SectionPanel p = createSection(type, text);
				p.initialize(this);
				panel.add(p);
			}
		}
		
		validate();
		repaint();
		
		updateActions();
	}
	
	
	public DataBook getDataBook()
	{
		DataBook b = new DataBook();
		
		int sz = panel.getComponentCount();
		for(int i=0; i<sz; i++)
		{
			Component c = panel.getComponent(i);
			if(c instanceof SectionPanel)
			{
				((SectionPanel)c).saveSection(b);
			}
		}
		return b;
	}
	
	
	protected SectionPanel createSection(SectionType type, String text)
	{
		switch(type)
		{
		case CODE:
			return new CodePanel(text);
		case H1:
		case H2:
		case H3:
			return new HeaderPanel(text);
		case TEXT:
		default:
			return new TextPanel(text);
		}
	}
	
	
	protected void updateActions()
	{		
		CodePanel cp = getCodePanel();
		boolean sec = (activeSection != null);
		
		runCurrentAction.setEnabled((cp != null) && (!cp.isRunning()));
		toCodeAction.setEnabled(sec && !(activeSection instanceof CodePanel));
		toH1Action.setEnabled(sec && !(activeSection instanceof HeaderPanel));
		toTextAction.setEnabled(sec && !(activeSection instanceof TextPanel));
	}
	
	
	public CodePanel getCodePanel()
	{
		if(activeSection instanceof CodePanel)
		{
			return (CodePanel)activeSection;
		}
		return null;
	}
	
	
	protected int indexOf(SectionPanel p)
	{
		int sz = panel.getComponentCount();
		for(int i=0; i<sz; i++)
		{
			Component c = panel.getComponent(i);
			if(c == p)
			{
				return i;
			}
		}
		return -1;
	}
	
	
	protected void replace(SectionPanel old, SectionPanel p)
	{
		int ix = indexOf(old);
		if(ix >= 0)
		{
			boolean focus = (old == activeSection);
			
			panel.remove(old);
			panel.add(p, null, ix);
			
			if(focus)
			{
				setActiveSection(p);
			}
			
			UI.validateAndRepaint(this);			
		}
	}
	
	
	protected void actionRunCurrent()
	{
		CodePanel p = getCodePanel();
		if(p != null)
		{
			if(!p.isRunning())
			{
				p.runScript();
			}
		}
	}
	
	
	protected void actionToCode()
	{
		String text = activeSection.getText();
		replace(activeSection, createSection(SectionType.CODE, text));
	}
	
	
	protected void actionToH1()
	{
		String text = activeSection.getText();
		replace(activeSection, createSection(SectionType.H1, text));
	}
	
	
	protected void actionToText()
	{
		String text = activeSection.getText();
		replace(activeSection, createSection(SectionType.TEXT, text));
	}
}
