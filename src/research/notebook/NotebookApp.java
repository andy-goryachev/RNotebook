// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package research.notebook;
import goryachev.common.ui.Application;
import javax.swing.ImageIcon;
import research.notebook.icons.ToolboxIcons;


public class NotebookApp
	extends Application
{
	public static final String VERSION = "0.04.00";
	public static final String COPYRIGHT = "copyright © 2014 andy goryachev";	
	
	
	public NotebookApp()
	{
		super("Notebook.4", VERSION, COPYRIGHT);
	}


	public boolean exiting()
	{
		return true;
	}


	public ImageIcon getAppIcon()
	{
		return ToolboxIcons.Application;
	}


	public String getAppTitle()
	{
		return "Notebook";
	}


	protected void initApplication() throws Exception
	{
	}


	public void openMainWindow()
	{
		new NotebookWindow().open();
	}
	
	
	public static void main(String[] args)
	{
		new NotebookApp().start();
	}
}
