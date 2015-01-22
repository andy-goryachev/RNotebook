// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.nb;
import goryachev.notebook.RNotebookApp;
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
	
	
	public void setValue(String key, String val)
	{
		RNotebookApp.getStorage().setValue(key, val);
	}
	
	
	public String getValue(String key)
	{
		return RNotebookApp.getStorage().getValue(key);
	}
	
	
	public String toString()
	{
		return "NB";
	}
	
	
	public InlineHelp getHelp()
	{
		InlineHelp h = new InlineHelp("NB");
		h.a("NB provides operations with the notebook application:");
		//
		h.a("NB.display(x)", "displays an object in the code output section");
		h.a("NB.getValue(key)", "returns a string value from the notebook storage");
		h.a("NB.setValue(key, val)", "stores a string value in the notebook storage");
		return h;
	}
}
