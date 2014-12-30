// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.dialogs.AboutDialog;
import goryachev.common.ui.text.CDocumentBuilder;
import goryachev.notebook.icons.NotebookIcons;
import java.awt.Window;


public class About
{
	public static final CAction action = new CAction() { public void action() { about(getSourceWindow()); } };
	
	
	public static void about(Window parent)
	{
		CDocumentBuilder b = new CDocumentBuilder();
		b.a("Research Notebook").nl(2);
		
		b.a("Version").sp().a(Application.getVersion()).nl();
		b.a(Application.getCopyright()).nl(2);
		
		b.link(NotebookApp.WEB_SITE);
		
		AboutDialog d = new AboutDialog(parent);
		d.setLogo(NotebookIcons.Logo);
		d.setDocument(b.getDocument());
		d.open();
	}
}
