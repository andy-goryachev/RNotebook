// Copyright (c) 2012-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.UI;
import java.awt.Component;
import java.awt.event.KeyEvent;


@Deprecated
public abstract class WizardDialog
	extends CDialog
{
	public final WizardPanel wizardPanel;
	
	
	public WizardDialog(Component parent, String name, boolean disableNavigation)
	{
		super(parent, name, true);
		setMinimumSize(750, 600);
		
		wizardPanel = new WizardPanel(disableNavigation);
		
		setContent(wizardPanel);
		getContentPanel().setBorder(null);
	}
	

	public void onWindowOpened()
	{
		wizardPanel.selectFirstRow();
	}
	

	public boolean onWindowClosing()
	{
		return true;
	}

	
	public void addPage(WizardPage p)
	{
		wizardPanel.addPage(p);
	}
	
	
	public Object setValue(String key, Object value)
	{
		return wizardPanel.getNamedObjects().put(key, value);
	}
	
	
	public Object getValue(String key)
	{
		return wizardPanel.getNamedObjects().get(key);
	}
	
	
	public void setCloseOnEscape()
	{
		UI.whenAncestorOfFocusedComponent(getRootPane(), KeyEvent.VK_ESCAPE, closeAction);
	}
	
	
	public void setSelectedPage(WizardPage p)
	{
		wizardPanel.setSelectedPage(p);
	}
}
