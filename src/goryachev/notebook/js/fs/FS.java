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
	
	
	public long lastModified(Object x) throws Exception
	{
		File f = JsUtil.parseFile(x);
		return f.lastModified();
	}
	
	
	public boolean isFile(Object x) throws Exception
	{
		File f = JsUtil.parseFile(x);
		return f.isFile();
	}
	
	
	public boolean isDirectory(Object x) throws Exception
	{
		File f = JsUtil.parseFile(x);
		return f.isDirectory();
	}
	
	
	public boolean isHidden(Object x) throws Exception
	{
		File f = JsUtil.parseFile(x);
		return f.isHidden();
	}
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp("FS");
		h.a("provides access to the filesystem:");
		
		h.a("freeSpace, getFreeSpace(file)", "returns the amount of free space");
		h.a("isDirectory(path)", "tests whether the file denoted by this path is a directory"); 
		h.a("isFile(path)", "tests whether the file denoted by this path is a normal file");
		h.a("isHidden(path)", "tests whether the file denoted by this path is a hidden file"); 
		h.a("lastModified(file)", "returns the file timestamp");
		h.a("ls([path],[mode])", "returns the list of files in the current directory");
		h.a("pwd()", "returns the current directory");
		h.a("totalSpace, getTotalSpace(file)", "returns the amount of total space");
		h.a("touch", "updates the timestamp of a file, creating it if necessary");
		return h.toString();
	}
}
