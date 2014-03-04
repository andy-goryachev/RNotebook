// Copyright (c) 2008-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.MailTools;
import goryachev.common.util.ProductInfo;
import goryachev.common.util.TXT;
import java.awt.Component;


public class ContactSupport
{
	public static CAction action = new CAction() { public void action() { openMail(getSourceWindow(), null); }};
	
	
	public static void openMail(Component w, String body)
	{
		try
		{
			String email = ProductInfo.getSupportEmail();
			String subject = TXT.get("ContactSupport.subject.question about PRODUCT", "Question about {0}", Application.getTitle());
			MailTools.mail(email, subject, body);
		}
		catch(Exception e)
		{
			Dialogs.error(w, e);
		}
	}
}
