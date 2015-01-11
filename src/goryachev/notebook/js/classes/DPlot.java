// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;


public class DPlot
{
	private String title;
	
	
	public DPlot()
	{
	}

	
	public DPlot copy()
    {
	    return new DPlot();
    }
	
	
	// void makes it a setter
	public void setTitle(String s)
	{
		title = s;
	}
	
	
	public String getTitle()
	{
		return title;
	}
	
	
	// this allows for chaining, but disables the property-like access (p.title = 'a');
	public DPlot title(String s)
	{
		setTitle(s);
		return this;
	}
}
