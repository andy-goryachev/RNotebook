// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.io;
import goryachev.notebook.util.InlineHelp;


public class OS
{
	public OS()
	{
	}
	
	
	public int getCpuCount()
	{
		return Runtime.getRuntime().availableProcessors();
	}
	
	
	public long getTime()
	{
		return System.currentTimeMillis();
	}
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp();
		h.a("OS provides access to the following system functions:");
		h.a("OS.cpuCount", "current time in milliseconds");
		h.a("OS.time", "current time in milliseconds");
		return h.toString();
	}
}
