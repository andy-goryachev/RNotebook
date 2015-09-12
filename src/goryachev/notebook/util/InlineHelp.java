// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.CSorter;
import goryachev.common.util.SB;
import java.lang.reflect.Method;


/** Little object helps create inline help generated when js evaluates a top-level interpreter object */ 
// TODO use annotations
// TODO or, return a table! use search within the table
public class InlineHelp
{
	private final String name;
	private CList<Object> lines = new CList();
	private static int indent = 3;
	
	
	public InlineHelp(String name)
	{
		this.name = name;
	}
	
	
	public void a(String header)
	{
		lines.add(header);
	}
	
	
	public void a(String function, String description)
	{
		lines.add(new String[] { name + "." + function, description });
	}
	
	
	public String toString()
	{
		int w = 0;
		SB sb = new SB();
		CMap<String,String> fs = new CMap();
		
		for(Object x: lines)
		{
			if(x instanceof String[])
			{
				String[] ss = (String[])x;
				String k = ss[0];
				if(k.length() > w)
				{
					w = k.length();
				}
				
				String v = ss[1];
				
				fs.put(k, v);
			}
		}
		
		// sort functions
		CList<String> ks = new CList(fs.keySet());
		CSorter.sort(ks);
		
		for(Object x: lines)
		{
			if(x instanceof String)
			{
				if(sb.length() > 0)
				{
					sb.nl();
				}
				
				sb.a(x);
			}
		}
		
		for(String k: ks)
		{
			if(sb.length() > 0)
			{
				sb.nl();
			}
			
			String v = fs.get(k);

			sb.sp(indent);
			sb.a(k);
			sb.sp(w - k.length());
			sb.a(" - ");
			sb.a(v);
		}
		
		return sb.toString();
	}
	
	
	public static InlineHelp create(String type, Class<?> c)
	{
		InlineHelp h = new InlineHelp(type);
		
		Doc cd = c.getAnnotation(Doc.class);
		
		// TODO constructor? header?
		if(cd != null)
		{
			h.a(cd.value());
		}
		
		Method[] ms = c.getMethods();
		for(Method m: ms)
		{
			Doc d = m.getAnnotation(Doc.class);
			if(d != null)
			{
				Arg a = m.getAnnotation(Arg.class);

				SB sb = new SB(64);
				sb.a(m.getName());
				sb.a('(');
				if(a != null)
				{
					String[] args = a.value();
					boolean comma = false;
					
					for(String s: args)
					{
						if(comma)
						{
							sb.a(",");
						}
						else
						{
							comma = true;
						}
						
						sb.a(s);
					}
				}
				sb.a(')');

				h.a(sb.toString(), d.value());
			}
		}
		return h;
	}
}
