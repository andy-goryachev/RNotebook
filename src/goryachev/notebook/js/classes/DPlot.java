// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;


public class DPlot
{
	private String title;
	private int padding = 10;
	
	
	public DPlot()
	{
	}

	
	public DPlot copy()
    {
		DPlot p = new DPlot();
		p.setTitle(title);
		return p;
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
	
	
	public void setPadding(int x)
	{
		padding = x;
	}
	
	
	public int getPadding()
	{
		return padding;
	}
	
	
	// this allows for chaining, but disables the property-like access (p.title = 'a');
	public DPlot title(String s)
	{
		setTitle(s);
		return this;
	}
}
