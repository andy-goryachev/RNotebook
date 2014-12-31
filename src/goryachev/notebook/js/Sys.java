// Copyright (c) 2010-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.util.CKit;
import goryachev.common.util.D;
import goryachev.common.util.SB;
import java.io.File;
import javax.script.ScriptEngine;


// aliased to "sys" variable in the global context
public class Sys
{
	private ScriptEngine engine;
	private ScriptLogger logger;
	
	
	public Sys(ScriptEngine engine, ScriptLogger logger)
	{
		this.engine = engine;
		this.logger = logger;
	}
	
	
	public void print(String s)
	{
		logger.print(s);
	}
	
	
	// FIX varargs does not work in javascript
	// TODO add function to the global content instead
	public void print(Object ... xs)
	{
		SB sb = new SB();
		
		boolean space = false;
		for(Object x: xs)
		{
			if(space)
			{
				sb.append(' ');
			}
			else
			{
				space = true;	
			}
			
			if(x != null)
			{
				sb.append(x);
			}
		}
		
		logger.print(sb.toString());
	}


	public void exit()
	{
		throw new ProcessExitedException();
	}


	public void test(Object x)
	{
		D.print(x);
	}
	
	
	// run script in the same context
	public Object run(String path) throws Exception
	{
		File f = new File(path);
		String script = CKit.readString(f);
		return engine.eval(script);
	}
	
	
	// create new string builder object
	public SB sb()
	{
		return new SB();
	}
}
