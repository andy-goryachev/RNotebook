// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.storage;
import goryachev.common.util.CKit;
import goryachev.swing.table.ZColumnHandler;
import goryachev.swing.table.ZModel;


public class StorageTableModel
	extends ZModel<StorageEntry>
{
	public StorageTableModel()
	{
		addColumn("Key", new ZColumnHandler<StorageEntry>()
		{
			public Object getCellValue(StorageEntry x) { return x.getKey(); }
		});
		setPreferredWidth(250);
		
		addColumn("Value", new ZColumnHandler<StorageEntry>()
		{
			public Object getCellValue(StorageEntry x) { return x.getValue(); }
		});
	}
	

	public StorageEntry findByKey(String k)
	{
		if(k != null)
		{
			for(StorageEntry en: this)
			{
				if(CKit.equals(en.getKey(), k))
				{
					return en;
				}
			}
		}
		return null;
	}
}
