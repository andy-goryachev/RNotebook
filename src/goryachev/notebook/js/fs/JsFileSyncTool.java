// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.notebook.js.JsUtil;
import java.io.File;
import java.io.FileFilter;
import research.tools.filesync.FileSyncTool;


public class JsFileSyncTool
{
	private FileSyncTool tool;
	
	
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
