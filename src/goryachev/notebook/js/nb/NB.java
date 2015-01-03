// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.nb;
import goryachev.notebook.js.JsEngine;
import goryachev.notebook.util.InlineHelp;


public class NB
{
	public NB()
	{
	}
	
	
	public void display(Object x)
	{
		JsEngine.get().display(x);
	}
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp();
		h.a("NB :");
		h.a("NB.display(x)", "displays an object in the code output section");
		//h.a("NB.loadTable(filename)", "loads table from CSV, XLS, or XLSX file");
		return h.toString();
	}
}
