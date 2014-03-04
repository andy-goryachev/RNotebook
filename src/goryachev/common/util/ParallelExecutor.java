// Copyright (c) 2012-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class ParallelExecutor
	implements ThreadFactory
{
	private String name;
	private AtomicInteger number = new AtomicInteger();
	private ThreadPoolExecutor exec;
	private boolean closed;
	
	
	public ParallelExecutor(String name)
	{
		this(name, Runtime.getRuntime().availableProcessors(), 60);
	}
	
	
	public ParallelExecutor(String name, int processCount, int keepAliveTimeSeconds)
	{
		this.name = name;
		exec = new ThreadPoolExecutor(processCount, processCount, keepAliveTimeSeconds, TimeUnit.SECONDS, new LinkedBlockingQueue(), this);
		exec.allowCoreThreadTimeOut(true);
	}
	
	
	public Thread newThread(Runnable r)
	{
		return new Thread(r, name + "." + number.getAndIncrement());
	}
	
	
	public void setKeepAliveTime(long time, TimeUnit unit)
	{
		exec.setKeepAliveTime(time, unit);
	}
	
	
	public synchronized void close()
	{
		if(!closed)
		{
			exec.shutdown();
			
			try
			{
				exec.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			}
			catch(Exception e)
			{
				Log.err(e);
			}
			closed = true;
		}
	}
	
	
	protected synchronized boolean isClosed()
	{
		return closed;
	}
	
	
	public void submit(Runnable r)
	{
		if(isClosed())
		{
			// or log it silently?
			throw new CException("CJob execution has been shut down.");
		}
		
		exec.execute(r);
	}
}
