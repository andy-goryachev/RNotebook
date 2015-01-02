// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.Application;
import goryachev.common.ui.BaseDialog;
import goryachev.common.ui.BasePanel;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CCheckBox;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.ContactSupport;
import goryachev.common.ui.InfoField;
import goryachev.common.ui.Menus;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.ui.options.BooleanOption;
import goryachev.common.util.ProductInfo;
import goryachev.common.util.TXT;
import java.awt.Component;


// TODO check for a new version here perhaps?
public class ThankYouDialog
	extends BaseDialog
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
		p.setLayout
		(
			new double[]
			{
				CPanel.FILL
			},
			new double[]
			{
				CPanel.FILL,
				CPanel.PREFERRED,
			}
		);
		p.add(0, 0, infoField);
		p.add(0, 1, doNotShowField);
		
		BasePanel bp = new BasePanel(p);
		bp.setBackground(Theme.fieldBG());
		bp.setIcon(CIcons.Success48);
		bp.buttons().addButton("Email Us", emailAction, Theme.alternativeButtonHighlight());
		bp.buttons().addButton(Menus.OK, okAction, true);
		
		setCenter(bp);
		
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
