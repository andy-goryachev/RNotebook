// Copyright (c) 2008-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.storage;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CPopupMenu;
import goryachev.common.ui.CPopupMenuController;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.CToolBar;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.Menus;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.options.OptionEditorInterface;
import goryachev.common.ui.table.CTableSelector;
import goryachev.common.ui.table.ZFilterLogic;
import goryachev.common.ui.table.ZTable;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;


public class StorageEditor
	extends CPanel
	implements OptionEditorInterface
{
	public final CAction addAction = new CAction() { public void action() { actionAdd(); } };
	public final CAction deleteAction = new CAction() { public void action() { actionDelete(); } };
	public final CAction modifyAction = new CAction() { public void action() { actionModify(); } };
	public final EmbeddedStorage storage;
	public final StorageTableModel model;
	public final ZTable table;
	public final ZFilterLogic filter;
	public final CTableSelector selector;
	
	
	public StorageEditor(EmbeddedStorage st)
	{
		this.storage = st;
		
		model = new StorageTableModel();
		
		table = new ZTable(model);
		table.setSortable(true);
		
		UI.whenFocused(table, KeyEvent.VK_ENTER, modifyAction);
		
		filter = new ZFilterLogic(table);
		
		selector = new CTableSelector(table)
		{
			public void tableSelectionChangeDetected()
            {
				onSelectionChange();
            }
		};
		
		CScrollPane scroll = new CScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(new CBorder());
		scroll.setViewportBorder(CBorder.NONE);
		scroll.getViewport().setBackground(Theme.fieldBG());

		new CPopupMenuController(table, scroll)
		{
			public JPopupMenu constructPopupMenu()
			{
				return createTablePopupMenu();
			}

			public void onDoubleClick()
			{
				actionModify();
			}
		};		

		// bottom panel
		
		setNorth(createToolbar());
		setCenter(scroll);
		setName("StorageEditor");
	}
	
	
	public JPopupMenu createTablePopupMenu()
	{
		CPopupMenu m = new CPopupMenu();
		m.add(new CMenuItem(Menus.Add, addAction));
		m.add(new CMenuItem(Menus.Modify, modifyAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Delete, deleteAction));
		return m;
	}
	
	
	protected void onSelectionChange()
	{
		StorageEntry x = getSelectedEntry();
		setEntry(x);
	}
	
	
	protected StorageEntry getSelectedEntry()
	{
		return model.getSelectedEntry(selector);
	}
	
	
	public void init()
	{
		load();
		
		setEntry(null);
	}


	public boolean isFullWidth()
	{
		return true;
	}


	public float getPreferredHeight()
	{
		return HEIGHT_MAX;
	}


	public JComponent getComponent()
	{
		return this;
	}


	private JComponent createToolbar()
	{
		CToolBar t = Theme.toolbar();
		t.add(new CButton(Menus.Add, addAction));
		t.add(new CButton(Menus.Modify, modifyAction));
		t.space();
		t.add(new CButton(Menus.Delete, deleteAction));
		t.fill();
		t.add(200, filter.getComponent());
		return t;
	}
	

	public void load()
	{
		CList<StorageEntry> list = new CList();
		
		for(String k: storage.getKeys())
		{
			String v = storage.getValue(k);
			list.add(new StorageEntry(k, v));
		}

		model.replaceAll(list);
	}


	public void commit()
    {
		for(StorageEntry en: model.getItems())
		{
			if(en.isModified())
			{
				storage.setValue(en.getKey(), en.getValue());
			}
		}
    }
	

	protected void setEntry(StorageEntry se)
	{
		boolean en = (se != null);
		deleteAction.setEnabled(en);
		modifyAction.setEnabled(en);
	}
	
	
	protected void actionModify()
	{
		StorageEntry en = getSelectedEntry();
		if(en != null)
		{
			StorageEntryDialog.open(this, en);
			model.refreshAll();
		}
	}
	
	
	protected void actionDelete()
	{
		StorageEntry en = getSelectedEntry();
		if(en != null)
		{
			if(Dialogs.confirm2(this, "Delete", "Delete key " + en.getKey() + "?", "Delete"))
			{
				model.removeItem(en);
				model.refreshAll();
			}
		}
	}
	
	
	public boolean isModified()
    {
		for(StorageEntry en: model.getItems())
		{
			if(en.isModified())
			{
				return true;
			}
		}
	    return false;
    }

	
	public String getSearchString()
	{
		SB sb = new SB();
		sb.a(Menus.Keys).sp();
		for(StorageEntry en: model)
		{
			sb.a(en.getKey()).sp().a(en.getValue()).sp();
		}
		return sb.toString();
	}
	
	
	protected void actionAdd()
	{
		StorageEntry en = StorageEntryDialog.open(this, null);
		if(en != null)
		{
			model.addItem(en);
		}
	}
}
