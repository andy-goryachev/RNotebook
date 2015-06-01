// Copyright (c) 2010-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.table.CTableSelector;
import goryachev.common.ui.table.ZModel;
import goryachev.common.ui.table.ZTable;
import goryachev.common.util.CList;
import java.awt.Component;


/** Base class for table-detail kind of panels */
public abstract class TableDetailViewPanel<T>
	extends CPanel3
{
	protected abstract void onTableSelectionChanged();
	
	protected final void createTablePopup() { }
	
	protected final void onTableDoubleClick() { }
	
	//
	
	public final ZModel<T> model;
	public final ZTable table;
	public final CScrollPane scroll;
	public final CTableSelector selector;
	public final CSplitPane split;
	public final CPanel3 tablePanel;
	public final CPanel3 detailPanel;


	public TableDetailViewPanel(String name, ZModel<T> m)
	{
		this.model = m;
		setName(name);
		
		table = new ZTable(model);
		table.setSortable(true);
		
		selector = new CTableSelector(table, true, false, true)
		{
			public void tableSelectionChangeDetected()
			{
				onTableSelectionChanged();
			}
		};

		scroll = createScrollPane(table); 
		
		// menus should be outside of this component
//		new CPopupMenuController(table, scroll)
//		{
//			public JPopupMenu constructPopupMenu()
//			{
//				return createTablePopup();
//			}
//			
//			public void onDoubleClick()
//			{
//				onTableDoubleClick();
//			}
//		};

		tablePanel = new CPanel3();
		tablePanel.setCenter(scroll);
		
		// detail
		
		detailPanel = new CPanel3();
		detailPanel.setName("detail");
		detailPanel.setBackground(Theme.textBG());
		
		// layout
		
		split = new CSplitPane(false, tablePanel, detailPanel);
		split.setName("split");
		split.setBorder(CBorder.NONE);
		
		setCenter(split);
	}
	
	
	protected CScrollPane createScrollPane(ZTable t)
	{
		CScrollPane s = new CScrollPane(t, CScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, CScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		s.getViewport().setBackground(Theme.textBG());
		return s;
	}
	
	
	public CList<T> getSelectedEntries()
	{
		CList<T> sel = model.getSelectedEntries(selector);
		return sel;
	}
	
	
	public T getSelectedEntry()
	{
		return model.getSelectedEntry(selector);
	}
	
	
	public boolean select(T item)
	{
		int ix = model.indexOfKey(item);
		if(ix < 0)
		{
			return false;
		}
		else
		{
			selector.setSelectedModelRow(ix);
			return true;
		}
	}
	
	
	public void setDetailPane(Component c)
	{
		GlobalSettings.storePreferences(detailPanel);

		detailPanel.setCenter(c);
		
		GlobalSettings.restorePreferences(detailPanel);
		detailPanel.validate();
		detailPanel.repaint();
	}
	
	
	public void selectAll()
	{
		table.selectAll();
	}
	

	public void selectFirstRow()
	{
		selector.selectFirstRow();
	}
}
