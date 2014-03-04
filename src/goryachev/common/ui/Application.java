// Copyright (c) 2007-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.dialogs.license.StandardLicense;
import goryachev.common.util.CJob;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import goryachev.common.util.TXT;
import goryachev.common.util.platform.ApplicationSupport;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import javax.swing.ImageIcon;


public abstract class Application
{
	// or createSplashScreen()
	protected Image createSplashImage() { return null; }
	
	// after app log is initialized
	protected void initCustomLog() { }

	// after user preferences are loaded
	protected void initI18N() throws Exception { }
	
	// before creating main window
	protected abstract void initApplication() throws Exception;
	
	// always in EDT, creates main (or first) window
	public abstract void openMainWindow() throws Exception;
	
	/** before exiting: return true if it is ok to exit */
	public boolean exiting() { return true; }
	
	public File getAppSettingsDirectory() { return getDefaultSettingsDirectory(); }
	
	// allows the app to start if no display is found (as in on server)
	// return true if app started, false if headless mode is not supported.
	public boolean startHeadless() { return false; }
	
	// to be used in dialogs etc.
	public abstract String getAppTitle();

	// to be used in dialogs etc.
	public abstract ImageIcon getAppIcon();

	//
	
	public static final CAction exitAction = new CAction() { public void action() { exit(); }};

	public final String profileName;
	public final String version;
	public final String copyright;
	private String preferencesFileName = "preferences.dat";
	private static String company = "goryachev.com";
	protected Window splash;
	private static Application instance;
	

	public Application(String profileName, String version, String copyright)
	{
		this.profileName = profileName;
		this.version = version;
		this.copyright = copyright;
	}
	
	
	public static String getID()
	{
		return instance == null ? null : instance.profileName;
	}
	
	
	public static ApplicationLicense getLicense()
	{
		return instance.getAppLicense();
	}
	
	
	public ApplicationLicense getAppLicense()
	{
		StandardLicense lic = new StandardLicense();
		lic.setMilitaryClause(true);
		return lic;
	}
	
	
	protected void loadUserPreferences()
	{
		GlobalSettings.init(getPreferencesFile());
	}
	
	
	public File getLogFolder()
	{
		return new File(Application.getSettingsDirectory(), "logs");
	}
	
	
	protected void initLogger()
	{
		File dir = getLogFolder();
		dir.mkdirs();
		
		Log.init(dir);
		
		initCustomLog();
	}
	
	
	protected void initLookAndFeel()
	{
		Theme.init();
	}
	

