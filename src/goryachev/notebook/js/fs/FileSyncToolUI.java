// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.util.D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.Timer;
import research.tools.filesync.FileSyncTool;


public class FileSyncToolUI
	extends CPanel
	implements FileSyncTool.Listener, ActionListener
{
	public final JLabel currentFileField;
	public final JLabel copiedField;
	public final JLabel deletedField;
	public final JLabel totalField;
	public final JLabel freeSpaceField;
	public final JLabel elapsedField;
	public final JLabel statusField;
	public final JLabel errorField;
	protected final Timer timer;
	protected FileSyncTool tool;
	protected long started;
	protected int errors;
	protected volatile File target;
	
	
	public FileSyncToolUI()
	{
		currentFileField = new JLabel(" ");
		
		copiedField = new JLabel(" ");
		
		deletedField = new JLabel(" ");
		
		totalField = new JLabel(" ");
		
		freeSpaceField = new JLabel(" ");
		
		elapsedField = new JLabel(" ");
		
		timer = new Timer(100, this);
		
		statusField = new JLabel();
		
		errorField = new JLabel(" ");
		
		setLayout
		(
			new double[]
			{
				100,
				PREFERRED,
				FILL
			},
			new double[]
			{
				PREFERRED,
				PREFERRED,
				PREFERRED,
				PREFERRED,
				PREFERRED,
				PREFERRED,
				PREFERRED,
			},
			10, 2
		);
		
		int ix = 0;
		add(0, ix, heading("Sync"));
		add(1, ix, "Current file:");
		add(2, ix, currentFileField);
		ix++;
		add(0, ix, 0, ix+5, statusField);
		add(1, ix, "Free space:");
		add(2, ix, freeSpaceField);
		ix++;
		add(1, ix, "Copied:");
		add(2, ix, copiedField);
		ix++;
		add(1, ix, "Deleted:");
		add(2, ix, deletedField);
		ix++;
		add(1, ix, "Total files:");
		add(2, ix, totalField);
		ix++;
		add(1, ix, "Elapsed time:");
		add(2, ix, elapsedField);
		ix++;
		add(1, ix, "Errors:");
		add(2, ix, errorField);
		
		setBorder(10);
		setBackground(Theme.panelBG());
	}
	

	public void handleSyncWarning(File src, File dst, String err)
	{
		errors++;
		errorField.setText(Theme.formatNumber(errors));
		
		D.print(err); // FIX
	}


	public void handleSyncRunning(final FileSyncTool tool, final boolean on)
	{
		UI.inEDTW(new Runnable()
		{
			public void run()
			{
				timer.stop();
				
				FileSyncToolUI.this.tool = tool;
				
				if(on)
				{
					timer.start();
					started = System.currentTimeMillis();
					statusField.setIcon(Theme.waitIcon(96));
				}
				else
				{
					currentFileField.setText(null);
					// TODO warn if ignore errors, error otherwise
					statusField.setIcon(errors == 0 ? CIcons.Success96 : CIcons.Error96);
				}
			}
		});
	}


	// timer event
	public void actionPerformed(ActionEvent ev)
	{
		if(tool != null)
		{
			FileSyncTool.Info info = tool.getInfo();
			update(info);
		}
	}
	
	
	public void handleSyncTarget(File f) 
	{
		this.target = f;
	}
	
	
	protected void update(FileSyncTool.Info d)
	{
		currentFileField.setText(d.currentFile == null ? null : d.currentFile.getAbsolutePath());
		copiedField.setText(Theme.formatNumber(d.copiedFiles));
		deletedField.setText(Theme.formatNumber(d.deletedFiles));
		totalField.setText(Theme.formatNumber(d.totalFiles));
		
		// free
		String s;
		if(target == null)
		{
			s = null;
		}
		else
		{
			long free = target.getFreeSpace();
			s = Theme.formatNumber(free);
		}
		
		freeSpaceField.setText(s);

		// elapsed
		if(started <= 0)
		{
			elapsedField.setText(null);
		}
		else
		{
			elapsedField.setText(Theme.formatTimePeriod2(System.currentTimeMillis() - started));
		}
	}
}
