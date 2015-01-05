// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.util.Dump;
import goryachev.notebook.util.InlineHelp;
import java.io.File;


/** "FS" object in the global context */
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
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp();
		h.a("FS provides filesystem operations:");
		h.a("FS.ls([path],[mode])", "lists files in current directory");
		h.a("FS.pwd()", "prints current directory");
		return h.toString();
	}
}
