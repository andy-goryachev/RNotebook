// Copyright (c) 2009-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.log;
import goryachev.common.ui.Theme;
import goryachev.common.ui.table.ZColumnHandler;
import goryachev.common.ui.table.ZModel;
import goryachev.common.ui.table.ZTableRenderer;
import goryachev.common.util.CList;
import goryachev.common.util.TXT;
import goryachev.common.util.log.LogEntry;
import java.awt.Color;
import java.util.Vector;


public class VLogModel
	extends ZModel<LogEntry>
{
	private int start;
	private int end;
	protected static Color errorColor = Color.red;
	private Vector<LogEntry> newEntries = new Vector();
	
	
	public VLogModel()
	{ 
		addColumn(" ", new ZColumnHandler<LogEntry>()
		{
			public Object getCellValue(LogEntry x) { return x; }
			public String getText(Object x) { return null; }
			public void decorate(Object x, ZTableRenderer r) 
			{
				if(((LogEntry)x).isException())
				{
					r.setBackground(errorColor);
				}
				else
				{
					r.setBackground(Theme.textBG());
				}
			}
		});
		setFixedWidth(20);
		
		addColumn(TXT.get("VLogTableModel.column.time", "Time"), new ZColumnHandler<LogEntry>()
		{
			public Object getCellValue(LogEntry x) { return x.getTimestamp(); }
			public String getText(Object x) { return formatDateTime(x); }
		});
		setPreferredWidth(120);
		
		addColumn(TXT.get("VLogTableModel.column.event type", "Type"), new ZColumnHandler<LogEntry>()
		{
			public Object getCellValue(LogEntry x) { return x.isException() ? "Exception" : null; }
		});
		setPreferredWidth(120);
		
		addColumn(TXT.get("LogWindow.column.event", "Event"), new ZColumnHandler<LogEntry>()
		{
			public Object getCellValue(LogEntry x) { return x.getText(); }
		});
		setPreferredWidth(400);
	}
	
	
	public void updateModel()
	{
		int size = newEntries.size();
		if(size > end)
		{
			CList<LogEntry> list;
			synchronized(newEntries)
			{
				list = new CList(newEntries);
				newEntries.clear();
			}
			
			addAll(list);
		}
	}
	

	// in any thread
	public void addEntry(LogEntry en)
	{
		newEntries.add(en);
	}
}
