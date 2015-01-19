// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.ui.UI;
import goryachev.notebook.js.JsUtil;
import java.io.File;
import javax.swing.JComponent;
import research.tools.filesync.FileSyncTool;
import research.tools.filesync.RFileFilter;


public class JsFileSyncTool
{
	private FileSyncTool tool;
	protected FileSyncToolUI ui;
	
	
	public JsFileSyncTool()
	{
	}
	
	
	protected FileSyncTool tool()
	{
		if(tool == null)
		{
			tool = new FileSyncTool();
		}
		return tool;
	}
	
	
	protected void setListener(FileSyncTool.Listener li)
	{
		tool().setListener(li);
	}
	
	
	public JComponent getGui()
	{
		if(ui == null)
		{
			UI.inEDTW(new Runnable()
			{
				public void run()
				{
					ui = new FileSyncToolUI();
					tool().setListener(ui);
				}
			});
		}
		return ui;
	}
	
	
	public void addSource(Object file)
	{
		File f = JsUtil.parseFile(file);
		tool().addSource(f);
	}
	
	
	public void addSource(Object source, Object filter) throws Exception
	{
		File src = JsUtil.parseFile(source);
		RFileFilter f = JsUtil.parseRFileFilter(filter);
		tool().addSource(src, f);
	}
	
	
	public void addJob(Object source, Object target)
	{
		File src = JsUtil.parseFile(source);
		File dst = JsUtil.parseFile(target);
		tool().addJob(src, dst);
	}
	
	
	public void addJob(Object source, Object target, Object filter) throws Exception
	{
		File src = JsUtil.parseFile(source);
		File dst = JsUtil.parseFile(target);
		RFileFilter f = JsUtil.parseRFileFilter(filter);
		tool().addJob(src, dst, f);
	}
	
	
	public void setTarget(Object file)
    {
		File f = JsUtil.parseFile(file);
		tool().setTarget(f);
    }
	
	
	public void setFilter(Object filter) throws Exception
	{
		RFileFilter ff = JsUtil.parseRFileFilter(filter);
		tool().setFileFilter(ff);
	}


	public void setGranularity(int ms)
	{
		tool().setGranularity(ms);
	}


	public void setIgnoreFailures(boolean on)
	{
		tool().setIgnoreFailures(on);
	}


	public void sync() throws Exception
	{
		tool().sync();
	}
}
