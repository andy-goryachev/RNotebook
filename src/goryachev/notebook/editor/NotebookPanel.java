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
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;


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
	private int indent = 100;
	private int margin = 75;
	private static PropertyChangeListener focusListener;
	private SectionPanel currentSection;
	
	
	public NotebookPanel()
	{
		panel = new SectionContainer();
		panel.setBackground(Theme.textBG());
		
		scroll = new CScrollPane(panel);
		scroll.setBackground2(Theme.textBG());
		
		setCenter(scroll);
		
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
		if(p != currentSection)
		{
			if(currentSection != null)
			{
				currentSection.setActive(false);
			}
			
			currentSection = p;
			currentSection.setActive(true);
			
			updateActions();
		}
	}
	
	
	public void setDataBook(DataBook b)
	{
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
		boolean sec = (currentSection != null);
		
		runCurrentAction.setEnabled((cp != null) && (!cp.isRunning()));
		toCodeAction.setEnabled(sec && !(currentSection instanceof CodePanel));
		toH1Action.setEnabled(sec && !(currentSection instanceof HeaderPanel));
		toTextAction.setEnabled(sec && !(currentSection instanceof TextPanel));
	}
	
	
	public CodePanel getCodePanel()
	{
		if(currentSection instanceof CodePanel)
		{
			return (CodePanel)currentSection;
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
			panel.remove(old);
			panel.add(p, null, ix);
			
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
		String text = currentSection.getText();
		replace(currentSection, createSection(SectionType.CODE, text));
	}
	
	
	protected void actionToH1()
	{
		String text = currentSection.getText();
		replace(currentSection, createSection(SectionType.H1, text));
	}
	
	
	protected void actionToText()
	{
		String text = currentSection.getText();
		replace(currentSection, createSection(SectionType.TEXT, text));
	}
}
