// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CMenu;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.ContactSupport;
import goryachev.common.ui.Menus;
import goryachev.common.ui.dialogs.CheckForUpdatesDialog;
import goryachev.notebook.licenses.OpenSourceLicenses;


public class HelpMenu
	extends CMenu
{
	public final CAction checkForUpdatesAction = new CAction() { public void action() { actionCheckForUpdates(); }};

	
	public HelpMenu()
	{
		super(Menus.Help);
		add(new CMenuItem(Menus.ContactSupport, ContactSupport.action));
		add(new CMenuItem(Menus.CheckForUpdates, checkForUpdatesAction));
		addSeparator();
		add(new CMenuItem(Menus.License, Application.licenseAction()));
		add(new CMenuItem(Menus.OpenSourceLicenses, OpenSourceLicenses.openDialogAction));
		addSeparator();
		add(new CMenuItem(Menus.About, About.action));
	}
	
	
	public void actionCheckForUpdates()
	{
		new CheckForUpdatesDialog(this, NotebookApp.getUpdateURL(true)).open();
	}
}
