// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.ut;
import goryachev.common.util.HSLColor;
import goryachev.notebook.util.InlineHelp;
import java.awt.Color;


public class UT
{
	public UT()
	{
	}
	
	
	public void sleep(long ms) throws Exception
	{
		Thread.sleep(ms);
	}
	
	
	public Color hslColor(float hue, float sat, float lum)
	{
		return HSLColor.toColor(hue, sat, lum);
	}
	
	
	public String toString()
	{
		return "UT";
	}
	
	
	public InlineHelp getHelp()
	{
		InlineHelp h = new InlineHelp("UT");
		h.a("UT provides network-related functions:");
		//
		h.a("hslColor(hue,saturation,luminocity)", "create color from HSL values");
		h.a("sleep(ms)", "sleeps for the specified number of milliseconds");
		return h;
	}
}
