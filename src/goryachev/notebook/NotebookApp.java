// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.Application;
import goryachev.common.util.SB;
import goryachev.notebook.icons.NotebookIcons;
import javax.swing.ImageIcon;


public class NotebookApp
	extends Application
{
	public static final String VERSION = "0.04.01";
	public static final String COPYRIGHT = "copyright © 2015 andy goryachev";	
	public static final String WEB_SITE = "http://goryachev.com/products/"; // TODO
	
	
	public NotebookApp()
	{
		super("Notebook.4", VERSION, COPYRIGHT);
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


	public boolean exiting()
	{
		return true;
	}


	public ImageIcon getAppIcon()
	{
		return NotebookIcons.Application;
	}


	public String getAppTitle()
	{
		return "Research Notebook";
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
