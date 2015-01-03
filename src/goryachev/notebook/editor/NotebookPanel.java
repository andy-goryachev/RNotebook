// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.InputTracker;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.util.D;
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
	public final CAction runAllAction = new CAction() { public void action() { actionRunAll(); } };
	public final CAction runCellAction = new CAction() { public void action() { actionRunCell(); } };
	public final CAction runInPlaceAction = new CAction() { public void action() { actionRunInPlace(); } };
	public final CAction selectNextCellAction = new CAction() { public void action() { actionSelect(1); } };
	public final CAction selectPreviousCellAction = new CAction() { public void action() { actionSelect(-1); } };
	public final CAction toCodeAction = new CAction() { public void action() { actionSwitchType(SectionType.CODE); } };
	public final CAction toTextAction = new CAction() { public void action() { actionSwitchType(SectionType.TEXT); } };
	public final CAction toH1Action = new CAction() { public void action() { actionSwitchType(SectionType.H1); } };
	
	public final CComboBox typeField;
	public final InputTracker typeFieldTracker;
	public final JPanel panel;
	public final SectionScrollPane scroll;
	private static PropertyChangeListener focusListener;
	protected static boolean suppressFocusListener;
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
		
		UI.whenInFocusedWindow(this, KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK, runInPlaceAction);
	}
	

	// FIX wrong, need mouse listener handler!
	// or move this functionality to section panel mouse handler?
	private void initStaticListener()
	{
		if(focusListener == null)
		{
			focusListener = new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent ev)
				{
					if(!suppressFocusListener)
					{
						Object x = ev.getNewValue();
						if(x != null)
						{
							SectionPanel p = SectionPanel.findParent(x);
							if(p != null)
							{
								NotebookPanel np = get(p); 
								if(np != null)
								{
									D.print(ev, p); // FIX
									np.setActiveSection(p);
								}
							}
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
			D.print(p); // FIX
			suppressFocusListener = true;
			
			if(activeSection != null)
			{
				activeSection.setActive(false);
			}
			
			activeSection = p;
			activeSection.setActive(true);
			
			//activeSection.getEditor().requestFocusInWindow();
			
			suppressFocusListener = false;
			
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
				
				if(i == 0)
				{
					setActiveSection(p);
				}
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
		boolean run = (cp != null) && (!cp.isRunning());
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
		runAllAction.setEnabled(false); // FIX
		runCellAction.setEnabled(run);
		runInPlaceAction.setEnabled(run);
		selectNextCellAction.setEnabled(sec);
		selectPreviousCellAction.setEnabled(sec);
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
	
	
	/** if current non-null cell is the last */
	public boolean isLast()
	{
		int ix = indexOf(activeSection);
		if(ix >= 0)
		{
			if(ix == (getSectionCount() - 1))
			{
				return true;
			}
		}
		return false;
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


	protected void actionRunInPlace()
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
	
	
	protected void actionRunAll()
	{
		// TODO
	}
	
	
	// moves to next cell (or creates empty code cell if last)
	protected void actionRunCell()
	{
		actionRunInPlace();
		
		if(isLast())
		{
			actionInsertCell(false);
		}
		else
		{
			actionSelect(1);
		}
	}
	
	
	protected void actionSelect(int delta)
	{
		int ix = indexOf(activeSection);
		if(ix >= 0)
		{
			D.print(ix); // FIX

			ix += delta;
			if(ix < 0)
			{
				ix = 0;
			}
			else if(ix >= getSectionCount())
			{
				ix = getSectionCount() - 1;
			}
			
			SectionPanel p = getSectionAt(ix);
			suppressFocusListener = true;
			setActiveSection(p);
			suppressFocusListener = false;
			
			p.getEditor().requestFocusInWindow();
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
