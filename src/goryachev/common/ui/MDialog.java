// Copyright (c) 2012-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.dialogs.StandardDialogPanel;
import goryachev.common.ui.icons.CIcons;
import java.awt.Component;
import javax.swing.Icon;


/** 
 * One more try to make convenient standard message dialog,
 * after StandardDialog and Dialogs.
 */
@Deprecated
public class MDialog
	extends CDialog
{
	public final StandardDialogPanel panel;
	
	
	public MDialog(Component parent)
	{
		super(parent, "MDialog", true);
		
		setMinimumSize(500, 300);
		
		panel = new StandardDialogPanel();

		setContent(panel);
		getContentPanel().setBorder(null);
	}
	
	
	protected CButtonPanel createButtonPanel()
	{
		CButtonPanel p = new CButtonPanel();
		p.setBorder(new CBorder(MARGIN));
		return p;
	}
	
	
	public void setTextError(Throwable e)
	{
		panel.setTextError(e);
	}
	
	
	public void setMessage(String s)
	{
		panel.setTextPlain(s);
	}
	
	
	public void setMessageHtml(String s)
	{
		panel.setTextHtml(s);
	}
	
	
	public void setLogo(Icon ic)
	{
		panel.setIcon(ic);
	}
	
	
	public void setIconQuestion()
	{
		setLogo(CIcons.Question96);
	}
	
	
	public void setIconWarning()
	{
		setLogo(CIcons.Warning96);
	}
	
	
	public void setIconInfo()
	{
		setLogo(CIcons.Info96);
	}
	
	
	public void setIconSuccess()
	{
		setLogo(CIcons.Success96);
	}
	
	
	public void setIconCancelled()
	{
		setLogo(CIcons.Cancelled96);
	}
}
