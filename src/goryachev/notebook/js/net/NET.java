// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.net;
import goryachev.notebook.util.InlineHelp;


public class NET
{
	public NET()
	{
	}
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp();
		h.a("NET provides network-related functions:");
		h.a("NET.get(url)", "returns content specified by the URL");
		return h.toString();
	}
}
