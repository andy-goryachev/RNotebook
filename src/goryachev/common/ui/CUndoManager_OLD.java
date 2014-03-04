// Copyright (c) 2012-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.Log;
import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;


public class CUndoManager_OLD
	extends UndoManager
{
	public static final Object KEY_UNDO_MANAGER = new Object();
	public final UndoAction undoAction = new UndoAction();
	public final RedoAction redoAction = new RedoAction();
	public static final GlobalUndoAction globalUndoAction = new GlobalUndoAction();
	public static final GlobalRedoAction globalRedoAction = new GlobalRedoAction();
	static
	{
		init();
	}
	

	public CUndoManager_OLD()
	{
	}
	
	
	private static void init()
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent ev)
			{
				Object x = ev.getNewValue();
				if(x instanceof Component)
				{
					CUndoManager_OLD m = CUndoManager_OLD.get((Component)x);
					if(m != null)
					{
						globalUndoAction.setManager(m);
						globalRedoAction.setManager(m);					
					}
				}
			}
		});
	}
	
	
	public void attachTo(Container c)
	{
		JComponent jc = findJComponent(c);
		jc.putClientProperty(KEY_UNDO_MANAGER, this);
	}
	
	
	public static JComponent findJComponent(Container c)
	{
		if(c instanceof JFrame)
		{
			return ((JFrame)c).getRootPane();
		}
		else if(c instanceof JComponent)
		{
			return (JComponent)c;
		}
		else
		{
			return null;
		}
	}
	
	
	public static CUndoManager_OLD get(Component c)
	{
		if(c == null)
		{
			return null;
		}
		
		Container con;
		if(c instanceof Container)
		{
			con = (Container)c;
		}
		else
		{
			con = c.getParent();
		}
		
		Object m = null;
		while(con != null)
		{
			JComponent jc = findJComponent(con);
			if(jc == null)
			{
				return null;
			}
			
			m = jc.getClientProperty(KEY_UNDO_MANAGER);
			if(m instanceof CUndoManager_OLD)
			{
				return (CUndoManager_OLD)m;
			}
			else if(con instanceof JFrame)
			{
				return null;
			}
			
			con = jc.getParent();
		}
		
		return null;
	}


	public synchronized boolean addEdit(UndoableEdit ed)
	{
		boolean rv = super.addEdit(ed);
		update();
		return rv;
	}


	public synchronized void discardAllEdits()
	{
		super.discardAllEdits();
		update();
	}


	protected void update()
	{
		undoAction.updateUndoState();
		redoAction.updateRedoState();
	}


	// TODO attach to the component being observed?
	public void observe(JTextComponent c)
	{
		c.getDocument().addUndoableEditListener(this);
	}
	
	
	public static void monitor(JTextComponent c)
	{
		CUndoManager_OLD m = CUndoManager_OLD.get(c);
		if(m == null)
		{
			throw new NullPointerException();
		}
		c.getDocument().addUndoableEditListener(m);
	}


	//
	
	
	public class RedoAction
	    extends CAction
	{
		public RedoAction()
		{
			super(Menus.Redo);
			setEnabled(false);
		}
		
		
		public void action()
		{
			// not called
		}
	
	
		public void actionPerformed(ActionEvent ev)
		{
			try
			{
				redo();
			}
			catch(Exception e)
			{
				Log.err(e);
				UI.beep();
			}
			
			update();
		}
	
	
		protected void updateRedoState()
		{
			if(canRedo())
			{
				setEnabled(true);
				setName(getRedoPresentationName());
			}
			else
			{
				setEnabled(false);
				setName(Menus.Redo);
			}
		}
	}
	
	
	//
	
	
	public class UndoAction
		extends CAction
	{
		public UndoAction()
		{
			super(Menus.Undo);
			setEnabled(false);
		}
		
		
		public void action()
		{
			// not called
		}
	
	
		public void actionPerformed(ActionEvent ev)
		{
			try
			{
				undo();
			}
			catch(Exception e)
			{
				Log.err(e);
				UI.beep();
			}
			
			update();
		}
	
	
		protected void updateUndoState()
		{
			if(canUndo())
			{
				setEnabled(true);
				setName(getUndoPresentationName());
			}
			else
			{
				setEnabled(false);
				setName(Menus.Undo);
			}
		}
	}
	
	
	//
	
	
	public abstract static class AbstractGlobalAction
		extends CAction 
		implements PropertyChangeListener
	{
		protected abstract Action getAction(CUndoManager_OLD m);
		
		//
		
		private Action action;
		
		
		public AbstractGlobalAction()
		{
			setEnabled(false);
		}
		
		
		public void action()
		{
			// not called
		}
		
		
		public void actionPerformed(ActionEvent ev)
		{
			if(action != null)
			{
				action.actionPerformed(ev);
			}
		}
	
	
		public void propertyChange(PropertyChangeEvent ev)
		{
			String id = ev.getPropertyName();
			if("Name".equals(id))
			{
				update();
			}
			else if("enabled".equals(id))
			{
				update();
			}
//			else
//			{
//				D.print(ev.getPropertyName());
//			}
		}
		
		
		protected void setManager(CUndoManager_OLD m)
		{
			Action a = getAction(m);
			if(action != a)
			{
				// change action
				if(action != null)
				{
					action.removePropertyChangeListener(this);
				}
				
				action = a;
				
				action.addPropertyChangeListener(this);
				update();
			}
		}
		
		
		protected void update()
		{
			if(action != null)
			{
				// TODO set tooltip instead
				//setText((String)action.getValue(Action.NAME));
				setEnabled(action.isEnabled());
			}
		}
	}
	
	
	//
	
	
	public static class GlobalUndoAction
		extends AbstractGlobalAction
	{
		protected Action getAction(CUndoManager_OLD m)
		{
			return m.undoAction;
		}
	}
	
	
	//
	
	
	public static class GlobalRedoAction
		extends AbstractGlobalAction
	{
		protected Action getAction(CUndoManager_OLD m)
		{
			return m.redoAction;
		}
	}
}
