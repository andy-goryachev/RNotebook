// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.dialogs;
import goryachev.common.util.CKit;
import goryachev.i18n.Menus;
import goryachev.i18n.TXT;
import goryachev.swing.Application;
import goryachev.swing.CButton;
import goryachev.swing.CheckForUpdate;
import goryachev.swing.icons.CIcons;
import goryachev.swing.text.CDocumentBuilder;
import goryachev.swing.wizard.ProcessPanel;
import goryachev.swing.wizard.WizardDialog2;
import java.awt.Component;


public class CheckForUpdatesDialog
	extends WizardDialog2
{
	protected final CheckForUpdate check;
	
	
	public CheckForUpdatesDialog(Component parent, String checkUrl, String webSite)
	{
		super(parent, "CheckForUpdatesDialog", true);
		
		setTitle(TXT.get("CheckForUpdatesDialog.title", "Check for Updates"));
		setSize(450, 250);
		
		check = new CheckForUpdate(checkUrl);

		ProcessPanel p = new ProcessPanel(null, null, closeDialogAction)
		{
			protected void execute() throws Exception
			{
				check.readWeb();
				comfortSleep(400);
			}
			

			protected void onSuccess()
			{
				try
				{
					if(check.isUpdateAvailable(Application.getVersion()))
					{
						CDocumentBuilder b = new CDocumentBuilder();
						b.a(TXT.get("CheckForUpdatesDialog.version NUMBER is available", "A new version {0} is available.", check.getLatestVersion()));
						b.nl();
						b.a(TXT.get("CheckForUpdatesDialog.to download", "Please visit the web site to download the new version."));
						b.nl();
						b.link(webSite);
						
						setStatusIcon(CIcons.Info32);
						setDocument(b.getDocument());
					}
					else
					{
						setText(TXT.get("CheckForUpdatesDialog.latest version", "You've got the latest version."));
					}
				}
				catch(Exception e)
				{
					setErrorCard(TXT.get("CheckForUpdatesDialog.latest version.error", "Check for update failed: {0}.", CKit.getExceptionMessage(e)));
				}
				
				closeOnEscape();
			}
		};
		p.buttonPanel().setBorder(10);
		p.buttonPanel().add(new CButton(Menus.Cancel, p.cancelAction));
		p.buttonPanel().add(new CButton(Menus.Close, closeDialogAction, true));
		setCard(p, null);
		
		p.start();
		p.setText(TXT.get("CheckForUpdatesDialog.checking for updates", "Checking for updates..."));
	}
}
