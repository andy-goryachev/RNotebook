// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.CSplitPane;
import goryachev.common.ui.GlobalSettings;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.ui.table.CTableSelector;
import goryachev.common.ui.table.ZColumnHandler;
import goryachev.common.ui.table.ZModel;
import goryachev.common.ui.table.ZTable;
import goryachev.common.ui.table.ZTableRenderer;
import goryachev.common.util.CKit;
import goryachev.common.util.HasPrompts;
import goryachev.common.util.NamedObjects;
import goryachev.common.util.TXT;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


// wizard panel.  use addPage()
@Deprecated
public class WizardPanel
	extends CPanel
	implements HasPrompts
{
	public final ZModel<WizardPage> model;
	public final ZTable table;
	public final CTableSelector selector;
	public final CSplitPane split;
	public final CPanel panel;
	public final CPanel detailPanel;
	public final CPanel sidePanel;
	public final CPanel buttonPanel;
	public final JTextField titleField;
	public final JLabel cornerField;
	private NamedObjects settings = new NamedObjects();
	
	
	public WizardPanel(boolean disableNavigation)
	{
		model = new ZModel<WizardPage>();
		model.addColumn("", new ZColumnHandler<WizardPage>()
		{
			public Object getCellValue(WizardPage x) { return x; }
			public String getText(Object x) { return ((WizardPage)x).getTitle(); }
			public void decorate(Object x, ZTableRenderer r)
			{
				WizardPage d = (WizardPage)x;
				r.setForeground(d.isEnabled() ? Theme.textFG() : Theme.panelFG());
				r.setIcon(d.isCompleted() ? CIcons.ItemCheck : CIcons.Empty16);
			}
		});
		
		table = new ZTable(model)
		{
			public void changeSelection(int row, int col, boolean toggle, boolean extend)
			{
				WizardPage en = getItem(row);
				if(en != null)
				{
					if(en.isEnabled())
					{
						super.changeSelection(row, col, toggle, extend);
					}
				}
			}
		};
		table.setTableHeader(null);
		table.setBackground(Theme.fieldBG());
		GlobalSettings.ignore(table);
		
		selector = new CTableSelector(table)
		{
			public void tableSelectionChangeDetected()
            {
				selectionUpdated();
            }
		};
		
		// remove handling of mouse events from table
		if(disableNavigation)
		{
			UI.removeMouseListeners(table);
			UI.removeKeyListeners(table);
			table.setFocusable(false);
		}
		// TODO auto-resize table?
		
		CScrollPane tableScroll = new CScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tableScroll.setBorder(CBorder.NONE);
		tableScroll.setViewportBorder(CBorder.NONE);
		tableScroll.getViewport().setBackground(Theme.textBG());
		tableScroll.setBackground2(table.getBackground());
		
		titleField = new JTextField();
		titleField.setFont(Theme.titleFont());
		titleField.setEditable(false);
		titleField.setBorder(new CBorder(0, 0, 1, 0, Theme.panelBG().darker(), 10));
		
		cornerField = new JLabel()
		{
			public Dimension getPreferredSize()
			{
				Dimension d = titleField.getPreferredSize();
				d.width = -1;
				return d;
			}
		};
		
		buttonPanel = new CPanel();
		buttonPanel.setBorder(new CBorder(1, 0, 0, 0, Theme.panelBG().darker(), 10));

		sidePanel = new CPanel();
		//sidePanel.setNorth(cornerField);
		sidePanel.setCenter(tableScroll);
		
		detailPanel = new CPanel();
		detailPanel.setNorth(titleField);

		split = new CSplitPane(true, sidePanel, detailPanel);
		split.setDividerLocation(150);
		split.setBorder(null);
		
		panel = new CPanel();
		panel.setCenter(split);
		
		setCenter(panel);
		setSouth(buttonPanel);
		
		TXT.registerListener(this);
	}
	

	public void selectFirstRow()
	{
		table.changeSelection(0, 0, false, false);
	}
	
	
	protected void selectionUpdated()
	{
		WizardPage p = model.getSelectedEntry(selector);
		openPage(p);
	}

	
	protected void openPage(WizardPage p)
	{
		if(p != null)
		{
			setDetailPane(p.getComponent());
			buttonPanel.setCenter(p.getButtons());
			titleField.setText(p.getTitle());
			
			p.notifyPageOpened();
		}
	}
	
	
	public void setDetailPane(JComponent c)
	{
		GlobalSettings.storePreferences(this);
		
		detailPanel.setCenter(c);
				
		GlobalSettings.restorePreferences(this);
		
		revalidate();
		repaint();
	}
	
	
	public void addPage(WizardPage p)
	{
		model.addItem(p);
		// ensure wizard pointer even if page gets disconnected from component hierarchy
		p.setWizardPanel(this);
	}
	
	
	public int getPageCount()
	{
		return model.size();
	}
	
	
	public boolean containsPage(WizardPage p)
	{
		return model.indexOfKey(p) >= 0;
	}
	
	
	public void insertPage(int index, WizardPage p)
	{
		p.setWizardPanel(this);
		model.insertItem(index, p);
	}
	
	
	public void insertLastPage(WizardPage p)
	{
		int ix = model.size();
		insertPage(ix, p);
	}
	
	
	public void insertAfterPage(WizardPage afterWhich, WizardPage p)
	{
		int ix = model.indexOfKey(afterWhich);
		if(ix < 0)
		{
			ix = model.size();
		}
		else
		{
			ix++;
		}
		insertPage(ix, p);
		selector.setSelectedModelRow(ix);
	}
	
	
	public void removePage(WizardPage p)
	{
		int ix = model.indexOfKey(p);
		if(ix >= 0)
		{
			WizardPage sel = getSelectedPage();			
			model.remove(ix);
			
			if(sel == p)
			{
				--ix;
				if(ix < 0)
				{
					ix = 0;
				}
				
				selector.setSelectedModelRow(ix);
			}
		}
	}
	
	
	public void removeAllButPage(WizardPage p)
	{
		for(int i=model.size()-1; i>=0; --i)
		{
			WizardPage wp = model.getItem(i);
			if(wp != p)
			{
				model.remove(i);
			}
		}
		selector.ensureSelection();
	}
	
	
	public void removeAllButPages(WizardPage ... ps)
	{
		for(int i=model.size()-1; i>=0; --i)
		{
			WizardPage wp = model.getItem(i);
			if(CKit.indexOf(ps, wp) < 0)
			{
				model.remove(i);
			}
		}
	}
	
	
	public void refreshTable()
	{
		model.refreshAll();
	}


	public void setSelectedPage(WizardPage p)
	{
		if(p != null)
		{
			int ix = model.indexOfKey(p);
			selector.setSelectedModelRow(ix);
			// TODO focus on previously focused component!
			//p.getComponent().requestFocusInWindow();
		}
	}
	
	
	public WizardPage getSelectedPage()
	{
		return model.getSelectedEntry(selector);
	}
	
	
	protected WizardPage getItem(int viewRow)
	{
		int ix = table.convertRowIndexToModel(viewRow);
		if(ix < 0)
		{
			return null;
		}
		else
		{
			return model.getItem(ix);
		}
	}
	
	
	public WizardPage getFirstPage()
	{
		return getItem(0);
	}
	
	
	public WizardPage getPrevPage()
	{
		int ix = table.getSelectedRow() - 1;
		if(ix >= 0)
		{
			return getItem(ix);
		}
		return null;
	}
	
	
	public WizardPage getNextPage()
	{
		int ix = table.getSelectedRow() + 1;
		if(ix < table.getRowCount())
		{
			return getItem(ix);
		}
		return null;
	}
	
	
	public void updatePrompts()
	{
		refreshTable();
	}
	
	
	public NamedObjects getNamedObjects()
	{
		return settings;
	}
}
