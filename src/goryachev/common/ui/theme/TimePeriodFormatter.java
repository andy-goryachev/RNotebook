// Copyright (c) 2013-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.util.CKit;
import goryachev.common.util.TXT;
import java.util.Date;


/** formats time period using variable precision and over estimation */
public class TimePeriodFormatter
{
	public static String format(Object x)
	{
		if(x != null)
		{
			long time;
			if(x instanceof Long)
			{
				time = (Long)x;
			}
			else if(x instanceof Date)
			{
				time = ((Date)x).getTime();
			}
			else
			{
				return null;
			}
			
			return format(time);
		}
		return null;
	}
	
	
	protected static int round(long x, int round)
	{
		return (int)((x / round) * round + round);
	}
	
	
	public static String format(long t)
	{
		long a;
		long b;
		int delta;
		
		if(t < 0)
		{
			return null;
		}
		
		a = t / CKit.MS_IN_A_DAY;
		if(a > 0)
		{
			// more than a day
			return TXT.get("TimePeriodFormatter.days", "{0} d.", (a + 1));
		}
		
		//
		
		a = t / CKit.MS_IN_AN_HOUR;
		b = -1;
		delta = 1;
		if(a < 5)
		{
			b = (t % CKit.MS_IN_AN_HOUR) / CKit.MS_IN_A_MINUTE;
			if(a > 0)
			{
				if(a == 1)
				{
					b = round(b, 10);
				}
				else
				{
					b = round(b, 30);
				}
				
				if(b == 60)
				{
					b = -1;
					a++;
					delta = 0;
				}
			}
		}

		if(a > 0)
		{
			if(b < 0)
			{
				// more than 5 hours
				return TXT.get("TimePeriodFormatter.hours", "{0} hr.", (a + delta));
			}
			else
			{
				// more than 1 hour
				return TXT.get("TimePeriodFormatter.hours minutes", "{0} hr. {1} min.", a, b);
			}
		}
		
		//
		
		a = t / CKit.MS_IN_A_MINUTE;
		b = -1;
		delta = 1;
		if(a < 3)
		{
			b = (t % CKit.MS_IN_A_MINUTE) / CKit.MS_IN_A_SECOND;
			if(a > 0)
			{
				if(a == 1)
				{
					b = round(b, 10);
				}
				else
				{
					b = round(b, 30);
				}
				
				if(b == 60)
				{
					b = -1;
					a++;
					delta = 0;
				}
			}
		}
		
		if(a > 0)
		{
			if(b < 0)
			{
				// more than 3 minutes
				return TXT.get("TimePeriodFormatter.minutes", "{0} min.", (a + delta));
			}
			else
			{
				// more than 1 minute
				return TXT.get("TimePeriodFormatter.minutes seconds", "{0} min. {1} sec.", a, b);
			}
		}
		
		//
		
		a = t / CKit.MS_IN_A_SECOND;
		if(a > 10)
		{
			a = round(a, 10);
		}
			
		return TXT.get("TimePeriodFormatter.seconds", "{0} sec.", a);
	}
}
