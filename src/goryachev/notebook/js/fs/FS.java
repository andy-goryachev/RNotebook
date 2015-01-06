// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.util.Dump;
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
		// FIX
		return Dump.list(as);
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
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp("FS");
		h.a("provides access to the filesystem:");
		
		h.a("freeSpace, getFreeSpace(file)", "returns the amount of free space");
		h.a("ls([path],[mode])", "returns the list of files in the current directory");
		h.a("pwd()", "returns the current directory");
		h.a("totalSpace, getTotalSpace(file)", "returns the amount of total space");
		return h.toString();
	}
}
