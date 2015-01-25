// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.ui.table.ZColumnHandler;
import goryachev.common.ui.table.ZModel;
import java.io.File;


public class SyncErrorModel
	extends ZModel<SyncErrorModel.Entry>
{
	public static class Entry
	{
		public String error;
		public File src;
		public File dst;
	}
	
	//
	
	public SyncErrorModel()
	{
		addColumn("Error", new ZColumnHandler<Entry>()
		{
			public Object getCellValue(Entry x)
			{
				return x.error;
			}
		});
		
		addColumn("Source", new ZColumnHandler<Entry>()
		{
			public Object getCellValue(Entry x)
			{
				return parent(x.src);
			}
		});
		setPreferredWidth(300);
		
		addColumn("Destination", new ZColumnHandler<Entry>()
		{
			public Object getCellValue(Entry x)
			{
				return parent(x.dst);
			}
		});
		setPreferredWidth(300);
	}
	
	
	protected String parent(File f)
	{
		if(f == null)
		{
			return null;
		}
		else
		{
			return f.getParent();
		}
	}
}
