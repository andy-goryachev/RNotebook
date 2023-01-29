// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.net;
import goryachev.common.util.CKit;
import goryachev.common.util.TextTools;
import goryachev.common.util.UserException;
import goryachev.notebook.js.JsObjects;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.Arg;
import goryachev.notebook.util.Doc;
import goryachev.notebook.util.InlineHelp;
import goryachev.swing.Application;
import goryachev.swing.CBrowser;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;


@Doc("provides network-related functions")
public class NET
{
	public NET()
	{
	}
	
	
	@Doc("returns content specified by the URL")
	@Arg("url")
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
		if("gzip".equalsIgnoreCase(enc))
		{
			GZIPInputStream in = new GZIPInputStream(c.getInputStream());
			return CKit.readString(in, CKit.CHARSET_UTF8);
		}
		else
		{
			Charset cs = JsUtil.parseCharset(enc);
			return CKit.readString(c.getInputStream(), cs);
		}
	}
	
	
	protected Object readImage(HttpURLConnection c) throws Exception
	{
		Object r = ImageIO.read(c.getInputStream());
		return JsUtil.wrap(r);
	}
	
	
	@Doc("opens a link in browser")
	@Arg("url")
	public void inBrowser(Object url) throws Exception
	{
		URL u = JsUtil.parseURL(url);
		CBrowser.openLink(u);
	}
	
	
	public String toString()
	{
		return getHelp().toString();
	}
	
	
	public InlineHelp getHelp()
	{
		return InlineHelp.create(JsObjects.NET, getClass());
	}
}
