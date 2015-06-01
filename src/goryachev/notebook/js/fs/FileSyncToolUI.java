// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.ui.CPanel3;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.ui.table.ZTable;
import goryachev.common.util.CKit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.Timer;
import research.tools.filesync.FileSyncTool;


public class FileSyncToolUI
	extends CPanel3
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
		scroll.setBorder(Theme.lineBorder());
		
		CPanel3 p = new CPanel3(10, 2);
		p.setBorder(5, 10);
		p.setBackground(Theme.panelBG());
		p.addColumns
		(
			100,
			CPanel3.PREFERRED,
			CPanel3.FILL
		);
		p.row(0, heading("Sync"));
		p.row(1, new JLabel("Current file:"));
		p.row(2, currentFileField);
		p.nextRow();
		p.row(0, 1, 5, statusField);
		p.row(1, new JLabel("Free space:"));
		p.row(2, freeSpaceField);
		p.nextRow();
		p.row(1, new JLabel("Updated:"));
		p.row(2, copiedField);
		p.nextRow();
		p.row(1, new JLabel("Deleted:"));
		p.row(2, deletedField);
		p.nextRow();
		p.row(1, new JLabel("Total files:"));
		p.row(2, totalField);
		p.nextRow();
		p.row(1, new JLabel("Elapsed time:"));
		p.row(2, elapsedField);
		p.nextRow();
		p.row(1, new JLabel("Errors:"));
		p.row(2, errorField);
		
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
	}


	public void handleSyncRunning(final FileSyncTool tool, final boolean on)
	{
		final boolean cancelled = CKit.isCancelled();
		
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
					statusField.setIcon(errors == 0 ? (cancelled ? CIcons.Cancelled96 : CIcons.Success96) : ignore ? CIcons.Warning96 : CIcons.Error96);
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
