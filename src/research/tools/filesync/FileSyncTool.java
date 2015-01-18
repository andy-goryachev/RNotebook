// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.tools.filesync;
import goryachev.common.util.CKit;
import goryachev.common.util.CMap;
import goryachev.common.util.SB;
import goryachev.common.util.UserException;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


// https://ant.apache.org/manual/Tasks/sync.html
public class FileSyncTool
{
	public static interface Listener
	{
		public void handleSyncRunning(FileSyncTool parent, boolean on);
		
		public void handleSyncFileError(File src, File dst, String err);
	}
	
	//
	
	public static class Info
	{
		public File currentFile;
		public File currentTarget;
		public int copiedFiles;
		public int deletedFiles;
		public int totalFiles;
	}
	
	//
	
	private File source;
	private File target;
	private FileFilter filter;
	private int granularity;
	private boolean ignoreFailures;
	private SB warnings; // FIX kill
	private Listener listener = createEmptyListener();
	// info
	private volatile File currentFile;
	private volatile File currentTarget;
	private volatile int copiedFiles;
	private volatile int deletedFiles;
	private volatile int totalFiles;
	
	
	public FileSyncTool()
	{
	}
	
	
	public Info getInfo()
	{
		Info d = new Info();
		d.currentFile = currentFile;
		d.currentTarget = currentTarget;
		d.copiedFiles = copiedFiles;
		d.deletedFiles = deletedFiles;
		d.totalFiles = totalFiles;
		return d;
	}
	
	
	protected Listener createEmptyListener()
	{
		return new Listener()
		{
			public void handleSyncRunning(FileSyncTool parent, boolean on) { }
			public void handleSyncFileError(File src, File dst, String err) { }
		};
	}
	
	
	public void setListener(Listener li)
	{
		if(li == null)
		{
			li = createEmptyListener();
		}
		
		listener = li;
	}
	
	
	public void setSource(File f)
    {
		source = f;
    }

	
	public void setTarget(File f)
    {
		target = f;
    }
	
	
	public void setFileFilter(FileFilter ff)
	{
		filter = ff;
	}
	
	
	public void setGranularity(int ms)
	{
		granularity = ms;
	}


	public void setIgnoreFailures(boolean on)
	{
		ignoreFailures = on;
	}


	// FIX kill
	public String getReport()
	{
		return warnings == null ? "" : warnings.toString();
	}
	
	
	protected void warn(File src, File dst, String msg)
	{
		listener.handleSyncFileError(src, dst, msg);
		
		if(ignoreFailures)
		{
			if(warnings == null)
			{
				warnings = new SB();
			}
			warnings.a(msg).nl();
		}
		else
		{
			throw new UserException(msg);
		}
	}
	
	
	protected boolean deleteRecursively(File file) throws Exception
	{
		CKit.checkCancelled();
		
		boolean result = true;
		if(file.exists())
		{
			if(file.isDirectory())
			{
				File[] fs = file.listFiles();
				if(fs != null)
				{
					for(File f: fs)
					{
						result &= deleteRecursively(f);
					}
				}
			}

			boolean rv = file.delete();
			result &= rv;
			if(rv)
			{
				deletedFiles++;
			}
		}
		return result;
	}


	public void sync() throws Exception
	{
		if(source == null)
		{
			throw new UserException("Please set the source directory");
		}
		else if(target == null)
		{
			throw new UserException("Please set the target directory");
		}
		
		if(target.exists())
		{
			if(!target.isDirectory())
			{
				throw new UserException("Target is not a directory: " + target);
			}
		}
		
		// sync
		listener.handleSyncRunning(this, true);		
		try
		{
			syncPrivate(source, target);
		}
		finally
		{
			listener.handleSyncRunning(this, false);
		}
	}


	protected void syncPrivate(File src, File dst) throws Exception
	{
		CKit.checkCancelled();
		
		currentFile = src;
		currentTarget = dst;
		
		if(src.isFile())
		{
			syncFile(src, dst);
		}
		else if(src.isDirectory())
		{
			syncDirectory(src, dst);
		}
		else
		{
			warn(src, dst, "don't know how to sync " + src);
		}
	}
	
	
	protected boolean isUnchanged(File src, File dst)
	{
		long diff = Math.abs(src.lastModified() - dst.lastModified());
		if(diff > granularity)
		{
			return false;
		}
		
		if(src.length() != dst.length())
		{
			return false;
		}
		
		return true;
	}
	
	
	protected void syncFile(File src, File dst) throws Exception
	{
		if(dst.exists())
		{
			if(dst.isFile())
			{
				totalFiles++;
				
				if(isUnchanged(src, dst))
				{
					// no need to change
					return;
				}
			}
			else
			{
				if(!deleteRecursively(dst))
				{
					warn(src, dst, "unable to delete " + dst);
					return;
				}
			}
		}
		
		try
		{
			// override and copy attributes
			// FIX does not allow progress listener or interruption
			// FIX only 8k buffer
			Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
			copiedFiles++;
		}
		catch(Exception e)
		{
			warn(src, dst, "failed to copy file " + src + " to " + dst);
		}
	}
	
	
	protected void syncDirectory(File src, File dst) throws Exception
	{
		boolean create;
		if(dst.exists())
		{
			if(!dst.isDirectory())
			{
				if(!deleteRecursively(dst))
				{
					warn(src, dst, "unable to delete target " + dst);
				}
				
				create = true;
			}
			else
			{
				create = false;
			}
		}
		else
		{
			create = true;
		}
		
		if(create)
		{
			if(!dst.mkdirs())
			{
				warn(src, dst, "unable to create target directory " + dst);
			}
		}
		
		// sync the content
		
		File[] sfs = (filter == null ? src.listFiles() : src.listFiles(filter));
		
		// FIX detect case-sensitivity and insensitivity
		
		File[] dfs = dst.listFiles();
		CMap<String,File> dFiles = new CMap();
		if(dfs != null)
		{
			for(File f: dfs)
			{
				String name = f.getName();
				dFiles.put(name, f);
			}
		}
		
		if(sfs != null)
		{
			for(File sf: sfs)
			{
				String name = sf.getName();
				File tf = dFiles.remove(name);
				if(tf == null)
				{
					syncPrivate(sf, new File(dst, name));
				}
				else
				{
					syncPrivate(sf, tf); 
				}
			}
			
			for(File f: dFiles.values())
			{
				if(!deleteRecursively(f))
				{
					warn(src, dst, "unable to delete destination: " + f);
				}
			}
		}
	}
}
