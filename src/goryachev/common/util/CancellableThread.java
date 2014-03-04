// Copyright (c) 2012-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class CancellableThread
	extends Thread
	implements Cancellable
{
	private volatile boolean cancelled;

	
	public CancellableThread()
	{
	}
	
	
	public CancellableThread(String name)
	{
		super(name);
	}
	
	
	public CancellableThread(Runnable target)
	{
		super(target);
	}
	
	
	public CancellableThread(Runnable target, String name)
	{
		super(target, name);
	}
	
	
	public void cancel()
	{
		cancelled = true;
		interrupt();
	}
	
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	
	public boolean isRunning()
	{
		return !cancelled;
	}
	
	
	public void checkCancelled() throws CancelledException
	{
		if(cancelled)
		{
			throw new CancelledException();
		}
	}
}
