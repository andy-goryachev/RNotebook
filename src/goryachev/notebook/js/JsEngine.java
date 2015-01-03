// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.notebook.cell.CodePanel;
import goryachev.notebook.js.fs.FS;
import goryachev.notebook.js.io.IO;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicInteger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


public class JsEngine
{
	private static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	private ScriptEngine engine;
	private AtomicInteger runCount = new AtomicInteger(1);
	private BackgroundThread thread;
	private SB log = new SB();
	private CList<Object> results = new CList();
	
	
	public JsEngine()
	{
	}
	
	
	protected synchronized ScriptEngine engine() throws Exception
	{
		if(engine == null)
		{
			engine = scriptEngineManager.getEngineByName("JavaScript");
			
			engine.getContext().setWriter(new PrintWriter(new Writer()
			{
				public void write(char[] cbuf, int off, int len) throws IOException
				{
					print(new String(cbuf,off,len));
				}

				public void close() throws IOException { }
				public void flush() throws IOException { }
			}));
		
			// global context objects
			engine.put("IO", new IO());
			engine.put("FS", new FS());
			
			// common packages
			engine.eval("importPackage(Packages.java.lang);");
		}
		return engine;
	}
	
	
	protected synchronized void print(String s)
	{
		if(log.isNotEmpty())
		{
			log.nl();
		}
		
		// TODO check for full buffer and append message "Too many lines" 
		
		log.append(s);
	}
	
	
	protected synchronized void display(Object x)
	{
		if(log.isNotEmpty())
		{
			results.add(log.getAndClear());
		}
		
		results.add(x);
	}
	
	
	public void execute(final CodePanel p)
	{
		p.setRunning();
		results.clear();

		final String script = p.getText();

		thread = new BackgroundThread("js")
		{
			public void process() throws Throwable
			{
				Object rv = engine().eval(script);
				display(rv);
			}
			
			public void success()
			{
				finished(p);
			}

			public void onError(Throwable e)
			{
				display(e);
				finished(p);
			}
		};
		thread.start();
	}
	
	
	protected void finished(CodePanel p)
	{
		thread = null;
		
		int count = runCount.getAndIncrement();  
		p.setResult(count, results);
	}
	
	
	public void execute(CList<CodePanel> ps)
	{
		// TODO
	}
	
	
	public void interrupt()
	{
		BackgroundThread t = thread;
		if(t != null)
		{
			t.cancel();
		}
	}
	
	
	public boolean isRunning()
	{
		return (thread != null);
	}
}
