// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.os;

import goryachev.common.util.CKit;


public class ProcessMonitor
{
	private final Process process;
	public final ProcessMonitorThread stdout;
	public final ProcessMonitorThread stderr;
	private Thread caller;
	protected volatile boolean running = true;
	
	
	public ProcessMonitor(Process p, boolean capture)
	{
		process = p;
		stdout = new ProcessMonitorThread(this, "stdout", capture, p.getInputStream());
		stderr = new ProcessMonitorThread(this, "stderr", capture, p.getErrorStream());
		this.caller = Thread.currentThread();
	}
	
	
	public void start() throws Exception
	{
		stdout.start();
		stderr.start();
	}
	
	
	public void setRunning(boolean on)
	{
		if(running != on)
		{
			if(!on)
			{
				process.destroy();
			}
			
			running = on;
		}
	}
	
	
	public boolean isRunning()
	{
		// FIX incorrect: need to start another thread to poll the status
		return running && !CKit.isCancelled(caller);
	}
	
	
	public String toString()
	{
		return "stdout: " + stdout.getBuffer() + "\n\nstderr: " + stderr.getBuffer();
	}
}
