// Copyright © 2014-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.notebook.icons.NotebookIcons;
import goryachev.swing.Application;
import goryachev.swing.CAction;
import goryachev.swing.dialogs.AboutDialog;
import goryachev.swing.text.CDocumentBuilder;
import java.awt.Window;


public class About
{
	public static final CAction action = new CAction() { public void action() { about(getSourceWindow()); } };
	
	
	public static void about(Window parent)
	{
		CDocumentBuilder b = new CDocumentBuilder();
		b.a(Application.getTitle()).nl(2);
		
		b.a("Version").sp().a(Application.getVersion()).nl();
		b.a(Application.getCopyright()).nl(2);
		
		b.link(RNotebookApp.WEB_SITE);
		
		AboutDialog d = new AboutDialog(parent);
		d.setLogo(NotebookIcons.Logo);
		d.setDocument(b.getDocument());
		d.open();
	}
}
