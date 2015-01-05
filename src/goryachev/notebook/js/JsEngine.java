// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.notebook.cell.CodePanel;
import goryachev.notebook.cell.NotebookPanel;
import goryachev.notebook.cell.Results;
import goryachev.notebook.js.fs.FS;
import goryachev.notebook.js.io.IO;
import goryachev.notebook.js.nb.NB;
import java.util.concurrent.atomic.AtomicInteger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


public class JsEngine
{
	public static final String SOURCE = "line ";
	private final NotebookPanel np;
	protected ScriptableObject scope;
	protected GlobalScope globalScope;
	private AtomicInteger runCount = new AtomicInteger(1);
	private BackgroundThread thread;
	private SB log = new SB();
	private CList<Object> results;
	protected static final ThreadLocal<JsEngine> engine = new ThreadLocal();
	
	
	public JsEngine(NotebookPanel np)
	{
		this.np = np;
	}
	
	
	protected synchronized Scriptable scope(Context cx) throws Exception
	{
		if(scope == null)
		{
			scope = new GlobalScope(cx);
			
			// top level objects
			ScriptableObject.putProperty(scope, "FS", new FS());
			ScriptableObject.putProperty(scope, "IO", new IO());
			ScriptableObject.putProperty(scope, "NB", new NB());
			
			// java integration
			cx.evaluateString(scope, "importPackage(java.lang);", "INIT", 1, null);
		}
		
		return scope;
	}
	
	
	@Deprecated // for debugging
	public GlobalScope getGlobalScope()
	{
		return globalScope;
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
	
	
	public synchronized void display(Object x)
	{
		if(log.isNotEmpty())
		{
			results.add(log.getAndClear());
		}
		
		Object v = Results.copyValue(x);
		results.add(v);
	}
	
	
	public void execute(final CodePanel p)
	{
		p.setRunning();
		results = new CList();

		final String script = p.getText();

		thread = new BackgroundThread("js")
		{
			public void process() throws Throwable
			{
				Context cx = Context.enter();
				
				// set interpreted mode so we can stick interruption check in Interpreter.interpretLoop()
				cx.setOptimizationLevel(-1);
				
				try
				{
					engine.set(JsEngine.this);
					
					Object rv = cx.evaluateString(scope(cx), script, SOURCE, 1, null);
					if(rv == Context.getUndefinedValue())
					{
						rv = null;
					}
					
					display(rv);
				}
				finally
				{
					Context.exit();
				}
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
	
	
	public static JsEngine get()
	{
		return engine.get();
	}
}
