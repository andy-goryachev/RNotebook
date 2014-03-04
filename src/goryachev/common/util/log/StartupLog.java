// Copyright (c) 2009-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.log;
import goryachev.common.ui.Application;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CSorter;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;


public class StartupLog
{
	private SB sb;
	private String indent = "\t";
	private DecimalFormat numberFormat = new DecimalFormat("#,##0.##");
	
	
	public String getSystemInfo()
	{
		sb = new SB();
		
		extractApp();
		extractEnvironment();
		extractSystemProperties();
		
		String s = sb.toString();
		sb = null;
		return s;
	}
	
	
	protected void extractApp()
	{
		sb.a("Application\n");
		sb.a(indent);
		if(CKit.isNotBlank(Application.getTitle()))
		{
			sb.a(Application.getTitle()).a(" version ").a(Application.getVersion()).nl();
		}
		
		sb.a(indent);
		sb.a("Time: ").a(new SimpleDateFormat("yyyy-MMdd HH:mm:ss").format(System.currentTimeMillis())).nl();
		
		long max = Runtime.getRuntime().maxMemory();
		long free = max - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
		
		sb.a(indent).a("Available Memory: ").append(number(max)).nl();
		sb.a(indent).a("Free Memory: ").a(number(free)).nl();
		
		sb.nl();
	}

	
	protected void extractEnvironment()
	{
		sb.a("Environment\n");
		
		Map<String,String> env = System.getenv();
		CList<String> keys = new CList(env.keySet());
		CSorter.sort(keys);
		for(String key: keys)
		{
			sb.a(indent);
			sb.a(key).append(" = ").append(safe(env.get(key))).nl();
		}
		sb.nl();
	}
	
	
	protected void extractSystemProperties()
	{
		sb.a("System Properties\n");
		
		Properties p = System.getProperties();
		CList<String> keys = new CList(p.stringPropertyNames());
		CSorter.sort(keys);
		for(String key: keys)
		{
			sb.a(indent);
			sb.a(key).append(" = ").append(safe(p.getProperty(key))).nl();
		}
		sb.nl();
	}
	
	
	protected String number(Object x)
	{
		return numberFormat.format(x);
	}
	
	
	protected String safe(String s)
	{
		if(s != null)
		{
			boolean notSafe = false;
			int sz = s.length();
			for(int i=0; i<sz; i++)
			{
				char c = s.charAt(i);
				if(c < 0x20)
				{
					notSafe = true;
					break;
				}
			}
			
			if(notSafe)
			{
				SB sb = new SB(sz);
				for(int i=0; i<sz; i++)
				{
					char c = s.charAt(i);
					if(c < 0x20)
					{
						sb.a(unicode(c));
					}
					else
					{
						sb.a(c);
					}
				}
				s = sb.toString();
			}
		}
		return s;
	}
	
	
	protected static String unicode(char c)
	{
		return "\\u" + Hex.toHexString(c, 4);
	}
}
