// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.InputTracker;
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
	public final CAction deleteCellAction = new CAction() { public void action() { actionDeleteCell(); } };
	public final CAction insertCellAboveAction = new CAction() { public void action() { actionInsertCell(true); } };
	public final CAction insertCellBelowAction = new CAction() { public void action() { actionInsertCell(false); } };
	public final CAction runCurrentAction = new CAction() { public void action() { actionRunCurrent(); } };
	public final CAction toCodeAction = new CAction() { public void action() { actionSwitchType(SectionType.CODE); } };
	public final CAction toTextAction = new CAction() { public void action() { actionSwitchType(SectionType.TEXT); } };
	public final CAction toH1Action = new CAction() { public void action() { actionSwitchType(SectionType.H1); } };
	
	public final CComboBox typeField;
	public final InputTracker typeFieldTracker;
	public final JPanel panel;
	public final SectionScrollPane scroll;
	private static PropertyChangeListener focusListener;
	private SectionPanel activeSection;
	
	
	public NotebookPanel()
	{
		panel = new SectionContainer();
		
		scroll = new SectionScrollPane(panel);
		
		setCenter(scroll);
		
		setBackground(Theme.textBG());
		
		typeField = new CComboBox(new Object[]
		{
			SectionType.CODE,
			SectionType.H1,
			SectionType.TEXT,
		});
		typeField.setSelectedItem(SectionType.TEXT);
		typeFieldTracker = new InputTracker(typeField)
		{
			public void onInputEvent()
			{
				actionSwitchType((SectionType)typeField.getSelectedItem());
			}
		};
		
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
				
				SectionPanel p = SectionPanel.create(type, text);
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
		
		int sz = getSectionCount();
		for(int i=0; i<sz; i++)
		{
			SectionPanel p = getSectionAt(i);
			p.saveSection(b);
		}
		return b;
	}
	

	protected void updateActions()
	{
		CodePanel cp = getCodePanel();
		boolean sec = (activeSection != null);
		SectionType t = getSectionType();
		
		// update type pulldown
		if(t != null)
		{
			if(t != typeField.getSelectedItem())
			{
				typeFieldTracker.setEnabled(false);
				typeField.setSelectedItem(t);
				typeFieldTracker.setEnabled(true);
			}
		}
		
		deleteCellAction.setEnabled(sec);
		insertCellAboveAction.setEnabled(sec);
		runCurrentAction.setEnabled((cp != null) && (!cp.isRunning()));
		toCodeAction.setEnabled(sec && (t != SectionType.CODE));
		toH1Action.setEnabled(sec && (t != SectionType.H1));
		toTextAction.setEnabled(sec && (t != SectionType.TEXT));
	}
	
	
	public CodePanel getCodePanel()
	{
		if(activeSection instanceof CodePanel)
		{
			return (CodePanel)activeSection;
		}
		return null;
	}
	
	
	public SectionType getSectionType()
	{
		if(activeSection != null)
		{
			return activeSection.getType();
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
	
	
	protected int getSectionCount()
	{
		return panel.getComponentCount();
	}
	
	
	protected SectionPanel getSectionAt(int ix)
	{
		if(ix >= 0)
		{
			if(ix < panel.getComponentCount())
			{
				return (SectionPanel)panel.getComponent(ix);
			}
		}
		return null;
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


	protected void insert(int ix, SectionPanel p)
	{
		panel.add(p, ix);
		setActiveSection(p);
		UI.validateAndRepaint(this);
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
	
	
	protected void actionSwitchType(SectionType t)
	{
		if(t != null)
		{
			String text = activeSection.getText();
			replace(activeSection, SectionPanel.create(t, text));
		}
	}
	
	
	protected void actionInsertCell(boolean above)
	{
		SectionPanel p = SectionPanel.create(getSectionType(), null);
			
		int ix = indexOf(activeSection);
		if(ix < 0)
		{
			ix = getSectionCount();
		}
		else
		{
			if(!above)
			{
				ix++;
			}
		}
		
		insert(ix, p);
		p.focusLater();
	}
	
	
	protected void actionDeleteCell()
	{
		int ix = indexOf(activeSection);
		if(ix >= 0)
		{
			panel.remove(activeSection);
			
			if(getSectionCount() > 0)
			{
				if(ix >= getSectionCount())
				{
					ix--;
				}
				
				SectionPanel p = getSectionAt(ix);
				setActiveSection(p);	
			}
			
			UI.validateAndRepaint(this);
		}
	}
}
