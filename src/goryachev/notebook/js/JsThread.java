// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.log.Log;
import goryachev.common.util.CList;
import goryachev.swing.BackgroundThread;


public abstract class JsThread
    extends BackgroundThread
{
	static Log log = Log.get("JsThread");
	private CList<Runnable> onFinish;


	public JsThread()
	{
		super("js");
	}


	protected void executeOnFinishCallbacks()
	{
		CList<Runnable> cb;
		
		synchronized(this)
		{
			cb = onFinish;
			onFinish = null;
		}
		
		if(cb != null)
		{
			for(Runnable r: cb)
			{
				try
				{
					r.run();
				}
				catch(Throwable e)
				{
					log.error(e);
				}
			}
		}
	}


	public synchronized void addOnFinishCallback(Runnable r)
	{
		if(onFinish == null)
		{
			onFinish = new CList();
		}
		onFinish.add(r);
	}
}
