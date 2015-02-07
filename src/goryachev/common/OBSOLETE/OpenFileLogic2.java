// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.OBSOLETE;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CMenu;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.GlobalSettings;
import goryachev.common.ui.dialogs.CFileChooser;
import goryachev.common.ui.options.RecentFilesOption;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import goryachev.common.util.SB;
import goryachev.common.util.TXT;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;


@Deprecated
public abstract class OpenFileLogic2
{
	public abstract void delegateStoreFile(File f) throws Exception;
	
	public abstract void delegateLoadFile(File f) throws Exception;
	
	public abstract void delegateUpdateActions(); 
	
	//
	
	public final CAction openAction = new CAction() { public void action() { openFile(); }};
	public final CAction saveAction = new CAction() { public void action() { save(); }};
	public final CAction saveAsAction = new CAction() { public void action() { saveAs(); }};

	public static final String KEY_LAST_FOLDER = "last.folder";
	public static final String KEY_RECENT_FILES = "recent.files";
	public final JFrame frame;
	public final RecentFilesOption recentFilesOption;
	private File file;
	private boolean modified;
	private FileFilter filter;
	private String ensureExt;
	
	
	public OpenFileLogic2(JFrame w)
	{
		this.frame = w;
		
		recentFilesOption = new RecentFilesOption(KEY_RECENT_FILES)
		{
			protected void onRecentFileSelected(File f)
			{
				openFile(f);
			}
		};
	}
	
	
	public CMenu recentFilesMenu()
	{
		return recentFilesOption.recentFilesMenu();
	}
	
	
	public void setFileFilter(FileFilter f)
	{
		filter = f;
	}
	
	
	public FileFilter getFileFilter()
	{
		return filter;
	}
	
	
	public void setEnsureExtension(String ext)
	{
		ensureExt = ext;
	}
	
	
	public void setModified(boolean on)
	{
		if(modified != on)
		{
			modified = on;
			delegateUpdateActions();
			doUpdateTitle();
		}
	}
	
	
	public void doUpdateTitle()
	{
		SB sb = new SB();
		
		sb.a(Application.getTitle());
		
		File f = getFile();
		if(f != null)
		{
			sb.a(" - ");
			sb.a(f);
		}
		
		if(isModified())
		{
			sb.a(" *");
		}
		
		frame.setTitle(sb.toString());
	}
	
	
	public boolean isModified()
	{
		return modified;
	}
	
	
	public File getFile()
	{
		return file;
	}
	
	
	public boolean hasFile()
	{
		return (file != null);
	}
	
	
	public void setFile(File f)
	{
		this.file = f;
		recentFilesOption.add(f);
		GlobalSettings.save();
		setModified(false);
		
		delegateUpdateActions();
		doUpdateTitle();		
	}
	
	
	public void openFile()
	{
		openFile(null);
	}
	
	
	public void openFile(File chosen)
	{
		if(askToSaveIfModified())
		{
			return;
		}
			
		File f = chosen;
		if(chosen == null)
		{
			CFileChooser fc = new CFileChooser(frame, KEY_LAST_FOLDER);
			fc.setDialogType(CFileChooser.OPEN_DIALOG);
			
			if(filter != null)
			{
				fc.setFileFilter(filter);
			}
			
			f = getFile();
		
			if(f != null)
			{
				fc.setCurrentDirectory(f);
				fc.setSelectedFile(f);
			}
			
			f = fc.openFileChooser();
		}
		
		if(f != null)
		{
			try
			{
				delegateLoadFile(f);

				setFile(f);
			}
			catch(Exception e)
			{
				Log.err(e);
				Dialogs.error(frame, e);
			}
		}
	}
	
	
	public void save()
	{
		save(true);
	}
	
	
	public void save(boolean showErrors)
	{
		try
		{
			File f = getFile();
			if(f == null)
			{
				saveAs();
				return;
			}
			
			delegateStoreFile(f);
			
			setModified(false);
			delegateUpdateActions();
			doUpdateTitle();
		}
		catch(Exception e)
		{
			if(showErrors)
			{
				Dialogs.error(frame, e);
			}
			else
			{
				Log.err(e);
			}
		}
	}
	
	
	public void saveAs()
	{
		CFileChooser fc = new CFileChooser(frame, KEY_LAST_FOLDER);
		fc.setDialogType(CFileChooser.SAVE_DIALOG);
		
		if(filter != null)
		{
			fc.setFileFilter(filter);
		}
		
		File f = getFile();
		if(f != null)
		{
			fc.setCurrentDirectory(f);
			fc.setSelectedFile(f);
		}
		
		f = fc.openFileChooser();
		if(f != null)
		{
			if(ensureExt != null)
			{
				f = CKit.ensureExtension(f, ensureExt);
			}
			
			if(f.exists())
			{
				int rv = Dialogs.choice
				(
					frame, 
					"File Exists", 
					TXT.get("OpenFileLogic2.q.file exists", "File {0} exists.  Overwrite?", f),
					new String[] { "Cancel", "Overwrite" }
				);
				switch(rv)
				{
				case 1:
					break;
				default:
					return;
				}
			}
			
			try
			{
				delegateStoreFile(f);

				setFile(f);
			}
			catch(Exception e)
			{
				Log.err(e);
				Dialogs.error(frame, e);
			}
		}
	}


	/** returns true if the user wants to continue the editing */
	public boolean askToSaveIfModified()
	{
		if(isModified())
		{
			String msg;
			File f = getFile();
			if(f == null)
			{
				msg = TXT.get("OpenFileLogic2.save changes", "Save Changes?");
			}
			else
			{
				msg = TXT.get("OpenFileLogic2.file modified save?", "File {0} is modified.  Save?", f);
			}
			
			int rv = Dialogs.choice
			(
				frame, 
				null, 
				msg, 
				new String[] { "Save", "Discard", "Cancel" }
			);
			switch(rv)
			{
			case 0:
				save();
				return false;
			case 1:
				return false;
			default:
				return true;
			}
		}
		return false;
	}
	

	public File getMostRecentFile()
	{
		return recentFilesOption.getRecentFile();
	}
}
