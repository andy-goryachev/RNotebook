// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.nb;
import goryachev.notebook.RNotebookApp;
import goryachev.notebook.js.JsEngine;
import goryachev.notebook.js.JsObjects;
import goryachev.notebook.util.Arg;
import goryachev.notebook.util.Doc;
import goryachev.notebook.util.InlineHelp;


@Doc("provides operations with the notebook application:")
public class NB
{
	public NB()
	{
	}
	
	
	@Doc("displays an object in the code output section")
	@Arg("x")
	public void display(Object x)
	{
		JsEngine.get().display(x);
	}
	
	
	@Doc("stores a string value in the notebook storage")
	@Arg({"key", "value"})
	public void setValue(String key, String val)
	{
		RNotebookApp.getStorage().setValue(key, val);
	}
	
	
	@Doc("returns a string value from the notebook storage")
	@Arg({"key"})
	public String getValue(String key)
	{
		return RNotebookApp.getStorage().getValue(key);
	}
	
	
	public String toString()
	{
		return getHelp().toString();
	}
	
	
	public InlineHelp getHelp()
	{
		return InlineHelp.create(JsObjects.NB, getClass());
	}
}
