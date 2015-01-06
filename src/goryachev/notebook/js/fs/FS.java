// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.util.FileTools;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.InlineHelp;
import java.io.File;


public class FS
{
	private File cur;
	
	
	public FS()
	{
	}
	
	
	protected File cur()
	{
		if(cur == null)
		{
			cur = new File(".");
		}
		return cur;
	}
	
	
	public String pwd() throws Exception
	{
		return cur().getCanonicalPath();
	}
	
	
	public String ls(Object ... as)
	{
		throw JsUtil.todo();
	}
	
	
	public long getFreeSpace()
	{
		return getFreeSpace(cur());
	}
	
	
	public long getFreeSpace(Object x)
	{
		File f = JsUtil.parseFile(x);
		return f.getFreeSpace();
	}
	
	
	public long getTotalSpace()
	{
		return getTotalSpace(cur());
	}
	
	
	public long getTotalSpace(Object x)
	{
		File f = JsUtil.parseFile(x);
		return f.getTotalSpace();
	}
	
	
	public void touch(Object x) throws Exception
	{
		File f = JsUtil.parseFile(x);
		if(!f.exists())
		{
			FileTools.createZeroLengthFile(f);
		}
		else
		{
			f.setLastModified(System.currentTimeMillis());
		}
	}
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp("FS");
		h.a("provides access to the filesystem:");
		
		h.a("freeSpace, getFreeSpace(file)", "returns the amount of free space");
		h.a("ls([path],[mode])", "returns the list of files in the current directory");
		h.a("pwd()", "returns the current directory");
		h.a("totalSpace, getTotalSpace(file)", "returns the amount of total space");
		h.a("touch", "updates the timestamp of a file, creating it if necessary");
		return h.toString();
	}
}
