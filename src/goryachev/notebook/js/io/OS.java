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
	
	
	public long getTotalMem()
	{
		return Runtime.getRuntime().totalMemory();
	}
	
	
	public long getFreeMem()
	{
		return Runtime.getRuntime().freeMemory();
	}
	
	
	public long getMaxMem()
	{
		return Runtime.getRuntime().maxMemory();
	}
	
	
	public long getUsedMem()
	{
		Runtime r = Runtime.getRuntime();
		return r.totalMemory() - r.freeMemory();
	}
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp("OS");
		h.a("OS provides access to the following system functions:");
		h.a("cpuCount", "current time in milliseconds");
		h.a("time", "current time in milliseconds");
		h.a("freeMem", "returns the amount of free memory in the JVM");
		h.a("maxMem", "returns the maximum amount of memory that the JVM will attempt to use.");
		h.a("usedMem", "returns the amount of memory currently being used");
		h.a("totalMem", "returns the total amount of memory in the JVM");
		return h.toString();
	}
}
