// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.os;
import goryachev.notebook.js.JsThread;


public class ProcessMonitor
{
	public final Process process;
	public final ProcessMonitorThread stdout;
	public final ProcessMonitorThread stderr;
	protected volatile boolean running = true;
	
	
	public ProcessMonitor(Process p, boolean capture)
	{
		process = p;
		stdout = new ProcessMonitorThread(this, "stdout", capture, p.getInputStream());
		stderr = new ProcessMonitorThread(this, "stderr", capture, p.getErrorStream());

		// make sure to destroy the process on interruption
		JsThread t = (JsThread)Thread.currentThread();
		t.addOnFinishCallback(new Runnable()
		{
			public void run()
			{
				process.destroy();
			}
		});
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
		return running;
	}
	
	
	public String toString()
	{
		return "stdout: " + stdout.getBuffer() + "\n\nstderr: " + stderr.getBuffer();
	}
}