	public synchronized void start()
	{
		if(instance != null)
		{
			throw new RuntimeException("Application already started");
		}
		instance = this;
		
		try
		{
			initLogger();
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		
		long start = System.nanoTime();
		
		// splash screen
		try
		{
			UI.inEDTW(new Runnable()
			{
				public void run()
				{
					splash = createSplashScreen();
					if(splash != null)
					{
						splash.setVisible(true);
					}
				}
			});
		}
		catch(HeadlessException e)
		{
			handleHeadlessException(e);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		try
		{
			loadUserPreferences();
			initI18N();
		}
		catch(Exception e)
		{
			Log.err(e);
			System.exit(-1);
		}
		
		try
		{
			initApplication();
		}
		catch(final Exception e)
		{
			// failed app init
			Log.err(e);

			try
			{
				UI.inEDTW(new Runnable()
				{
					public void run()
					{
						showError(e);
					}
				});
			}
			catch(Exception er)
			{ }
			
			System.exit(-2);
		}
		
		if(splash != null)
		{
			long t = (System.nanoTime() - start)/1000000;
			if(t < 500)
			{
				CKit.sleep((int)t);
			}
		}
		
		try
		{
			UI.inEDTW(new Runnable()
			{
				public void run()
				{
					startUI();
				}
			});
		}
		catch(Exception e)
		{
			Log.err(e);
			System.exit(-3);
		}
	}


	// in EDT
	protected void startUI()
	{
		try
		{
			initLookAndFeel();
			
			CFocusMonitor.init();
			
			openMainWindow();
			closeSplashScreen();
		}
		catch(HeadlessException e)
		{
			handleHeadlessException(e);
		}
		catch(Throwable e)
		{
			showError(e);
		}
	}
	
	
	protected void handleHeadlessException(HeadlessException e)
	{
		if(startHeadless())
		{
			return;
		}
		
		// can't run headless
		e.printStackTrace();
		
		Log.err(e);
		System.exit(-33);
	}
	
	
	// in EDT
	public void showError(Throwable e)
	{
		Log.err(e);
		
		String msg = e.getMessage();
		if(CKit.isBlank(msg))
		{
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			msg = w.toString();
		}
		
		closeSplashScreen();
		
		// FIX
		Dialogs.startupError(getIcon(), TXT.get("Application.startup error title","Startup Error") + " - " + getTitle(), msg);
	}
	
	
	protected void closeSplashScreen()
	{
		if(splash != null)
		{
			splash.dispose();
			splash = null;
		}
	}
	
	
	public synchronized static void exit()
	{
		if(instance != null)
		{
			if(!instance.exiting())
			{
				return;
			}
			
			savePreferences();
		}
		
		if(ApplicationSupport.shutdownCJobExecutor)
		{
			// shutdown CJob executor if it has been started
			CJob.shutdown();
		}
		
		System.exit(0);
	}
	
	
	public static void savePreferences()
	{
		GlobalSettings.save();
	}
	
	
	public String preferencesFileName()
	{
		return "preferences.dat";
	}
	
	
	public File getPreferencesFile()
	{
		return new File(getSettingsDirectory(), preferencesFileName());
	}


	public static File getSettingsDirectory()
	{
		return instance.getAppSettingsDirectory();
	}


	public static File getDefaultSettingsDirectory()
	{
		File f = new File(getUserHome(), ".goryachev.com" + File.separator + instance.profileName);
		f.mkdirs();
		return f;
	}


	public static File getUserHome()
	{
		return new File(System.getProperty("user.home"));
	}
	
	
	protected Window createSplashScreen()
	{
		Image splashImage = createSplashImage();
		if(splashImage != null)
		{
			return new DefaultSplashScreen(splashImage);
		}
		else
		{
			return null;
		}
	}

	
	public static void load(Properties p, File file) throws Exception
	{
		FileInputStream in = new FileInputStream(file);
		try
		{
			p.load(in);
		}
		finally
		{
			CKit.close(in);
		}
	}
	
	
	public static void save(Properties p, File file) throws Exception
	{
		FileOutputStream out = new FileOutputStream(file);
		try
		{
			p.store(out,"");
		}
		finally
		{
			CKit.close(out);
		}
	}
	

	public static ImageIcon getIcon()
	{
		return instance == null ? null : instance.getAppIcon();
	}
	
	
	public static String getTitle()
	{
		return instance == null ? null : instance.getAppTitle();
	}
	
	
	public static String getVersion()
	{
		return instance == null ? null : instance.version;
	}
	
	
	public static String getCopyright()
	{
		return instance == null ? null : instance.copyright;
	}
	
	
	public static String getCompany()
	{
		return company;
	}
	

	public static void closeAllWindow()
	{
		for(Window w: Window.getWindows())
		{
			w.dispose();
		}
	}
	
	
	public static CAction licenseAction()
	{
		return new CAction()
		{
			public void action()
			{
				ApplicationLicense license = getLicense();

				BaseDialog d = new BaseDialog(getSourceWindow(), "ApplicationLicenseDialog", true);
				d.setTitle(license.getTitle() + " - " + getTitle() + " " + getVersion());
				
				BasePanel p = new BasePanel();
				CTextPane t = p.setCenterCTextPane();
				t.setDocument(license.getDocument());
				t.setCaretPosition(0);
				p.buttons().add(new CButton(Menus.OK, d.closeDialogAction, true));
				d.setCenter(p);
				d.closeOnEscape(p);
				d.open();
			}
		};
	}
}
