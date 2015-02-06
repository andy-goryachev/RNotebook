// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.ui.UI;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.notebook.cell.CodePanel;
import goryachev.notebook.cell.NotebookPanel;
import goryachev.notebook.cell.Results;
import java.util.concurrent.atomic.AtomicInteger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


public class JsEngine
{
	private final NotebookPanel np;
	protected ScriptableObject scope;
	protected GlobalScope globalScope;
	private AtomicInteger sequence = new AtomicInteger(1);
	private volatile JsThread thread;
	private SB log = new SB();
	protected static final ThreadLocal<JsEngine> engineRef = new ThreadLocal();
	protected static final ThreadLocal<CodePanel> codePanelRef = new ThreadLocal();
	
	
	public JsEngine(NotebookPanel np)
	{
		this.np = np;
	}
	
	
	protected synchronized Scriptable scope(Context cx) throws Exception
	{
		if(scope == null)
		{
			scope = new GlobalScope(cx);
		}
		
		return scope;
	}

	
	public void setSequence(int x)
	{
		sequence.set(x);		
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
		Object v = Results.copyValue(x);
		CodePanel p = codePanelRef.get();
		
		if(v instanceof Object[])
		{
			for(Object item: (Object[])v)
			{
				if(item != null)
				{
					displayPrivate(p, item);
				}
			}
		}
		else
		{
			displayPrivate(p, v);
		}
	}
	
	
	protected void displayPrivate(final CodePanel p, final Object v)
	{
		// make sure to show text logged so far
		final String text;
		if(log.isNotEmpty())
		{
			text = log.getAndClear();
		}
		else
		{
			text = null;
		}

		UI.inEDTW(new Runnable()
		{
			public void run()
			{
				if(text != null)
				{
					p.addResult(text);
				}
				
				p.addResult(v);
			}
		});
	}
	
	
	public void execute(final CodePanel p)
	{
		p.setRunning(true);

		final String script = p.getText();

		thread = new JsThread()
		{
			public void process() throws Throwable
			{
				Context cx = Context.enter();
				
				// set interpreted mode so we can stick interruption check in Interpreter.interpretLoop()
				cx.setOptimizationLevel(-1);
				
				try
				{
					engineRef.set(JsEngine.this);
					codePanelRef.set(p);
					
					// "line " produces an error message like "line #5"
					Object rv = cx.evaluateString(scope(cx), script, "line ", 1, null);
					display(rv);
				}
				finally
				{
					codePanelRef.set(null);
					Context.exit();
					
					executeOnFinishCallbacks();
				}
			}
			
			public void success()
			{
				finished(p);
			}

			public void onError(Throwable e)
			{
				displayPrivate(p, new JsError(JsUtil.decodeException(e)));
				finished(p);
			}
		};
		thread.start();
	}
	

	public void addOnFinishCallback(Runnable r)
	{
		JsThread t = thread;
		if(t != null)
		{
			t.addOnFinishCallback(r);
		}
	}
	
	
	protected void finished(CodePanel p)
	{
		thread = null;
		
		int count = sequence.getAndIncrement();  
		p.setFinished(count);
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
		return engineRef.get();
	}
}
