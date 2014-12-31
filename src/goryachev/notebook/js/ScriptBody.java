// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


public class ScriptBody
{
	private static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	protected ScriptLogger logger;
	private String script;
	private Sys sys;
	
	
	public ScriptBody(ScriptLogger m, String script)
	{
		this.logger = m;
		this.script = script;
	}
	
	
	public Object process() throws Throwable
	{
		// TODO move outside somewhere
		ScriptEngine engine = scriptEngineManager.getEngineByName("JavaScript");
		sys = new Sys(engine, logger);
		
		engine.getContext().setWriter(new PrintWriter(new Writer()
		{
			public void write(char[] cbuf, int off, int len) throws IOException
			{
				logger.print(new String(cbuf,off,len));
			}

			public void close() throws IOException { }
			public void flush() throws IOException { }
		}));
	
		engine.eval("importPackage(Packages.java.lang);");

		engine.put("sys", sys);

		// run script
		Object rv = engine.eval(script);
		return rv;
	}
	
	
	public void cancel()
	{
		// TODO
	}


	public void err(Throwable e)
	{
		logger.printError(CKit.stackTrace(e));
	}


	public void log(Object x)
	{
		if(x != null)
		{
			logger.print(x.toString());
		}
	}
	
	
	public void log(Object ... xs)
	{
		SB sb = new SB();
	
		boolean space = false;
		for(Object x: xs)
		{
			if(x != null)
			{
				if(space)
				{
					sb.append(' ');
				}
				else
				{
					space = true;
				}
			
				sb.append(x);
			}
		}
		
		log(sb);
	}
}
