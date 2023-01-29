// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.ContactSupport;
import goryachev.common.util.ProductInfo;
import goryachev.i18n.Menus;
import goryachev.i18n.TXT;
import goryachev.swing.Application;
import goryachev.swing.CAction;
import goryachev.swing.CCheckBox;
import goryachev.swing.CDialog;
import goryachev.swing.CPanel;
import goryachev.swing.InfoField;
import goryachev.swing.Panels;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import goryachev.swing.icons.CIcons;
import goryachev.swing.options.BooleanOption;
import java.awt.Component;


// TODO check for a new version here perhaps?
public class ThankYouDialog
	extends CDialog
{
	public final CAction emailAction = new CAction() { public void action() { actionEmail(); } };
	public final CAction okAction = new CAction() { public void action() { actionOK(); } };
	public final InfoField infoField;
	public final CCheckBox doNotShowField;
	public static final BooleanOption doNotShowOption = new BooleanOption("do.not.show.reminder", false);
	
	
	public ThankYouDialog(Component parent)
	{
		super(parent, "ThankYouDialog", true);
		
		setDialogTitle(TXT.get("ThankYouDialog.title", "Thank you"));
		setMinimumSize(500, 300);
		setSize(500, 300);
		setCloseOnEscape();
		borderless();
		
		doNotShowField = new CCheckBox(Menus.DoNotShowThisDialog);
		
		String text =
			TXT.get("ThankYouDialog.thank you.1", "Thank you for using {0}!", Application.getTitle()) +
			"\n\n" +
			"We hope you like our product.  " +
			"Our engineers and testers spent countless hours making sure the product works, and works right.  " +
			"Should you, however, find that something is not working right, or can be improved — do not hesitate to tell us.  " +
			"\n\n" + 
			TXT.get("ThankYouDialog.thank you.4", "You can reach us at {0}", ProductInfo.getSupportEmail())
			;
		
		infoField = new InfoField(text, 0);
		infoField.setForeground(Theme.TEXT_FG);
		
		CPanel p = new CPanel();
		p.setBorder();
		p.setBackground(Theme.TEXT_BG);
		p.addColumn(CPanel.FILL);
		p.row(0, infoField);
		p.nextFillRow();
		p.nextRow();
		p.row(0, doNotShowField);
		p.setBackground(Theme.FIELD_BG);
		p.setLeading(Panels.iconField(CIcons.Success48));
		
		panel().setCenter(p);
		buttonPanel().addButton("Email Us", emailAction, Theme.DESTRUCTIVE_BUTTON_COLOR);
		buttonPanel().addButton(Menus.OK, okAction, true);
		
		UI.later(new Runnable()
		{
			public void run()
			{
				toFront();
			}
		});
	}
	
	
	protected void actionOK()
	{
		doNotShowOption.set(doNotShowField.isSelected());
		close();
	}
	
	
	protected void actionEmail()
	{
		ContactSupport.openMail(this, null);
	}


	public static void openDialog(final Component parent)
	{
		if(doNotShowOption.get() == false)
		{
			// open later to make sure all windows are open
			UI.later(new Runnable()
			{
				public void run()
				{
					new ThankYouDialog(parent).open();
				}
			});
		}
	}
}
