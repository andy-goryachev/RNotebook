// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.util.CSorter;
import goryachev.common.util.FileTools;
import goryachev.common.util.SB;
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
	
	
	// TODO ls prints, listFiles returns an array
	public String ls(Object ... files) throws Exception
	{
		boolean printDirName = false;
		boolean current = false;
		if(files.length == 0)
		{
			current = true;
		}
		else if(files.length > 1)
		{
			printDirName = true;
		}
		
		SB sb = new SB();
		
		if(current)
		{
			list(cur(), false, sb);
		}
		
		boolean nl = false;
		for(Object fn: files)
		{
			if(nl)
			{
				sb.nl();
			}
			else
			{
				nl = true;
			}
			
			File f = JsUtil.parseFile(fn);
			list(f, printDirName, sb);
		}
		
		return sb.toString();
	}
	
	
	protected void list(File dir, boolean printDirName, SB sb) throws Exception
	{
		if(printDirName)
		{
			sb.conditionalNewline();
			sb.a(dir.getCanonicalPath());
			sb.a(':');
		}
		
		File[] fs = dir.listFiles();
		if(fs != null)
		{
			CSorter.sort(fs);
			
			int w = 0;
			for(File f: fs)
			{
				int len = f.getName().length();
				if(f.isDirectory())
				{
					len++;
				}
				if(w < len)
				{
					w = len;
				}
			}
			
			w += 1;
			
			for(File f: fs)
			{
				String name = f.getName();
				sb.conditionalNewline();
				sb.a(name);
				if(f.isDirectory())
				{
					sb.a('/');
				}
				sb.sp(w - name.length());
				// TODO size
			}
		}
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
	
	
	public boolean exists(Object x) throws Exception
	{
		File f = JsUtil.parseFile(x);
		return f.exists();
	}
	
	
	public File tempFile(String prefix, String suffix) throws Exception
	{
		return File.createTempFile(prefix, suffix);
	}
	
	
	public String toString()
	{
		return "FS";
	}
	
	
	// sync src, target
	// newSync() -> src, target, filter, listener, overwrite, includeEmptyDirs, failOnError, verbose? granularity
	public String sync(Object source, Object target) throws Exception
	{
		File src = JsUtil.parseFile(source);
		File tgt = JsUtil.parseFile(target);
		
		FileSyncTool t = new FileSyncTool();
		t.setSource(src);
		t.setTarget(tgt);
		t.setGranularity(2000);
		t.setIncludeEmptyDirs(true);
		t.sync();
		return t.getReport();
	}
	
	
	public InlineHelp getHelp()
	{
		InlineHelp h = new InlineHelp("FS");
		h.a("FS provides access to the filesystem:");
		
		h.a("exists(file)", "tests whether the file exists");
		h.a("freeSpace, getFreeSpace(path)", "returns the amount of free space");
		h.a("isDirectory(path)", "tests whether the file denoted by this path is a directory"); 
		h.a("isFile(path)", "tests whether the file denoted by this path is a normal file");
		h.a("isHidden(path)", "tests whether the file denoted by this path is a hidden file"); 
		h.a("lastModified(file)", "returns the file timestamp");
		h.a("ls([path],...)", "lists files");
		h.a("pwd()", "returns the current directory");
		h.a("tempFile(prefix, suffix)", "creates a temporary file");
		h.a("totalSpace, getTotalSpace(path)", "returns the amount of total space");
		h.a("touch(file)", "updates the timestamp of a file, creating it if necessary");
		return h;
	}
}
