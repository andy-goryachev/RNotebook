// Copyright © 2008-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.swing.CAction;
import goryachev.swing.Dialogs;
import goryachev.swing.MailTools;
import java.awt.Component;


public class ContactSupport
{
	public static CAction action = new CAction() { public void action() { openMail(getSourceWindow(), null); }};
	
	
	public static void openMail(Component w, String body)
	{
		try
		{
			String email = RNotebookApp.SUPPORT_EMAIL;
			String subject = "Question about " + RNotebookApp.getTitle() + " ver. " + RNotebookApp.getVersion();
			MailTools.mail(email, subject, body);
		}
		catch(Exception e)
		{
			Dialogs.error(w, e);
		}
	}
}
