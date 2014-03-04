// Copyright (c) 2007-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.dialogs.CFileChooser;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.common.util.html.HtmlTools;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;


// implements standard single-open-file logic
@Deprecated
public abstract class OpenFileLogic
{
	/** title or file name has changed */
	protected void delegateRefresh() { updateTitleDefault(); }
	
	protected abstract void delegateCommit();
	
	protected abstract void delegateNewFile() throws Exception;
	
	protected abstract void delegateSaveFile(File f) throws Exception;
	
	protected abstract void delegateOpenFile(File f) throws Exception;
	
	//
	
	public final CAction newFileAction = new CAction() { public void action() { newFilePrivate(); } };
	public final CAction openFileAction = new CAction() { public void action() { openFilePrivate(); } };
	public final CAction saveAction = new CAction() { public void action() { saveFilePrivate(); } };
	public final CAction saveAsAction = new CAction() { public void action() { saveFileAsPrivate(); } };
	public final CAction clearRecentAction = new CAction() { public void action() { clearRecentFiles(); } };
	
	public String openButtonText = Menus.Open;
	public String saveButtonText = Menus.Save;

	protected final JFrame parent;
	protected final  String lastDirKey;
	protected final String recentFilesKey;
	protected int maxRecentFiles = 10;
	private File file;
	private Boolean modified;
	private CList<File> recent;
	private CList<FileFilter> filters;

	private static final char DIVIDER = '\0';
	
	
	//
	
	
	public OpenFileLogic(JFrame parent)
	{
		this(parent, "last.dir", "recent.files");
	}

	
	public OpenFileLogic(JFrame parent, String lastDirKey, String recentFilesKey)
	{
		this.parent = parent;
		this.lastDirKey = lastDirKey;
		this.recentFilesKey = recentFilesKey;
	}
	
	
	public void setMaxRecentFiles(int n)
	{
		this.maxRecentFiles = n;
	}
	
	
	public File getFile()
	{
		return file;
	}
	
	
	public boolean isModified()
	{
		return modified == null ? false : modified;
	}
	
	
	public void setModified(boolean on)
	{
		if(!Boolean.valueOf(on).equals(modified))
		{
			modified = on;
			delegateRefresh();
		}
	}
	
	
	public void openRecentQuietly()
	{
		File f = getRecentFile();
		if(f != null)
		{
			if(f.exists() && f.isFile())
			{
				try
				{
					openFilePrivate(f);
				}
				catch(Exception e)
				{ }
			}
		}
	}
	
	
	public void clearRecentFiles()
	{
		GlobalSettings.set(recentFilesKey, null);
		recent = null;
	}
	
	
	protected CList<File> recentFiles()
	{
		if(recent == null)
		{
			recent = new CList();
			try
			{
				String s = GlobalSettings.getString(recentFilesKey);
				if(s != null)
				{
					s = CKit.decompressString(s);
					
					for(String fn: CKit.split(s, DIVIDER))
					{
						recent.add(new File(fn));
					}
				}
			}
			catch(Exception e)
			{ }
		}
		return recent;
	}
	
	
	public void addRecentFile(File f)
	{
		if(f != null)
		{
			CList<File> fs = recentFiles();
			int sz = fs.size();
			for(int i=sz-1; i>=0; --i)
			{
				if(fs.get(i).equals(f))
				{
					fs.remove(i);
				}
			}
			
			fs.add(0, f);
			while(fs.size() > maxRecentFiles)
			{
				fs.remove(fs.size() - 1);
			}
			
			GlobalSettings.set(recentFilesKey, encodeFiles(fs));
		}
	}
	
	
	protected String encodeFiles(CList<File> fs)
	{
		try
		{
			SB sb = new SB();
			for(File f: fs)
			{
				if(sb.getLength() > 0)
				{
					sb.a(DIVIDER);
				}
				sb.a(f.getAbsolutePath());
			}
		
			return CKit.compressString(sb.toString());
		}
		catch(Exception e)
		{ 
			return null;
		}
	}
	
	
	public File getRecentFile()
	{
		CList<File> fs = recentFiles();
		if(fs.size() > 0)
		{
			return fs.get(0);
		}
		return null;
	}
	
	
	public CList<File> getRecentFiles()
	{
		return new CList(recentFiles());
	}
	
	
	public int getRecentFileCount()
	{
		return recentFiles().size();
	}
	
	
	public boolean checkModified()
	{
		if(isModified())
		{
			int rv = Dialogs.choice
			(
				parent, 
				"Modified",
				null,
				"Do you want to save current file?",
				new String[] { "Save", "Discard", "Cancel" }
			);
			try
			{
				switch(rv)
				{
				case 0:
					// save
					delegateSaveFile(file);
					return true;
				case 1:
					// discard
					return true;
				case 2:
				default:
					return false;
				}
			}
			catch(Exception e)
			{
				Dialogs.error(parent, e);
				return false;
			}
		}
		return true;
	}

	
	protected void newFilePrivate()
	{
		delegateCommit();
		
		try
		{
			if(checkModified())
			{
				file = null;
				delegateNewFile();
			}
		}
		catch(Exception e)
		{
			Dialogs.error(parent, e);
		}
	}
	
	
	protected void openFilePrivate()
	{
		delegateCommit();
		
		try
		{
			if(checkModified())
			{
				CFileChooser fc = new CFileChooser(parent, lastDirKey);
				configureFileFilter(fc);
				fc.setApproveButtonText(openButtonText);
				File f = fc.openFileChooser();
				if(f != null)
				{
					openFilePrivate(f);
				}
			}
		}
		catch(Exception e)
		{
			Dialogs.error(parent, e);
		}
	}
	
	
	protected CList<FileFilter> filters()
	{
		if(filters == null)
		{
			filters = new CList();
		}
		return filters;
	}
	
	
	public void addFileFilter(String ext, final String description)
	{
		filters().add(0, new CExtensionFileFilter(ext, description));
	}


