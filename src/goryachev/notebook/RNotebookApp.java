// Copyright © 2014-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.log.Log;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.notebook.icons.NotebookIcons;
import goryachev.notebook.js.nb.NEmbeddedStorage;
import goryachev.notebook.storage.EmbeddedStorage;
import goryachev.swing.Application;
import goryachev.swing.UI;
import goryachev.swing.options.IntOption;
import goryachev.swing.options.StringListOption;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class RNotebookApp
	extends Application
{
	public static final String VERSION = "2023.0128.2108";
	public static final String COPYRIGHT = "copyright © 2023 andy goryachev";	
	public static final String WEB_SITE = "https://goryachev.com/products/rnotebook";
	public static final String SUPPORT_EMAIL = "support@goryachev.com";
	protected static final StringListOption openDocumentsOption = new StringListOption("open.documents");
	protected static final IntOption startCountOption = new IntOption("start.count", 0, 0, Integer.MAX_VALUE);
	public static final int REMINDER_COUNT = 60;
	public static final String EMBEDDED_STORAGE_FILE = "storage.dat";
	private static NEmbeddedStorage storage;
	
	
	public RNotebookApp()
	{
		super("RNotebook", VERSION, COPYRIGHT);
	}
	
	
	public static void main(String[] args)
	{
		new RNotebookApp().start();
	}
	
	
	public static String getUpdateURL(boolean menu)
	{
		SB sb = new SB();
		sb.a(WEB_SITE);
		sb.a("/version?");
		sb.a(getVersion());
		
		if(menu)
		{
			sb.a("&menu");
		}
		
		return sb.toString();
	}


	public ImageIcon getAppIcon()
	{
		return NotebookIcons.Application;
	}


	public String getAppTitle()
	{
		return "RNotebook";
	}


	protected void initApplication() throws Exception
	{
		// disable disk cache
		ImageIO.setUseCache(false);
	}
	
	
	protected void storeOpenWindows()
	{
		CList<String> ids = new CList();
		for(MainWindow w: UI.getWindowsOfType(MainWindow.class))
		{
			File f = w.getFile();
			if(f != null)
			{
				ids.add(f.getAbsolutePath());
			}
		}
		
		openDocumentsOption.set(ids);
	}
	
	
	public boolean exiting()
	{
		// TODO save storage
		
		storeOpenWindows();
		
		if(MainWindow.askToSaveAllOnExit() == false)
		{
			return false;
		}

		return true;
	}

	
	public void openMainWindow() throws Exception
	{
		MainWindow lastWindow = null;

		// load last state
		CList<String> fs = openDocumentsOption.get();
		if(fs != null)
		{
			for(String fname: fs)
			{
				try
				{
					final File f = new File(fname);
					if(f.exists())
					{
						final MainWindow w = new MainWindow();
						w.open();
						lastWindow = w;

						// show all windows before doing i/o
						UI.later(new Runnable()
						{
							public void run()
							{
								w.openFile(f);
							}
						});
					}
				}
				catch(Exception e)
				{
					log.error(e);
				}
			}
		}

		if(lastWindow == null)
		{
			lastWindow = new MainWindow();
			lastWindow.open();
			lastWindow.newFile();
		}

		// thank you dialog every so often
		int startCount = startCountOption.get() + 1;
		startCountOption.set(startCount);
		if((startCount % REMINDER_COUNT) == 0)
		{
			ThankYouDialog.openDialog(lastWindow);
		}
	}


	public synchronized static EmbeddedStorage getStorage()
	{
		if(storage == null)
		{
			File f = new File(getSettingsDirectory(), EMBEDDED_STORAGE_FILE);
			storage = new NEmbeddedStorage(f);
		}
		return storage;
	}
}
