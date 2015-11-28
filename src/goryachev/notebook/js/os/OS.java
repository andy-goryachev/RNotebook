// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.os;
import goryachev.notebook.js.JsObjects;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.Arg;
import goryachev.notebook.util.Doc;
import goryachev.notebook.util.InlineHelp;
import java.net.InetAddress;


@Doc("provides access to the following system functions")
public class OS
{
	public OS()
	{
	}
	
	
	@Doc("returns number of processor cores")
	@Arg("")
	public int getCpuCount()
	{
		return Runtime.getRuntime().availableProcessors();
	}
	
	
	@Doc("current time in milliseconds")
	@Arg("")
	public long getTime()
	{
		return System.currentTimeMillis();
	}
	
	
	@Doc("returns the total amount of memory in the JVM")
	@Arg("")
	public long getTotalMem()
	{
		return Runtime.getRuntime().totalMemory();
	}
	
	
	@Doc("returns the amount of free memory in the JVM")
	@Arg("")
	public long getFreeMem()
	{
		return Runtime.getRuntime().freeMemory();
	}
	
	
	@Doc("returns the maximum amount of memory that the JVM will attempt to use")
	@Arg("")
	public long getMaxMem()
	{
		return Runtime.getRuntime().maxMemory();
	}
	
	
	@Doc("returns the amount of memory currently being used")
	@Arg("")
	public long getUsedMem()
	{
		Runtime r = Runtime.getRuntime();
		return r.totalMemory() - r.freeMemory();
	}
	
	
	@Doc("returns the host name")
	@Arg("")
	public String getHostName() throws Exception
	{
		return InetAddress.getLocalHost().getHostName();
	}
	
	
	@Doc("returns the OS name")
	@Arg("")
	public String getName()
	{
		return System.getProperty("os.name");
	}
	
	
	@Doc("returns the OS version")
	@Arg("")
	public String getVersion()
	{
		return System.getProperty("os.version");
	}
	
	
	@Doc("returns the OS architecture")
	@Arg("")
	public String getArch()
	{
		return System.getProperty("os.arch");
	}
	
	
	@Doc("returns the user name")
	@Arg("")
	public String getUserName()
	{
		return System.getProperty("user.name");
	}
	
	
	public String toString()
	{
		return getHelp().toString();
	}
	
	
	@Doc("launches an external command")
	@Arg({"cmd", "[arg]", "..."})
	public void launch(Object ... args) throws Exception
	{
		String[] cmd = JsUtil.parseStringArray(args);
		Process p = Runtime.getRuntime().exec(cmd);
		
		// consume stdout, stderr
		new ProcessMonitor(p, false).start();
	}
	
	
	@Doc("executes an external executable, waits for it to finish")
	@Arg({"cmd", "[arg]", "..."})
	public Object exec(Object ... args) throws Exception
	{
		String[] cmd = JsUtil.parseStringArray(args);
		Process p = Runtime.getRuntime().exec(cmd);

		// capture stdout, stderr
		ProcessMonitor m = new ProcessMonitor(p, true);
		m.start();
		p.waitFor();
		return m;
	}
	

	public InlineHelp getHelp()
	{
		return InlineHelp.create(JsObjects.OS, getClass());
	}
}
