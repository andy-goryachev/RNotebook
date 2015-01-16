// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package research.tools.filesync;
import goryachev.common.util.CKit;
import goryachev.common.util.CMap;
import goryachev.common.util.FileTools;
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
		public void deleted(File f);
		
		public void copied(File f);
		
		public void error(Throwable e);
	}
	
	//
	
	private File source;
	private File target;
	private FileFilter filter;
	private int granularity;
	private boolean ignoreFailures;
	private SB warnings;
	private Listener listener;
	
	
	public FileSyncTool()
	{
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
	
	
	public void setListener(Listener li)
	{
		listener = li;
	}
	
	
	public void setGranularity(int ms)
	{
		granularity = ms;
	}


	public void setIgnoreFailures(boolean on)
	{
		ignoreFailures = on;
	}


	public String getReport()
	{
		return warnings == null ? "" : warnings.toString();
	}
	
	
	protected void warn(String msg)
	{
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
		
		syncPrivate(source, target);
	}


	protected void syncPrivate(File src, File dst) throws Exception
	{
		CKit.checkCancelled();
		
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
			warn("don't know how to sync " + src);
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
		
		// TODO also check attributes?
		
		return true;
	}
	
	
	protected void syncFile(File src, File dst) throws Exception
	{
		if(dst.exists())
		{
			if(dst.isFile())
			{
				if(isUnchanged(src, dst))
				{
					// no need to change
					return;
				}
			}
			else
			{
				if(FileTools.deleteRecursively(dst))
				{
					if(listener != null)
					{
						listener.deleted(dst);
					}
				}
				else
				{
					warn("unable to delete " + dst);
					return;
				}
			}
		}
		
		// override and copy attributes
		// FIX does not allow progress listener or interruption
		try
		{
			Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
			
			if(listener != null)
			{
				listener.copied(dst);
			}
		}
		catch(Exception e)
		{
			warn("failed to copy file " + src + " to " + dst);
		}
	}
	
	
	protected void syncDirectory(File src, File dst) throws Exception
	{
		boolean create;
		if(dst.exists())
		{
			if(!dst.isDirectory())
			{
				if(FileTools.deleteRecursively(dst))
				{
					if(listener != null)
					{
						listener.deleted(dst);
					}
				}
				else
				{
					warn("unable to delete target " + dst);
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
				warn("unable to create target directory " + dst);
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
				if(FileTools.deleteRecursively(f))
				{
					if(listener != null)
					{
						listener.deleted(f);
					}
				}
				else
				{
					warn("unable to delete destination: " + f);
				}
			}
		}
	}
}
