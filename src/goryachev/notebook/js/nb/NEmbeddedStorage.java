// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.nb;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.DelayedAction;
import goryachev.notebook.storage.EmbeddedStorage;
import java.io.File;
import java.util.Properties;


public class NEmbeddedStorage
	extends EmbeddedStorage
{
	private final File file;
	private Properties prop;
	private DelayedAction saveAction;
	static Log log = Log.get("NEmbeddedStorage");
	
	
	public NEmbeddedStorage(File f)
	{
		this.file = f;
	}
	
	
	protected Properties prop()
	{
		if(prop == null)
		{
			prop = CKit.readProperties(file);
		}
		return prop;
	}
	
	
	public synchronized void setValue(String key, String val)
	{
		if(val == null)
		{
			prop().remove(key);
		}
		else
		{
			prop().setProperty(key, val);
		}
		
		if(saveAction == null)
		{
			saveAction = new DelayedAction("save", this::save);
		}
		
		saveAction.schedule(150);
	}


	public synchronized String getValue(String key)
	{
		return prop().getProperty(key);
	}


	public synchronized CList<String> getKeys()
	{
		return new CList(prop().stringPropertyNames());
	}
	
	
	protected synchronized void save()
	{
		try
		{
			CKit.writeProperties(prop, file);
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
}
