// Copyright (c) 2012-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.UI;
import java.awt.event.KeyEvent;


@Deprecated
public abstract class WizardFrame
	extends AppFrame
{
	public final WizardPanel wizardPanel;
	
	
	public WizardFrame(String name, boolean disableNavigation)
	{
		super(name);
		setMinimumSize(750, 600);
		
		wizardPanel = new WizardPanel(disableNavigation);
		setContent(wizardPanel);
	}
	

	public void onWindowOpened()
	{
		if(wizardPanel.getSelectedPage() == null)
		{
			wizardPanel.selectFirstRow();
		}
	}
	

	public boolean onWindowClosing()
	{
		return true;
	}

	
	public void addPage(WizardPage p)
	{
		wizardPanel.addPage(p);
	}
	
	
	public int getPageCount()
	{
		return wizardPanel.getPageCount();
	}
	
	
	public boolean containsPage(WizardPage p)
	{
		return wizardPanel.containsPage(p);
	}
	
	
	public void insertPage(int index, WizardPage p)
	{
		wizardPanel.insertPage(index, p);
	}
	
	
	public void insertLastPage(WizardPage p)
	{
		wizardPanel.insertLastPage(p);
	}
	
	
	public void insertAfterPage(WizardPage afterWhich, WizardPage p)
	{
		wizardPanel.insertAfterPage(afterWhich, p);
	}
	
	
	public void removePage(WizardPage p)
	{
		wizardPanel.removePage(p);
	}
	
	
	public void removeAllButPage(WizardPage p)
	{
		wizardPanel.removeAllButPage(p);
	}
	
	
	public void removeAllButPages(WizardPage ... ps)
	{
		wizardPanel.removeAllButPages(ps);
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
