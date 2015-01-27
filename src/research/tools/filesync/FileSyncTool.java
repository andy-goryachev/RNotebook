// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.tools.filesync;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
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
		/** when sync process starts and ends */
		public void handleSyncRunning(FileSyncTool parent, boolean on);
		
		/** when sync process target directory changes */
		public void handleSyncTarget(File f);
		
		/** when an error is encountered */
		public void handleSyncWarning(File src, File dst, String err);
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
	
	protected static class Job
	{
		public File source;
		public File target;
		public RFileFilter filter;
	}
	
	//
	
	private CList<Job> jobs = new CList();
	private File commonTarget;
	private RFileFilter commonFilter;
	private int granularity;
	private boolean ignoreFailures;
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
			public void handleSyncWarning(File src, File dst, String err) { }
			public void handleSyncTarget(File f) { }
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
	
	
	public void addSource(File source)
	{
		addJob(source, null, null);
	}
	
	
	public void addSource(File source, RFileFilter filter)
	{
		addJob(source, null, filter);
	}
	
	
	public void addJob(File source, File target)
	{
		addJob(source, target, null);
	}
	
	
	public void addJob(File source, File target, RFileFilter filter)
	{
		Job j = new Job();
		j.source = source;
		j.target = target;
		j.filter = filter;
		jobs.add(j);
	}

	
	public void setTarget(File f)
    {
		commonTarget = f;
    }
	
	
	public void setFileFilter(RFileFilter ff)
	{
		commonFilter = ff;
	}
	
	
	public void setGranularity(int ms)
	{
		granularity = ms;
	}
	
	
	public int getGranularity()
	{
		return granularity;
	}


	public void setIgnoreFailures(boolean on)
	{
		ignoreFailures = on;
	}
	
	
	public boolean isIgnoreFailures()
	{
		return ignoreFailures;
	}

	
	protected void warn(File src, File dst, String msg)
	{
		listener.handleSyncWarning(src, dst, msg);
		
		if(!ignoreFailures)
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
	
	
	protected File getTargetFor(Job j)
	{
		if(j.target == null)
		{
			return new File(commonTarget, j.source.getName());
		}
		else
		{
			return j.target;
		}
	}
	
	
	protected FileFilter getFilterFor(Job j)
	{
		RFileFilter f = (j.filter == null ? commonFilter : j.filter);
		return f.getFilter(j.source);
	}


	public void sync() throws Exception
	{
		if(jobs.size() == 0)
		{
			throw new UserException("No source directories.");
		}
		
		for(Job j: jobs)
		{
			File f = getTargetFor(j);
			if(f == null)
			{
				throw new UserException("No target is specified for " + j.source);
			}
			
			if(f.exists())
			{
				if(!f.isDirectory())
				{
					throw new UserException("Target is not a directory: " + f);
				}
			}
		}
		
		// sync
		listener.handleSyncRunning(this, true);		
		try
		{
			for(Job j: jobs)
			{
				File f = getTargetFor(j);
				FileFilter filter = getFilterFor(j);
				
				listener.handleSyncTarget(f);
				
				syncPrivate(j.source, f, filter);
			}
		}
		finally
		{
			listener.handleSyncRunning(this, false);
		}
	}


	protected void syncPrivate(File src, File dst, FileFilter filter) throws Exception
	{
		currentFile = src;
		currentTarget = dst;

		CKit.checkCancelled();
				
		if(src.isFile())
		{
			syncFile(src, dst);
		}
		else if(src.isDirectory())
		{
			syncDirectory(src, dst, filter);
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
	
	
	protected void syncDirectory(File src, File dst, FileFilter filter) throws Exception
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
		CMap<String,File> toBeDeleted = new CMap();
		if(dfs != null)
		{
			for(File f: dfs)
			{
				String name = f.getName();
				toBeDeleted.put(name, f);
			}
		}
		
		if(sfs != null)
		{
			for(File sf: sfs)
			{
				String name = sf.getName();
				File tf = toBeDeleted.remove(name);
				if(tf == null)
				{
					syncPrivate(sf, new File(dst, name), filter);
				}
				else
				{
					syncPrivate(sf, tf, filter); 
				}
			}
			
			currentFile = src;
			currentTarget = dst;
			
			for(File f: toBeDeleted.values())
			{
				if(!deleteRecursively(f))
				{
					warn(src, dst, "unable to delete destination: " + f);
				}
			}
		}
	}
}
