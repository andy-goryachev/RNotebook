// Copyright (c) 2013-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;


// http://stackoverflow.com/questions/2779600/how-to-estimate-download-time-remaining-accurately
// http://en.wikipedia.org/wiki/Moving_average#Exponential_moving_average
// http://stackoverflow.com/questions/1881652/estimating-forecasting-download-completion-time
public class ProgressLogic
{
	private long start;
	private double durationAverage;
	private double durationExp = -1;
	private double alpha;
	
	
	public ProgressLogic()
	{
		setNPeriods(2);
		start();
	}
	
	
	public void setAlpha(double a)
	{
		alpha = a;
	}
	
	
	public void setNPeriods(int n)
	{
		setAlpha(2.0 / (n + 1));
	}
	
	
	protected long time()
	{
		return System.currentTimeMillis();
	}
	
	
	public void start()
	{
		setStart(time());
	}
	
	
	public synchronized void setStart(long t)
	{
		start = t;
	}
	
	
	public synchronized void setProgress(Progress p)
	{
		double fudgeFactor = 2;
		
		// average computed over all period
		durationAverage = fudgeFactor * (time() - start) / p.getProgress();
		
		// exponential moving average
		if(durationExp < 0)
		{
			durationExp = durationAverage;
		}
		else
		{
			durationExp = alpha * durationAverage + (1.0 - alpha) * durationExp;
		}
	}
	
	
	/** returns estimated time to completion */
	public synchronized long getEstimatedTimeToCompletion()
	{
		return (long)(start + durationAverage - time());
	}
	
	
	/** returns estimated time to completion, computed using exponential moving average */
	public synchronized long getEstimatedExponentialTimeToCompletion()
	{
		return (long)(start + durationExp - time());
	}
	
	
	/** returns estimated time to completion string */
	public synchronized String getEstimatedTimeRemaining()
	{
		if(durationExp < 0)
		{
			return "";
		}
		
		long est = getEstimatedTimeToCompletion();
		if(est > 30 * CKit.MS_IN_A_DAY)
		{
			return "...";
		}
		long err = Math.abs(est - getEstimatedExponentialTimeToCompletion());
		
		long hrs = est/CKit.MS_IN_AN_HOUR;
		long min = est/CKit.MS_IN_A_MINUTE;
		long sec = est/CKit.MS_IN_A_SECOND;
		
		return Theme.formatTimePeriod(est);
	}
}
