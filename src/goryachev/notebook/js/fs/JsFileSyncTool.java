// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.ui.UI;
import goryachev.notebook.js.JsUtil;
import java.io.File;
import java.io.FileFilter;
import javax.swing.JComponent;
import research.tools.filesync.FileSyncTool;


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
	
	
	public void setSource(Object file)
    {
		File f = JsUtil.parseFile(file);
		tool().setSource(f);
    }

	
	public void setTarget(Object file)
    {
		File f = JsUtil.parseFile(file);
		tool().setTarget(f);
    }
	
	
	public void setFileFilter(Object filter)
	{
		FileFilter ff;
		if(filter instanceof FileFilter)
		{
			ff = (FileFilter)filter;
		}
		else
		{
			ff = JsFileFilter.parse(filter.toString());
		}
		
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


	public String getReport()
	{
		return tool().getReport();
	}


	public void sync() throws Exception
	{
		tool().sync();
	}
}
