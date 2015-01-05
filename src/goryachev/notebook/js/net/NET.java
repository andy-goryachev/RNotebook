// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.net;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.InlineHelp;


public class NET
{
	public NET()
	{
	}
	
	
	public Object get(Object url) throws Exception
	{
		throw JsUtil.todo();
	}
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp("NET");
		h.a("provides network-related functions:");
		h.a("get(url)", "returns content specified by the URL");
		return h.toString();
	}
}
