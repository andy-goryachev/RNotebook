// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.storage;
import goryachev.common.ui.table.ZColumnHandler;
import goryachev.common.ui.table.ZModel;
import goryachev.common.util.CKit;


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
