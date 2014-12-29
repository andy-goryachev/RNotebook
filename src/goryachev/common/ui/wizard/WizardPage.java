// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.InfoField;
import goryachev.common.ui.UI;
import goryachev.common.util.CancelledException;
import goryachev.common.util.Committable;
import goryachev.common.util.NamedObjects;
import java.awt.Window;
import javax.swing.JComponent;


@Deprecated // use new wizard
public abstract class WizardPage
	implements Committable
{
	public abstract String getTitle();
	
	public abstract JComponent getComponent();
	
	public abstract JComponent createButtons();
	
	/** thrown CancelledException will not result in an error dialog */
	public abstract void commit() throws Exception;
	
	/** called the first time a page becomes visible */
	protected void onWizardPageActivated() { }
	
	/** called each time a page becomes visible */
	protected void onWizardPageSelected() { }
	
	// replace these with onWizardPageXXX
	@Deprecated
	protected final void activated() { }
	@Deprecated
	protected final void selected() { }
	@Deprecated
	protected final void setActivated(boolean on) { }
	
	//
	
	public final CAction closeWizardAction = new CAction() { public void action() { closeWizard(); }};
	public final CAction prevAction = new CAction() { public void action() { toPrevPage(); }};
	public final CAction nextAction = new CAction() { public void action() { toNextPage(); }};
	
	private WizardPanel wizardPanel;
	private JComponent buttons;
	private boolean enabled = true;
	private boolean completed;
	private boolean activated;
	
	
	public WizardPage()
	{
		this.enabled = true;
	}
	
	
	protected void setWizardPanel(WizardPanel p)
	{
		wizardPanel = p;
	}
	
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	
	public void setEnabled(boolean on)
	{
		enabled = on;
	}
	
	
	public boolean isCompleted()
	{
		return completed;
	}
	
	
	public void setCompleted(boolean on)
	{
		completed = on;
		
		WizardPanel p = getWizard();
		if(p != null)
		{
			p.refreshTable();
		}
	}
	
	
	protected void notifyPageOpened()
	{
		if(!activated)
		{
			onWizardPageActivated();
			activated = true;
		}
		
		onWizardPageSelected();
	}

	
	public JComponent getButtons()
	{
		if(buttons == null)
		{
			buttons = createButtons();
		}
		return buttons;
	}
	
	
	public WizardPanel getWizard()
	{
		return wizardPanel;
	}
	
	
	protected void toFirstPage()
	{
		WizardPanel w = getWizard();
		WizardPage p = w.getFirstPage();
		if(p != null)
		{
			if(p.isEnabled() == false)
			{
				p.setEnabled(true);
				w.refreshTable();
			}

			w.setSelectedPage(p);
		}
	}
	
	
	protected void toPrevPage()
	{
		WizardPanel w = getWizard();
		WizardPage p = w.getPrevPage();
		if(p != null)
		{
			if(p.isEnabled() == false)
			{
				p.setEnabled(true);
				w.refreshTable();
			}

			w.setSelectedPage(p);
		}
	}
	
	
	protected void toNextPage()
	{
		// deactivate first?
		
		try
		{
			commit();
			
			WizardPanel w = getWizard();
			setCompleted(true);
			w.refreshTable();
			
			WizardPage p = w.getNextPage();
			if(p != null)
			{
				if(p.isEnabled() == false)
				{
					p.setEnabled(true);
					w.refreshTable();
				}
				
				w.setSelectedPage(p);
				
				//p.setActivated(true);
				
				//p.onWizardPageSelected();
			}
		}
		catch(CancelledException e)
		{
			// as if nothing happened
		}
		catch(Exception e)
		{
			Dialogs.error(getComponent(), e);
		}
	}
	
	
	public void closeWizard()
	{
		Window w = UI.getParentWindow(getWizard());
		if(w instanceof AppFrame)
		{
			((AppFrame)w).close();
		}
		else if(w instanceof CDialog)
		{
			((CDialog)w).close();
		}
		else
		{
			w.dispose();
		}
	}
	
	
	public void grabAttention(JComponent c)
	{
		new AttentionGrabber(c).start();
	}
	
	
	public Object setValue(String key, Object value)
	{
		return getNamedObjects().put(key, value);
	}
	
	
	public Object getValue(String key)
	{
		return getNamedObjects().get(key);
	}
	
	
	public NamedObjects getNamedObjects()
	{
		return getWizard().getNamedObjects();
	}
	
	
	public void setComponent(JComponent c)
	{
		getWizard().setDetailPane(c);
	}
	
	
	protected InfoField infoField(String text)
	{
		InfoField p = new InfoField(text);
		p.setBorder(new CBorder(10));
		return p;
	}
}