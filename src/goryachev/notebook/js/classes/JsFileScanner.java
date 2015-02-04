// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;
import goryachev.common.util.FileScanner;
import goryachev.common.util.Noobfuscate;
import goryachev.common.util.RFileFilter;
import goryachev.notebook.js.JsUtil;
import java.io.File;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.InterpretedFunction;
import org.mozilla.javascript.Scriptable;


@Noobfuscate
public class JsFileScanner
{
	private final FileScanner scanner;
	private InterpretedFunction func;
	
	
	public JsFileScanner()
	{
		scanner = new FileScanner()
		{
			protected void processFile(File f) throws Exception
			{
				handleFile(f);
			}
		};
	}
	
	
	protected void handleFile(File f)
	{
		Context cx = Context.getCurrentContext();
		// TODO probably needs these two
		Scriptable scope = null;
		Scriptable thisObj = null;
		func.call(cx, scope, thisObj, new Object[] { f });
	}
	
	
	public void addFolder(Object folder)
	{
		File f = JsUtil.parseFile(folder);
		scanner.addFolder(f);
	}
	
	
	public void addFolder(Object folder, Object filter) throws Exception
	{
		File f = JsUtil.parseFile(folder);
		RFileFilter ff = JsUtil.parseRFileFilter(filter);
		scanner.addFolder(f, ff);
	}
	
	
	public void setFileFilter(Object filter) throws Exception
	{
		RFileFilter ff = JsUtil.parseRFileFilter(filter);
		scanner.setFileFilter(ff);
	}
	
	
	public void setHandler(InterpretedFunction func)
	{
		this.func = func;
	}
	
	
	public void scan() throws Exception
	{
		scanner.scan();
	}
}
