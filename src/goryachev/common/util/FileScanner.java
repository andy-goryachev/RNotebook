// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.File;


// TODO filter
// TODO multi-core
public abstract class FileScanner
{
	protected abstract void processFile(File f) throws Exception;
	
	//
	
	private CList<File> files = new CList();
	private CMap<File,Object> scanned;
	private boolean sort;
	
	
	public FileScanner()
	{
	}
	
	
	public void addFolder(String filename)
	{
		addFolder(new File(filename));
	}
	
	
	public void setSort(boolean on)
	{
		sort = on;
	}
	
	
	public void addFolder(File f)
	{
		files.add(f);
	}
	
	
	public void scan() throws Exception
	{
		scanned = new CMap();
		for(File folder: files)
		{
			scanFile(folder);
		}
	}
	
	
	protected void scanFile(File file) throws Exception
	{
		if(scanned.put(file, null) != null)
		{
			return;
		}
		
		if(file.isFile())
		{
			processFile(file);
		}
		else if(file.isDirectory())
		{
			File[] fs = file.listFiles();
			if(fs != null)
			{
				if(sort)
				{
					new CComparator<File>()
					{
						public int compare(File a, File b)
						{
							return compareText(a.getName(), b.getName());
						}
					}.sort(fs);
				}
				
				for(File f: fs)
				{
					scanFile(f);
				}
			}
		}
	}
}
