// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.os;


public class ProcessMonitor
{
	private final Process process;
	private final ProcessMonitorThread stdout;
	private final ProcessMonitorThread stderr;
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
		running = on;
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
