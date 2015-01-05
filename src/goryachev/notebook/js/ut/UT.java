// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.ut;
import goryachev.notebook.util.InlineHelp;


public class UT
{
	public UT()
	{
	}
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp();
		h.a("UT provides network-related functions:");
		h.a("UT.sleep(ms)", "sleeps for a specified period in milliseconds");
		return h.toString();
	}
}
