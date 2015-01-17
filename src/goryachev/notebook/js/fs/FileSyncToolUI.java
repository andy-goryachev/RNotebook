// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.fs;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import java.io.File;
import javax.swing.JLabel;
import research.tools.filesync.FileSyncTool;


public class FileSyncToolUI
	extends CPanel
	implements FileSyncTool.Listener
{
	public final JLabel currentFileField;
	
	
	public FileSyncToolUI()
	{
		currentFileField = new JLabel(" ");
		
		setLayout
		(
			new double[]
			{
				100,
				PREFERRED,
				PREFERRED,
				10,
				PREFERRED,
				10,
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
			10, 5
		);
		
		int ix = 0;
		add(0, ix, heading("Sync"));
		add(1, ix, "Current file:");
		add(2, ix, 7, ix, currentFileField);
		ix++;
		add(1, ix, "Free space on target:");
		//add(2, ix, 7, ix, currentFileField);
		ix++;
		add(2, ix, label("Byte(s)"));
		add(4, ix, label("File(s)"));
		add(6, ix, label("Folders(s)"));
		ix++;
		add(1, ix, "Copied:");
		ix++;
		add(1, ix, "Deleted:");
		
		setBorder(10);
		setBackground(Theme.panelBG());
	}


	public void handleSyncFileDeleted(File f)
	{
	}


	public void handleSyncFileCopied(File f)
	{
	}


	public void handleSyncFileError(Throwable e)
	{
	}


	public void handleSyncCurrentSourceFile(final File f)
	{
		UI.inEDT(new Runnable()
		{
			public void run()
			{
				currentFileField.setText(f.getAbsolutePath());
			}
		});
	}
}
