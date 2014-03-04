// Copyright (c) 2013-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.dialogs.ProcessPage_OLD;
import goryachev.common.ui.dialogs.StandardDialogPanel;
import goryachev.common.ui.icons.CIcons;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.Timer;


@Deprecated // to be replaced by BaseDialog maybe?
public abstract class StandardOperation
{
	public abstract JComponent createStartPage(CDialog parent);
	
	public abstract void execute() throws Exception;
	
	public abstract Progress getProgress();
	
	public abstract JComponent createResultPage(CDialog parent);
	
	/** called on background thread completion, in EDT */
	protected void onCompletion() { }
	
	//
	
	private String name;
	private String title;
	protected StandardOperationDialog dialog;
	protected volatile boolean cancelled;
	protected int updatePeriod = 500;
	protected int initialDelay = 150;
	
	
	public StandardOperation(String name, String title)
	{
		this.name = name;
		this.title = title;
	}
	
	
	public String getTitle()
	{
		return title;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	protected boolean isModal()
	{
		return true;
	}
	
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	
	protected void setCancelled(boolean on)
	{
		cancelled = on;
	}
	
	
	public void openDialog(Component parent)
	{
		dialog = new StandardOperationDialog(parent, getName(), isModal());
		dialog.open();
	}
	
	
	protected void initStartPage(StandardOperationDialog d)
	{
		CButtonPanel bp = d.getButtonPanel();
		bp.removeAll();
		bp.addButton(Menus.Close, d.closeAction);
		bp.addButton(Menus.Start, d.startAction, true);
		
		JComponent c = createStartPage(d);
		d.setPage(c);
	}
	
	
	protected ProcessPage_OLD createProcessPage(StandardOperationDialog d)
	{
		return new ProcessPage_OLD();
	}
	
	
	protected void initResultPage(StandardOperationDialog d, Throwable err)
	{
		CButtonPanel bp = d.getButtonPanel();
		bp.removeAll();
		bp.addButton(Menus.Close, d.closeAction, true);
		
		if(err == null)
		{
			JComponent c = createResultPage(d);
			d.setPage(c);
		}
		else
		{
			StandardDialogPanel c = constructErrorPage();
			c.setTextError(err);
			d.setPage(c);
		}
	}
	
	
	protected void initCancelledPage(StandardOperationDialog d)
	{
		CButtonPanel bp = d.getButtonPanel();
		bp.removeAll();
		bp.addButton(Menus.Close, d.closeAction, true);
		
		StandardDialogPanel p = new StandardDialogPanel();
		p.setIcon(CIcons.Cancelled96);
		p.setTextPlain("Cancelled");
		d.setPage(p);
	}
	
	
	/** constructs a standard error page */
	public StandardDialogPanel constructErrorPage()
	{
		StandardDialogPanel p = new StandardDialogPanel();
		p.setIcon(CIcons.Error96);
		return p;
	}
	
	
	/** constructs a standard success page */
	public StandardDialogPanel constructSuccessPage()
	{
		StandardDialogPanel p = new StandardDialogPanel();
		p.setIcon(CIcons.Success96);
		return p;
	}
	
	
	/** constructs a standard info page */
	public StandardDialogPanel constructInfoPage()
	{
		StandardDialogPanel p = new StandardDialogPanel();
		p.setIcon(CIcons.Info96);
		return p;
	}
	
	
	/** constructs a standard warning page */
	public StandardDialogPanel constructWarningPage()
	{
		StandardDialogPanel p = new StandardDialogPanel();
		p.setIcon(CIcons.Warning96);
		return p;
	}

	
	//
	
	
	public class StandardOperationDialog
		extends CDialog
	{
		public final CAction cancelAction = new CAction() { public void action() { actionCancel(); } };
		public final CAction startAction = new CAction() { public void action() { actionStart(); } };
		protected BackgroundThread thread;
		protected Timer timer;
		protected long start;
		protected ProcessPage_OLD progressPage;
		
	
		public StandardOperationDialog(Component parent, String name, boolean modal)
		{
			super(parent, name, modal);
			setSize(500, 350);
			setTitle(StandardOperation.this.getTitle());
			
			contentPanel.setBorder(null);
			
			initStartPage(this);
		}
		
		
		protected CButtonPanel createButtonPanel()
		{
			CButtonPanel p = new CButtonPanel();
			p.setBorder(new CBorder(10));
			return p;
		}


		public boolean onWindowClosing()
		{
			if(thread != null)
			{
				int rv = Dialogs.choice
				(
					this, 
					"Operation is Running", 
					null, 
					"Do you want to interrupt this operation?",
					new String[]
					{
						"Allow to Continue",
						"Interrupt"
					}
				);
				
				if(rv == 1)
				{
					actionCancel();
				}
				else
				{
					return false;
				}
			}
			return true;
		}


		public void setPage(JComponent c)
		{
			CPanel p = getContentPanel();
			p.setCenter(c);
			p.validate();
			p.repaint();
		}
		
		
		protected void actionStart()
		{
			CButtonPanel bp = getButtonPanel();
			bp.removeAll();
			bp.addButton(Menus.Cancel, cancelAction);
			bp.addButton(Menus.Close, closeAction, true);
			
			progressPage = createProcessPage(this);
			setPage(progressPage);
			
			start = System.currentTimeMillis();
			
			timer = new Timer(updatePeriod, new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					updateProgress();
				}	
			});
			timer.setInitialDelay(initialDelay);
			timer.start();
			
			thread = new BackgroundThread(StandardOperation.this.getName())
			{
				public void process() throws Throwable
				{
					execute();
				}

				public void success()
				{
					onThreadCompletion(null);
				}

				public void onError(Throwable e)
				{
					onThreadCompletion(e);
				}
			};
			thread.start();
			
			updateActions();
		}
		
		
		protected void actionCancel()
		{
			setCancelled(true);
			
			if(thread != null)
			{
				thread.cancel();
			}
		}
		
		
		protected void updateProgress()
		{
			progressPage.updateProgress(start, getProgress(), isCancelled());
		}
		
		
		protected void onThreadCompletion(Throwable e)
		{
			onCompletion();
			
			timer.stop();
			boolean cancelled = thread.isCancelled();
			thread = null;
			updateActions();
						
			if(cancelled)
			{
				initCancelledPage(this);
			}
			else
			{
				initResultPage(this, e);
			}
		}
		
		
		protected void updateActions()
		{
			boolean running = (thread != null);
			closeAction.setEnabled(!running);
			cancelAction.setEnabled(running);
		}
	}
}
