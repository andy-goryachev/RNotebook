// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CCheckBox;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.ContactSupport;
import goryachev.common.ui.InfoField;
import goryachev.common.ui.Menus;
import goryachev.common.ui.Panels;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.ui.options.BooleanOption;
import goryachev.common.util.ProductInfo;
import goryachev.common.util.TXT;
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
		infoField.setForeground(Theme.textFG());
		
		CPanel p = new CPanel();
		p.setBorder();
		p.setBackground(Theme.textBG());
		p.addColumn(CPanel.FILL);
		p.row(0, infoField);
		p.nextFillRow();
		p.nextRow();
		p.row(0, doNotShowField);
		p.setBackground(Theme.fieldBG());
		p.setLeading(Panels.iconField(CIcons.Success48));
		
		panel().setCenter(p);
		buttonPanel().addButton("Email Us", emailAction, Theme.alternativeButtonHighlight());
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
