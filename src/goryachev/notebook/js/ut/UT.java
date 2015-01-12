// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.ut;
import goryachev.common.util.HSLColor;
import goryachev.notebook.util.InlineHelp;
import java.awt.Color;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


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
	
	
	public Document parseHtml(Object x)
	{
		return Jsoup.parse(x.toString());
	}
	
	
	public String toString()
	{
		return "UT";
	}
	
	
	public InlineHelp getHelp()
	{
		InlineHelp h = new InlineHelp("UT");
		h.a("UT offers helpful utility functions:");
		//
		h.a("hslColor(hue,saturation,luminocity)", "create color from HSL values");
		h.a("parse(html)", "parse HTML document");
		h.a("sleep(ms)", "sleeps for the specified number of milliseconds");
		return h;
	}
}
