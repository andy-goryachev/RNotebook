// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.util.CKit;
import goryachev.common.util.CUnique;
import goryachev.common.util.D;
import goryachev.common.util.SB;
import goryachev.common.util.UserException;
import java.io.File;


// https://ant.apache.org/manual/Tasks/sync.html
public class FileSyncTool
{
	private File source;
	private File target;
	private boolean includeEmptyDirs;
	private int granularity;
	private boolean ignoreFailures;
	private SB warnings;
	
	
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
	
	
	public void setFileFilter(Object ff)
	{
		// TODO
	}
	
	
	public void setListener(Object li)
	{
		// TODO
		D.print(li);
	}
	
	
	public void setGranularity(int ms)
	{
		granularity = ms;
	}


	public void setIncludeEmptyDirs(boolean on)
	{
		includeEmptyDirs = on;
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
		else
		{
			if(!target.mkdirs())
			{
				throw new UserException("Failed to create the target directory: " + target);
			}
		}
		
		syncPrivate(source, target);
	}


	protected void syncPrivate(File src, File dst)
	{
//		CKit.checkCancelled();
//		
//		if(src.isFile())
//		{
//			if(dst.exists())
//			{
//				if(dst.isFile())
//				{
//					long diff = Math.abs(src.lastModified() - dst.lastModified());
//					if(diff > granularity)
//					{
//						// overwrite!
//						copyFile(src, dst);
//					}
//					
//					// done with the file
//					return;
//				}
//				
//				delete(dst);
//				copyFile(src, dst);
//			}
//			syncFile(src, dst);
//		}
//		else if(src.isDirectory())
//		{
//			boolean create;
//			if(dst.exists())
//			{
//				if(!dst.isDirectory())
//				{
//					if(dst.delete() == false)
//					{
//						warn("unable to delete target " + dst);
//					}
//					
//					create = true;
//				}
//				else
//				{
//					create = false;
//				}
//			}
//			else
//			{
//				create = true;
//			}
//			
//			if(create)
//			{
//				if(dst.mkdirs() == false)
//				{
//					warn("unable to create target directory " + dst);
//				}
//			}
//			
//			// sync folders
//			syncFolders(src, dst);
//		}
//		else
//		{
//			warn("don't know how to sync " + src);
//		}
	}
	
	
	protected void syncFile(File src, File dst)
	{
		
	}
	
	
//	protected void syncFolders(File src, File dst)
//	{
//		// TODO filter
//		File[] sfs = src.listFiles();
//		
//		// FIX detect case-sensitivity and insensitivity
//		
//		File[] dfs = dst.listFiles();
//		CUnique<File> udf = new CUnique(dfs);
//		
//		if(sfs != null)
//		{
//			for(File sf: sfs)
//			{
//				// FIX by name
//				File tf = udf.remove(sf);
//				if(tf == null)
//				{
//					// create
//				}
//				else
//				{
//					syncPrivate(sf, tf); 
//				}
//			}
//		}
//	}
}
