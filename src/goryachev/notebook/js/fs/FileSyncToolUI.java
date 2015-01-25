// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.ui.table.ZTable;
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
	public static final int REFRESH_PERIOD = 100;
	public final JLabel currentFileField;
	public final JLabel copiedField;
	public final JLabel deletedField;
	public final JLabel totalField;
	public final JLabel freeSpaceField;
	public final JLabel elapsedField;
	public final JLabel statusField;
	public final JLabel errorField;
	public final SyncErrorModel model;
	public final ZTable table;
	public final CScrollPane scroll;
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
		
		timer = new Timer(REFRESH_PERIOD, this);
		
		statusField = new JLabel();
		
		errorField = new JLabel(" ");
		
		model = new SyncErrorModel();
		
		table = new ZTable(model);
		
		scroll = new CScrollPane(table, false);
		scroll.setTrackComponentDimensions(true);
		scroll.setBorder(Theme.lineBorder()); // TODO too dark
		
		CPanel p = new CPanel();
		p.setBorder(10);
		p.setBackground(Theme.panelBG());
		p.setLayout
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
		p.add(0, ix, heading("Sync"));
		p.add(1, ix, "Current file:");
		p.add(2, ix, currentFileField);
		ix++;
		p.add(0, ix, 0, ix+5, statusField);
		p.add(1, ix, "Free space:");
		p.add(2, ix, freeSpaceField);
		ix++;
		p.add(1, ix, "Copied:");
		p.add(2, ix, copiedField);
		ix++;
		p.add(1, ix, "Deleted:");
		p.add(2, ix, deletedField);
		ix++;
		p.add(1, ix, "Total files:");
		p.add(2, ix, totalField);
		ix++;
		p.add(1, ix, "Elapsed time:");
		p.add(2, ix, elapsedField);
		ix++;
		p.add(1, ix, "Errors:");
		p.add(2, ix, errorField);
		
		setNorth(p);
		setCenter(scroll);		
	}
	

	public void handleSyncWarning(File src, File dst, String err)
	{
		final SyncErrorModel.Entry en = new SyncErrorModel.Entry();
		en.error = err;
		en.src = src;
		en.dst = dst;
		
		UI.inEDTW(new Runnable()
		{
			public void run()
			{
				errors++;
				errorField.setText(Theme.formatNumber(errors));

				model.addItem(en);
			}
		});
		
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
					boolean ignore = tool.isIgnoreFailures();
					statusField.setIcon(errors == 0 ? CIcons.Success96 : ignore ? CIcons.Warning96 : CIcons.Error96);
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
