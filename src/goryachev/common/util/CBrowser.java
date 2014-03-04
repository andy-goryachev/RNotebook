// Copyright (c) 2012-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;


public class CBrowser
{
	public static void openLink(String uri) throws Exception
	{
		openLink(new URI(uri));
	}
	
	
	public static void openLinkQuiet(String uri)
	{
		try
		{
			openLink(uri);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}
	
	
	public static void openLink(URL url) throws Exception
	{
		openLink(url.toURI());
	}


	public static void openLinkQuiet(URL url)
	{
		try
		{
			openLink(url);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}


	protected static void openLink(URI uri) throws Exception
	{
		Desktop.getDesktop().browse(uri);
	}
}
