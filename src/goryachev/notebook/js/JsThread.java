// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.util.CList;
import goryachev.common.util.Log;


public abstract class JsThread
    extends BackgroundThread
{
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
					Log.err(e);
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
