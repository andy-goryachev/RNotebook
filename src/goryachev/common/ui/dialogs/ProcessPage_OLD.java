// Copyright (c) 2013-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Progress;
import goryachev.common.ui.ProgressLabel;
import goryachev.common.ui.ProgressLogic;
import goryachev.common.ui.Theme;
import goryachev.common.util.CKit;
import goryachev.common.util.TXT;
import javax.swing.JLabel;


@Deprecated // use new ProcessPage
public class ProcessPage_OLD
	extends CPanel
{
	public final ProgressLabel progressBar;
	public final JLabel percentLabel;
	public final JLabel elapsedLabel;
	public final JLabel remainingLabel;
	private long remaining;
	private long prevRemaining;
	private ProgressLogic progressLogic;
	
	
	public ProcessPage_OLD()
	{
		progressBar = new ProgressLabel();

		percentLabel = new JLabel(" ", null, JLabel.CENTER);

		elapsedLabel = new JLabel(" ");

		remainingLabel = new JLabel(" ");

		setLayout
		(
			new double[] 
			{
				CPanel.FILL, 
				CPanel.FILL
			},
			new double[] 
			{ 
				10,
				CPanel.PREFERRED,
				10,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.FILL,
			},
			20, 
			3
		);
		
		add(0,1,1,1, new JLabel(null, Theme.waitIcon(48), JLabel.CENTER));
		add(0,3,1,3, progressBar);
		add(0,4,1,4, percentLabel);
		add(0,5,     new JLabel(TXT.get("ProcessPage_OLD.remaining time","Remaining:"), null, JLabel.RIGHT));
		add(1,5,     remainingLabel);
		add(0,6,     new JLabel(TXT.get("ProcessPage_OLD.elapsed time","Elapsed:"), null, JLabel.RIGHT));
		add(1,6,     elapsedLabel);
		
		setBorder(new CBorder(10));
	}
	
	
	public void updateProgress(long start, Progress p, boolean cancelled)
	{
		if(p == null)
		{
			return;
		}
		
		if(progressLogic == null)
		{
			progressLogic = new ProgressLogic();
			progressLogic.setStart(start);
		}
		progressLogic.setProgress(p);
		
		long elapsed = System.currentTimeMillis() - start;
		// FIX do not show 100% if not completed
		int percent = (int)Math.round(100 * p.getProgress());
		progressBar.setValue(percent);
		percentLabel.setText(percent + "%");
		
		prevRemaining = remaining;
		
		if(percent == 0)
		{
			remaining = -1;
		}
		else
		{
			remaining = elapsed * (100 - percent) / percent;
		}
		
		elapsedLabel.setText(time(elapsed));
		
		// estimate remaining time
		if(cancelled)
		{
			remainingLabel.setText(TXT.get("ProcessPage_OLD.cancelling", "Cancelling..."));
		}
		else
		{
			long rem;
			if(prevRemaining < 0)
			{
				rem = remaining;
			}
			else
			{
				if(remaining < 0)
				{
					rem = prevRemaining;
				}
				else
				{
					// implement simple averaging across two last samples
					// ignoring undefined (negative) values
					// rem = (remaining + prevRemaining)/2;
					
					// avoid wild oscillations at the expense of overestimation
					rem = Math.max(remaining, prevRemaining);
				}
			}
			
			//remainingLabel.setText(time(rem));
			remainingLabel.setText(progressLogic.getEstimatedTimeRemaining());
		}
	}
	
	
	protected static String time(long t)
	{
		if(t < 0)
		{
			return "...";
		}
		else
		{
			return CKit.msToString(t);
		}
	}
}
