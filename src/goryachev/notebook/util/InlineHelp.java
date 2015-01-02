// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.common.util.CList;
import goryachev.common.util.SB;


/** Little object helps create inline help generated when js evaluates a top-level interpreter object */ 
public class InlineHelp
{
	private CList<Object> lines = new CList();
	private static int indent = 3;
	
	
	public InlineHelp()
	{
	}
	
	
	public void a(String header)
	{
		lines.add(header);
	}
	
	
	public void a(String function, String description)
	{
		lines.add(new String[] { function, description });
	}
	
	
	public String toString()
	{
		int w = 0;
		SB sb = new SB();
		
		// TODO sort?
		for(Object x: lines)
		{
			if(x instanceof String[])
			{
				String s = ((String[])x)[0];
				if(s.length() > w)
				{
					w = s.length();
				}
			}
		}
		
		
		for(Object x: lines)
		{
			if(sb.length() > 0)
			{
				sb.nl();
			}
			
			if(x instanceof String)
			{
				sb.a(x);
			}
			else if(x instanceof String[])
			{				
				String[] ss = (String[])x;
				
				sb.sp(indent);
				sb.a(ss[0]);
				sb.sp(w - ss[0].length());
				sb.a(" - ");
				sb.a(ss[1]);
			}
		}
		
		return sb.toString();
	}
}
