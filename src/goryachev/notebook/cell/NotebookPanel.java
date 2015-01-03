// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.InputTracker;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.util.D;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
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
	public final CAction toCodeAction = new CAction() { public void action() { actionSwitchType(CellType.CODE); } };
	public final CAction toTextAction = new CAction() { public void action() { actionSwitchType(CellType.TEXT); } };
	public final CAction toH1Action = new CAction() { public void action() { actionSwitchType(CellType.H1); } };
	
	public final CComboBox typeField;
	public final InputTracker typeFieldTracker;
	public final JPanel panel;
	public final CellScrollPane scroll;
	private static PropertyChangeListener focusListener;
	protected static boolean suppressFocusListener;
	private CellPanel activeCell;
	
	
	public NotebookPanel()
	{
		panel = new CellContainer();
		
		scroll = new CellScrollPane(panel);
		
		setCenter(scroll);
		
		setBackground(Theme.textBG());
		
		typeField = new CComboBox(new Object[]
		{
			CellType.CODE,
			CellType.H1,
			CellType.TEXT,
		});
		typeField.setSelectedItem(CellType.TEXT);
		typeFieldTracker = new InputTracker(typeField)
		{
			public void onInputEvent()
			{
				actionSwitchType((CellType)typeField.getSelectedItem());
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
							CellPanel p = CellPanel.findParent(x);
							if(p != null)
							{
								NotebookPanel np = get(p); 
								if(np != null)
								{
									D.print(ev, p); // FIX
									np.setActiveCell(p);
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
	
	
	protected void setActiveCell(CellPanel p)
	{
		if(p != activeCell)
		{
			D.print(p); // FIX
			suppressFocusListener = true;
			
			if(activeCell != null)
			{
				activeCell.setActive(false);
			}
			
			activeCell = p;
			activeCell.setActive(true);
			
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
				CellType type = b.getType(i);
				String text = b.getText(i);
				
				CellPanel p = CellPanel.create(type, text);
				p.initialize(this);
				panel.add(p);
				
				if(i == 0)
				{
					setActiveCell(p);
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
		
		int sz = getCellCount();
		for(int i=0; i<sz; i++)
		{
			CellPanel p = getCellAt(i);
			p.saveSection(b);
		}
		return b;
	}
	

	protected void updateActions()
	{
		CodePanel cp = getCodePanel();
		boolean sec = (activeCell != null);
		boolean run = (cp != null) && (!cp.isRunning());
		CellType t = getCellType();
		
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
		toCodeAction.setEnabled(sec && (t != CellType.CODE));
		toH1Action.setEnabled(sec && (t != CellType.H1));
		toTextAction.setEnabled(sec && (t != CellType.TEXT));
	}
	
	
	public CodePanel getCodePanel()
	{
		if(activeCell instanceof CodePanel)
		{
			return (CodePanel)activeCell;
		}
		return null;
	}
	
	
	public CellType getCellType()
	{
		if(activeCell != null)
		{
			return activeCell.getType();
		}
		return null;
	}
	
	
	protected int indexOf(CellPanel p)
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
		int ix = indexOf(activeCell);
		if(ix >= 0)
		{
			if(ix == (getCellCount() - 1))
			{
				return true;
			}
		}
		return false;
	}
	
	
	protected int getCellCount()
	{
		return panel.getComponentCount();
	}
	
	
	protected CellPanel getCellAt(int ix)
	{
		if(ix >= 0)
		{
			if(ix < panel.getComponentCount())
			{
				return (CellPanel)panel.getComponent(ix);
			}
		}
		return null;
	}
	
	
	protected void replace(CellPanel old, CellPanel p)
	{
		int ix = indexOf(old);
		if(ix >= 0)
		{
			boolean focus = (old == activeCell);

			panel.remove(old);
			panel.add(p, null, ix);

			if(focus)
			{
				setActiveCell(p);
			}

			UI.validateAndRepaint(this);
		}
	}


	protected void insert(int ix, CellPanel p)
	{
		panel.add(p, ix);
		setActiveCell(p);
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
		int ix = indexOf(activeCell);
		if(ix >= 0)
		{
			D.print(ix); // FIX

			ix += delta;
			if(ix < 0)
			{
				ix = 0;
			}
			else if(ix >= getCellCount())
			{
				ix = getCellCount() - 1;
			}
			
			CellPanel p = getCellAt(ix);
			suppressFocusListener = true;
			setActiveCell(p);
			suppressFocusListener = false;
			
			p.getEditor().setCaretPosition(0);
			p.getEditor().requestFocusInWindow();
		}
	}
	
	
	protected void actionSwitchType(CellType t)
	{
		if(t != null)
		{
			String text = activeCell.getText();
			replace(activeCell, CellPanel.create(t, text));
		}
	}
	
	
	protected void actionInsertCell(boolean above)
	{
		CellPanel p = CellPanel.create(getCellType(), null);
			
		int ix = indexOf(activeCell);
		if(ix < 0)
		{
			ix = getCellCount();
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
		int ix = indexOf(activeCell);
		if(ix >= 0)
		{
			panel.remove(activeCell);
			
			if(getCellCount() > 0)
			{
				if(ix >= getCellCount())
				{
					ix--;
				}
				
				CellPanel p = getCellAt(ix);
				setActiveCell(p);	
			}
			
			UI.validateAndRepaint(this);
		}
	}
}