	protected void configureFileFilter(CFileChooser fc)
	{
		fc.resetChoosableFileFilters();
		
		for(FileFilter ff: filters())
		{
			fc.addChoosableFileFilter(ff);
		}
	}


	public void doOpenFile(File f)
	{
		delegateCommit();
		
		try
		{
			if(checkModified())
			{
				openFilePrivate(f);
			}
		}
		catch(Exception e)
		{
			Dialogs.error(parent, e);
		}
	}
	
	
	public void openFilePrivate(File f) throws Exception
	{
		delegateOpenFile(f);
		
		file = f;
		addRecentFile(f);
		setModified(false);
		delegateRefresh();
	}
	
	
	protected void saveFilePrivate()
	{
		try
		{
			if(file != null)
			{
				delegateCommit();
				delegateSaveFile(file);
				setModified(false);
				delegateRefresh();
			}
		}
		catch(Exception e)
		{
			Dialogs.error(parent, e);
		}
	}
	
	
	protected File fixExtension(File f)
	{
		String name = f.getName();
		if(!name.contains("."))
		{
			String ext = getExtension();
			if(ext != null)
			{
				if(!ext.startsWith("."))
				{
					ext = "." + ext;
				}
				
				return new File(f.getParentFile(), name + ext); 
			}
		}
		return f;
	}
	
	
	protected String getExtension()
	{
		if(filters != null)
		{
			for(FileFilter f: filters)
			{
				if(f instanceof CExtensionFileFilter)
				{
					return ((CExtensionFileFilter)f).getFirstExtension();
				}
			}
		}
		return null;
	}
	
	
	protected void saveFileAsPrivate()
	{
		delegateCommit();
		
		CFileChooser fc = new CFileChooser(parent, lastDirKey);
		configureFileFilter(fc);
		fc.setApproveButtonText(saveButtonText);
		File f = fc.openFileChooser();
		f = fixExtension(f);
		if(f != null)
		{
			if(f.exists())
			{
				int rv = Dialogs.choice
				(
					parent, 
					"Overwrite?", // FIX
					"File exists.  Do you want to overwrite it?",
					null,
					new String[] { "Overwrite", "Cancel" }
				);
				if(rv != 0)
				{
					return;
				}
			}
			
			try
			{
				delegateSaveFile(f);
				
				file = f;
				setModified(false);
				addRecentFile(f);
				delegateRefresh();
			}
			catch(Exception e)
			{
				Dialogs.error(parent, e);
			}
		}
	}


	public CMenu recentFilesMenu()
	{
		return new RecentFilesMenu();
	}
	
	
	protected void updateTitleDefault()
	{
		File f = getFile();
		if(f == null)
		{
			parent.setTitle(Application.getTitle());
		}
		else
		{
			parent.setTitle((isModified() ? "*" : "") + f.getName() + " - " + Application.getTitle());
		}
	}
	
	
	//
	
	
	public class RecentFilesMenu 
		extends CMenu
		implements MenuListener
	{
		public RecentFilesMenu()
		{
			super("Open Recent");
			addMenuListener(this);
		}


		public String getText()
		{
			boolean old = super.isEnabled();
			boolean on = getRecentFileCount() > 0;
			if(old != on)
			{
				super.setEnabled(on);
			}

			return super.getText();
		}


		protected void rebuild()
		{
			removeAll();

			for(final File f: recentFiles())
			{
				String name = f.getName();
				String path = f.getAbsolutePath();

				if(path.endsWith(name))
				{
					path = path.substring(0, path.length() - name.length());
					path = "<html>" + HtmlTools.safe(path) + "<b>" + HtmlTools.safe(name) + "</b>";
				}

				add(new CMenuItem(path, new CAction()
				{
					public void action()
					{
						doOpenFile(f);
					}
				}));
			}

			if(getRecentFileCount() > 0)
			{
				addSeparator();
			}
			add(new CMenuItem("Clear Recent Files", clearRecentAction));
		}


		public void menuSelected(MenuEvent ev)
		{
			rebuild();
		}


		public void menuDeselected(MenuEvent ev) { }
		public void menuCanceled(MenuEvent ev) { }
	}
}
