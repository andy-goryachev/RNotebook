// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import java.io.File;
import java.io.FileFilter;


public class JsFileFilter
	implements FileFilter
{
	protected JsFileFilter()
	{
	}
	
	
	public static JsFileFilter parse(String spec)
	{
		return new JsFileFilter();
	}


	public boolean accept(File file)
	{
		return true;
	}
}
