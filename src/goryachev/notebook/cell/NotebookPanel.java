// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.InputTracker;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.notebook.Accelerators;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import goryachev.notebook.NotebookWindow;
import goryachev.notebook.js.JsEngine;
import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;


public class NotebookPanel
	extends CPanel
{
	public final CAction copyAction = new CAction() { public void action() { actionCopy(); } };
	public final CAction ctrlEnterAction = new CAction() { public void action() { actionCtrlEnter(); } };
	public final CAction cutAction = new CAction() { public void action() { actionCut(); } };
	public final CAction deleteCellAction = new CAction() { public void action() { actionDeleteCell(); } };
	public final CAction insertCellAboveAction = new CAction() { public void action() { actionInsertCell(true, null); } };
	public final CAction insertCellBelowAction = new CAction() { public void action() { actionInsertCell(false, null); } };
	public final CAction interruptAction = new CAction() { public void action() { actionInterrupt(); } };
	public final CAction mergeCellAboveAction = new CAction() { public void action() { actionMergeCell(true); } };
	public final CAction mergeCellBelowAction = new CAction() { public void action() { actionMergeCell(false); } };
	public final CAction moveCellDownAction = new CAction() { public void action() { actionMoveCell(false); } };
	public final CAction moveCellUpAction = new CAction() { public void action() { actionMoveCell(true); } };
	public final CAction pasteAction = new CAction() { public void action() { actionPaste(); } };
	public final CAction restartEngineAction = new CAction() { public void action() { actionRestartEngine(); } };
	public final CAction runAllAction = new CAction() { public void action() { actionRunAll(); } };
	public final CAction runCellAction = new CAction() { public void action() { actionRunCell(); } };
	public final CAction runInPlaceAction = new CAction() { public void action() { actionRunInPlace(false); } };
	public final CAction selectNextCellAction = new CAction() { public void action() { actionSelect(1); } };
	public final CAction selectPreviousCellAction = new CAction() { public void action() { actionSelect(-1); } };
	public final CAction splitCellAction = new CAction() { public void action() { actionSplit(); } };
	public final CAction toCodeAction = new CAction() { public void action() { actionSwitchType(CellType.CODE); } };
	public final CAction toTextAction = new CAction() { public void action() { actionSwitchType(CellType.TEXT); } };
	public final CAction toH1Action = new CAction() { public void action() { actionSwitchType(CellType.H1); } };
	public final CAction toH2Action = new CAction() { public void action() { actionSwitchType(CellType.H2); } };
	public final CAction toH3Action = new CAction() { public void action() { actionSwitchType(CellType.H3); } };
	
	public final CComboBox typeField;
	public final InputTracker typeFieldTracker;
	public final JPanel panel;
	public final CellScrollPane scroll;
	private JsEngine engine;
	private CellPanel activeCell;
	private boolean modified;
	
	
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
			CellType.H2,
			CellType.H3,
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
		
		engine = new JsEngine(this);
		
		UI.whenInFocusedWindow(this, Accelerators.COMMIT.getKeyStroke(), ctrlEnterAction);
		
		updateActions();
	}
	

	public static NotebookPanel find(Component c)
	{
		return UI.getAncestorOfClass(NotebookPanel.class, c);
	}
	
	
	public boolean isModified()
	{
		return modified;
	}
	
	
	public boolean setModified(boolean on)
	{
		if(modified != on)
		{
			modified = on;
			
			NotebookWindow w = NotebookWindow.get(this);
			if(w != null)
			{
				w.updateActions();
			}
			
			return true;
		}
		return false;
	}
	
	
	protected void setActiveCell(CellPanel p)
	{
		if(p != activeCell)
		{
			if(activeCell != null)
			{
				activeCell.setActive(false);
			}
			
			activeCell = p;
			
			if(activeCell != null)
			{
				activeCell.setActive(true);	
				activeCell.getEditor().requestFocusInWindow();
				
				// FIX
				//activeCell.scrollRectToVisible(new Rectangle(0, 0, activeCell.getWidth(), activeCell.getHeight()));
			}
			
			updateActions();
		}
	}
	
	
	public void setDataBook(DataBook b)
	{
		panel.removeAll();
		
		CellPanel first = null;
		
		if(b != null)
		{
			int sequence = -1;
			int sz = b.size();
			for(int i=0; i<sz; i++)
			{
				DataBook.Cell c = b.getCell(i);
				CellPanel p = CellPanel.create(this, c.type, c.text, c.sequence, c.results);
				panel.add(p);
				
				if(first == null)
				{
					first = p;
				}
				
				if(sequence < c.sequence)
				{
					sequence = c.sequence + 1;
				}
			}
			
			engine.setSequence(sequence);
		}
		
		UI.validateAndRepaint(this);
		updateActions();
		
		if(first != null)
		{
			setActiveLater(first);
		}
	}


	protected void setActiveLater(final CellPanel c)
	{
		UI.later(new Runnable()
		{
			public void run()
			{
				setActiveCell(c);
				int w = c.getWidth();
				c.scrollRectToVisible(new Rectangle(0, 0, w, 10));
			}
		});
	}


	public DataBook getDataBook()
	{
		DataBook b = new DataBook();
		
		int sz = getCellCount();
		for(int i=0; i<sz; i++)
		{
			CellPanel p = getCellAt(i);
			p.saveCell(b);
		}
		return b;
	}
	

	public void updateActions()
	{
		CodePanel cp = getCodePanel();
		boolean cell = (activeCell != null);
		boolean code = (cp != null);
		boolean running = engine.isRunning();
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
		
		ctrlEnterAction.setEnabled(cell);
		deleteCellAction.setEnabled(cell);
		insertCellAboveAction.setEnabled(cell);
		interruptAction.setEnabled(running);
		mergeCellAboveAction.setEnabled(cell);
		mergeCellBelowAction.setEnabled(cell);
		moveCellDownAction.setEnabled(cell);
		moveCellUpAction.setEnabled(cell);
		runAllAction.setEnabled(false); // FIX
		runCellAction.setEnabled(code && !running);
		runInPlaceAction.setEnabled(code && !running);
		selectNextCellAction.setEnabled(cell);
		selectPreviousCellAction.setEnabled(cell);
		splitCellAction.setEnabled(cell);
		toCodeAction.setEnabled(cell && (t != CellType.CODE));
		toH1Action.setEnabled(cell && (t != CellType.H1));
		toH2Action.setEnabled(cell && (t != CellType.H2));
		toH3Action.setEnabled(cell && (t != CellType.H3));
		toTextAction.setEnabled(cell && (t != CellType.TEXT));
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
			setModified(true);
		}
	}


	protected void insert(int ix, CellPanel p)
	{
		panel.add(p, ix);
		setActiveCell(p);
		
		UI.validateAndRepaint(this);
		setModified(true);
	}
	
	
	protected void actionCtrlEnter()
	{
		if(activeCell instanceof CodePanel)
		{
			engine.execute((CodePanel)activeCell);
			updateActions();
		}
		else
		{
			CellType t;
			if(activeCell == null)
			{
				t = (CellType)typeField.getSelectedItem();
			}
			else
			{
				t = activeCell.getType();
				
				// change to code from heading
				switch(t)
				{
				case H1:
				case H2:
				case H3:
					t = CellType.CODE;
				}
			}
			
			actionInsertCell(false, t);
		}
	}


	protected void actionRunInPlace(boolean insert)
	{
		if(activeCell instanceof CodePanel)
		{
			engine.execute((CodePanel)activeCell);
			updateActions();
		}
		else
		{
			if(insert)
			{
				actionInsertCell(false, null);
			}
		}
		
		updateActions();
	}
	
	
	protected void actionRunAll()
	{
		// TODO
		
		updateActions();
	}
	
	
	// moves to next cell (or creates empty code cell if last)
	protected void actionRunCell()
	{
		actionRunInPlace(false);
		
		if(isLast())
		{
			actionInsertCell(false, null);
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
			setActiveCell(p);
			
			p.getEditor().setCaretPosition(0);
			p.getEditor().requestFocusInWindow();
		}
	}
	
	
	protected void actionSwitchType(CellType t)
	{
		if(t != null)
		{
			if(activeCell != null)
			{
				String text = activeCell.getText();
				CellPanel p = CellPanel.create(this, t, text, -1, null);
				
				replace(activeCell, p);
			}
		}
	}
	
	
	protected void actionInsertCell(boolean above, CellType t)
	{
		if(t == null)
		{
			t = getCellType();
			if(t == null)
			{
				t = (CellType)typeField.getSelectedItem();
			}
		}
		
		CellPanel p = CellPanel.create(this, t, null, -1, null);
			
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
		UI.scrollRectToVisible(p);
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
			else
			{
				setActiveCell(null);
			}
			
			UI.validateAndRepaint(this);
			setModified(true);
		}
	}
	
	
	protected void actionInterrupt()
	{
		engine.interrupt();
	}
	
	
	protected void actionRestartEngine()
	{
		int ch = Dialogs.choice
		(
			this, 
			"Restart Engine", 
			"Do you want to restart the script engine?\nYou will lose all variables defined in it",
			new String[]
			{
				"Continue Running",
				"Restart",
			}
		);
		
		if(ch == 1)
		{
			engine = new JsEngine(this);
		}
	}
	
	
	protected void actionSplit()
	{
		CellType t = activeCell.getType();
		JTextComponent ed = activeCell.getEditor();
		int pos = ed.getCaretPosition();
		String text = activeCell.getText();
		
		String topText = text.substring(0, pos);
		String botText = text.substring(pos);
		
		CellPanel top = CellPanel.create(this, t, topText, -1, null);
		CellPanel bot = CellPanel.create(this, t, botText, -1, null);
		
		int ix = indexOf(activeCell);
		panel.remove(activeCell);
		
		insert(ix, bot);
		insert(ix, top);
		
		setActiveCell(bot);
		
		UI.validateAndRepaint(this);
		setModified(true);
	}
	
	
	protected void actionMergeCell(boolean above)
	{
		int ix = indexOf(activeCell);
		if(ix < 0)
		{
			return;
		}
		
		int mergeIndex;
		if(above)
		{
			if(ix == 0)
			{
				return;
			}
			
			mergeIndex = ix - 1;
		}
		else
		{
			if(ix >= getCellCount())
			{
				return;
			}
			
			mergeIndex = ix + 1;
		}
		
		CellPanel p = getCellAt(mergeIndex);
		String mergeText = p.getText();
		
		panel.remove(p);
		
		String text = activeCell.getText();
		int caret = activeCell.getEditor().getCaretPosition();
		
		if(above)
		{
			text = mergeText + "\n" + text;
			caret = mergeText.length() + 1;
		}
		else
		{
			text = text + "\n" + mergeText;
		}
		
		activeCell.getEditor().setText(text);
		activeCell.getEditor().setCaretPosition(caret);
		
		UI.validateAndRepaint(this);
		setModified(true);
	}
	
	
	protected void actionMoveCell(boolean up)
	{
		int ix = indexOf(activeCell);
		if(ix < 0)
		{
			return;
		}
		
		if(up)
		{
			if(ix == 0)
			{
				return;
			}
			
			panel.remove(activeCell);
			insert(ix - 1, activeCell);
		}
		else
		{
			if(ix >= (getCellCount() - 1))
			{
				return;
			}
			
			panel.remove(activeCell);
			insert(ix + 1, activeCell);
		}
		
		UI.validateAndRepaint(this);
		setModified(true);
		
		UI.scrollRectToVisible(activeCell);
	}
	
	
	public void actionCopy()
	{
		// TODO
	}
	
	
	public void actionCut()
	{
		// TODO
	}

	
	public void actionPaste()
	{
		// TODO
	}
}
