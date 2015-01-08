// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.net;
import goryachev.common.ui.Application;
import goryachev.common.util.CBrowser;
import goryachev.common.util.CKit;
import goryachev.common.util.TextTools;
import goryachev.common.util.UserException;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.InlineHelp;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import javax.imageio.ImageIO;


public class NET
{
	public NET()
	{
	}
	
	
	public Object get(Object url) throws Exception
	{
		URL u = JsUtil.parseURL(url);
		URLConnection c = u.openConnection();
		c.setRequestProperty("User-Agent", Application.getTitle() + " " + Application.getVersion());
		
		try
		{
			if(c instanceof HttpURLConnection)
			{
				HttpURLConnection ch = (HttpURLConnection)c;
				String type = ch.getContentType();
				
				if(type != null)
				{
					if(TextTools.startsWithIgnoreCase(type, "text/"))
					{
						return readText(ch);
					}
					else if(TextTools.startsWithIgnoreCase(type, "image/"))
					{
						return readImage(ch);
					}
					
					// TODO as binary if size is known
					// otherwise input stream or exception
				}
				
				throw new UserException("don't know how to handle response type " + type + " content: " + ch.getContent());
			}
			else
			{
				throw new UserException("don't know how to handle connection " + c);
			}
		}
		finally
		{
			CKit.close(c.getInputStream());
		}
	}
	
	
	protected Object readText(HttpURLConnection c) throws Exception
	{
		String enc = c.getContentEncoding();
		Charset cs = JsUtil.parseCharset(enc);
		return CKit.readString(c.getInputStream(), cs);
	}
	
	
	protected Object readImage(HttpURLConnection c) throws Exception
	{
		Object r = ImageIO.read(c.getInputStream());
		return JsUtil.wrap(r);
	}
	
	
	public void inBrowser(Object url) throws Exception
	{
		URL u = JsUtil.parseURL(url);
		CBrowser.openLink(u);
	}
	
	
	public String toString()
	{
		InlineHelp h = new InlineHelp("NET");
		h.a("provides network-related functions:");
		//
		h.a("get(url)", "returns content specified by the URL");
		h.a("inBrowser(url)", "opens link in a browser");
		return h.toString();
	}
}
