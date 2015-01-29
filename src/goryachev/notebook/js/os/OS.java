// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.os;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.InlineHelp;
import java.net.InetAddress;


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
	
	
	public String getHostName() throws Exception
	{
		return InetAddress.getLocalHost().getHostName();
	}
	
	
	public String getName()
	{
		return System.getProperty("os.name");
	}
	
	
	public String getVersion()
	{
		return System.getProperty("os.version");
	}
	
	
	public String getArch()
	{
		return System.getProperty("os.arch");
	}
	
	
	public String getUserName()
	{
		return System.getProperty("user.name");
	}
	
	
	public String toString()
	{
		return "OS";
	}
	
	
	public void launch(Object ... args) throws Exception
	{
		String[] cmd = JsUtil.parseStringArray(args);
		Runtime.getRuntime().exec(cmd);
	}
	
	
	public int exec(Object ... args) throws Exception
	{
		String[] cmd = JsUtil.parseStringArray(args);
		Process p = Runtime.getRuntime().exec(cmd);
		return p.waitFor();
	}
	

	public InlineHelp getHelp()
	{
		InlineHelp h = new InlineHelp("OS");
		h.a("OS provides access to the following system functions:");
		//
		h.a("arch", "returns the OS architecture");
		h.a("cpuCount", "returns number of processor cores");
		h.a("exec(cmd, [arg], ...)", "executes an external executable, wait for it to finish");
		h.a("freeMem", "returns the amount of free memory in the JVM");
		h.a("hostName", "returns the host name");
		h.a("launch(cmd, [arg], ...)", "executes an external executable without waiting for it to finish");
		h.a("maxMem", "returns the maximum amount of memory that the JVM will attempt to use");
		h.a("name", "returns the OS name");
		h.a("time", "current time in milliseconds");
		h.a("totalMem", "returns the total amount of memory in the JVM");
		h.a("usedMem", "returns the amount of memory currently being used");
		h.a("userName", "returns the user name");
		h.a("version", "returns the OS version");
		return h;
	}
}
