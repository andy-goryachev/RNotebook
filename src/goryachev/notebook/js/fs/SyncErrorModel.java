// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.swing.table.ZColumnHandler;
import goryachev.swing.table.ZModel;
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
