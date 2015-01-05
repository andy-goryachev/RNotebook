// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.ut;
import goryachev.notebook.util.InlineHelp;


public class UT
{
	public UT()
	{
	}
	
	
	public void sleep(long ms) throws Exception
	{
		Thread.sleep(ms);
	}
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp("UT");
		h.a("provides network-related functions:");
		h.a("sleep(ms)", "sleeps for the specified number of milliseconds");
		return h.toString();
	}
}
