// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.util.CSorter;
import goryachev.common.util.FileTools;
import goryachev.common.util.SB;
import goryachev.notebook.js.JsObjects;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.classes.JsFileScanner;
import goryachev.notebook.js.classes.JsFileSyncTool;
import goryachev.notebook.util.Arg;
import goryachev.notebook.util.Doc;
import goryachev.notebook.util.InlineHelp;
import java.io.File;
import research.tools.filesync.FileSyncTool;


@Doc("provides access to the filesystem")
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
	
	
	@Doc("returns the current directory")
	@Arg("")
	public String pwd() throws Exception
	{
		return cur().getCanonicalPath();
	}
	
	
	// TODO ls prints, listFiles returns an array
	@Doc("lists files")
	@Arg({"file,", "..."})
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
	
	
	@Doc("returns the amount of free space")
	@Arg("")
	public long getFreeSpace()
	{
		return getFreeSpace(cur());
	}
	
	
	@Doc("returns the amount of free space on the device")
	@Arg("path")
	public long getFreeSpace(Object x)
	{
		File f = JsUtil.parseFile(x);
		return f.getFreeSpace();
	}
	
	
	@Doc("returns the amount of total space")
	@Arg("")
	public long getTotalSpace()
	{
		return getTotalSpace(cur());
	}
	
	
	@Doc("returns the amount of total space on the device")
	@Arg("path")
	public long getTotalSpace(Object path)
	{
		File f = JsUtil.parseFile(path);
		return f.getTotalSpace();
	}
	
	
	@Doc("updates the timestamp of a file, creating it if necessary")
	@Arg("path")
	public void touch(Object path) throws Exception
	{
		File f = JsUtil.parseFile(path);
		if(!f.exists())
		{
			FileTools.createNewFile(f);
		}
		else
		{
			f.setLastModified(System.currentTimeMillis());
		}
	}
	
	
	@Doc("returns the file timestamp")
	@Arg("file")
	public long lastModified(Object file) throws Exception
	{
		File f = JsUtil.parseFile(file);
		return f.lastModified();
	}
	
	
	@Doc("tests whether the file denoted by this path is a normal file")
	@Arg("path")
	public boolean isFile(Object path) throws Exception
	{
		File f = JsUtil.parseFile(path);
		return f.isFile();
	}
	
	
	@Doc("tests whether the file denoted by this path is a directory")
	@Arg("path")
	public boolean isDirectory(Object path) throws Exception
	{
		File f = JsUtil.parseFile(path);
		return f.isDirectory();
	}
	
	
	@Doc("tests whether the file denoted by this path is a hidden file")
	@Arg("path")
	public boolean isHidden(Object path) throws Exception
	{
		File f = JsUtil.parseFile(path);
		return f.isHidden();
	}
	
	
	@Doc("tests whether the file exists")
	@Arg("file")
	public boolean exists(Object file) throws Exception
	{
		File f = JsUtil.parseFile(file);
		return f.exists();
	}
	
	
	@Doc("creates a temporary file")
	@Arg({"prefix", "suffix"})
	public File tempFile(String prefix, String suffix) throws Exception
	{
		return File.createTempFile(prefix, suffix);
	}
	
	
	public String toString()
	{
		return getHelp().toString();
	}
	
	
	@Doc("synchronizes target with the source directory, ignoring failures")
	@Arg({"source", "target"})
	public String sync(Object source, Object target) throws Exception
	{		
		JsFileSyncTool t = new JsFileSyncTool();
		t.addJob(source, target);
		t.setGranularity(2000);
		t.setIgnoreFailures(true);
		
		final SB sb = new SB();
		
		t.setListener(new FileSyncTool.Listener()
		{
			public void handleSyncWarning(File src, File dst, String err)
			{
				sb.a(err).nl();
			}

			public void handleSyncTarget(File f) { }
			public void handleSyncRunning(FileSyncTool parent, boolean on) { }
		});
		
		t.sync();
		
		return sb.toString();
	}
	
	
	@Doc("returns a new file synchronization tool")
	@Arg("")
	public JsFileSyncTool newSync()
	{
		return new JsFileSyncTool();
	}
	
	
	@Doc("returns a new file scanner tool")
	@Arg("")
	public JsFileScanner newFileScanner()
	{
		return new JsFileScanner();
	}
	
	
	public InlineHelp getHelp()
	{
		return InlineHelp.create(JsObjects.FS, getClass());
	}
}
