// Copyright (c) 2009-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.log;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.CSplitPane;
import goryachev.common.ui.CStatusBar;
import goryachev.common.ui.CToolBar;
import goryachev.common.ui.ClipboardTools;
import goryachev.common.ui.Img;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.table.CTableSelector;
import goryachev.common.ui.table.ZFilterLogic;
import goryachev.common.ui.table.ZTable;
import goryachev.common.util.CException;
import goryachev.common.util.ILogWriter;
import goryachev.common.util.Log;
import goryachev.common.util.TXT;
import goryachev.common.util.log.LogEntry;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.Timer;


public class VLogWindow
	extends AppFrame
	implements ILogWriter
{
	public static final int REFRESH_PERIOD = 500;
	public static final ImageIcon ICON_NORMAL = Img.local("vlog-normal.png");
	public static final ImageIcon ICON_ERROR = Img.local("vlog-error.png");
	public static final Color COLOR_ERROR = Color.red;
	
	public static final CAction openAction = new CAction(ICON_NORMAL) { public void action() { openWindow(); } };
	public final CAction copyAction = new CAction() { public void action() { onCopy(); } };
	public final CAction saveAction = new CAction() { public void action() { actionSave(); } };
	public final CAction clearAction = new CAction() { public void action() { actionClear(); } };
	public final CAction preferencesAction = new CAction() { public void action() { actionPreferences(); } };
	protected static VLogWindow instance;
	public final VLogModel model;
	public final ZTable table;
	public final JTextArea detail;
	public final Timer timer;
	public final ZFilterLogic filterLogic;
	public final CTableSelector selector;


	protected VLogWindow()
	{
		super("VLogWindow");
		setTitle(Application.getTitle() + " - " + TXT.get("LogWindow.title", "Error Log"));
		setSize(1000, 700);
		
		// FIX
		saveAction.setEnabled(false);
		preferencesAction.setEnabled(false);

		model = new VLogModel();
		
		table = new ZTable(model);
		table.setSortable(true);
		
		filterLogic = new ZFilterLogic(table);
		filterLogic.setPrompt("find...");
		
		selector = new CTableSelector(table)
		{
			public void tableSelectionChangeDetected()
			{
				onSelectionChange();
			}
		};
		
		CBorder border = new CBorder();
		
		CScrollPane tableScroll = new CScrollPane(table, CScrollPane.VERTICAL_SCROLLBAR_ALWAYS, CScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tableScroll.setBorder(border);
		tableScroll.getViewport().setBackground(Theme.textBG());
		
		detail = new JTextArea();
		detail.setWrapStyleWord(true);
		detail.setLineWrap(true);
		detail.setFont(Theme.plainFont());
		detail.setEditable(false);
		detail.setBorder(new CBorder(2));
		
		CScrollPane detailScroll = new CScrollPane(detail, CScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, CScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		detailScroll.setBorder(border);
		
		CSplitPane split = new CSplitPane(CSplitPane.VERTICAL_SPLIT, tableScroll, detailScroll);
		split.setDividerLocation(200);
		
		CPanel p = new CPanel();
		p.setNorth(createToolbar());
		p.setCenter(split);
		p.setSouth(createStatusBar());
		
		setContent(p);
		
		timer = new Timer(REFRESH_PERIOD, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				model.updateModel();
			}
		});
		timer.setInitialDelay(10);
		
		UI.whenAncestorOfFocusedComponent(getRootPane(), KeyEvent.VK_ESCAPE, closeAction);
	}
	

	public boolean onWindowClosing()
	{
		Log.removeMonitor(this);
		timer.stop();
		instance = null;
		return true;
	}
	

	public void onWindowOpened()
	{
		Log.addMonitor(this);
	}


	protected JComponent createToolbar()
	{
		CToolBar p = Theme.toolbar();
		p.add(filterLogic.getComponent());
		p.space();

		// FIX
		p.add(Theme.tbutton("Exception!", new CAction()
		{
			public void action()
			{
				try
				{
					// TODO also uncaught
					throw new CException("this is a test exception");
				}
				catch(Exception e)
				{
					Log.err(e);
				}
			}
		}));
		
		p.add(Theme.tbutton(TXT.get("LogWindow.button.copy","Copy"), copyAction));
		p.add(Theme.tbutton(TXT.get("LogWindow.button.save","Save"), saveAction));
		p.add(Theme.tbutton(TXT.get("LogWindow.button.clear","Clear"), clearAction));
		
		p.fill();
		
		p.add(Theme.tbutton(TXT.get("LogWindow.button.preferences","Preferences"), preferencesAction));
		return p;
	}
	
	
	public JComponent createStatusBar()
	{
		CStatusBar b = new CStatusBar();
		b.fill();
		b.copyright();
		return b;
	}
	
	
	public Window open()
	{
		super.open();
		timer.start();
		return this;
	}
	
	
//	protected static void setErrorState(boolean on)
//	{
//		ImageIcon icon = on ? ICON_ERROR : ICON_NORMAL;
//		openAction.setIcon(icon);
//	}
	
	
	protected void onSelectionChange()
	{
		LogEntry en = model.getSelectedEntry(selector);
		setEntry(en);
	}
	
	
	protected void setEntry(LogEntry x)
	{
		if(x == null)
		{
			detail.setText(null);
		}
		else
		{
			detail.setText(x.getText());
			detail.setCaretPosition(0);
		}
	}
	
	
	protected void onCopy()
	{
		StringBuilder sb = new StringBuilder();
		int sz = model.size();
		for(int i=0; i<sz; i++)
		{
			LogEntry ev = model.getItem(i);
			sb.append(format(ev.getTimestamp())).append(" ");
			sb.append(ev.getText()).append("\n");
		}
		
		ClipboardTools.copy(sb.toString());
	}
	
	
	protected static String format(long time)
	{
		// TODO option
		return new Date(time).toString();
	}


	public static void openWindow()
	{
		if(instance == null)
		{
			instance = new VLogWindow();
			instance.open();
		}
		else
		{
			instance.toFront();
		}
	}


	protected void actionSave()
	{
		// TODO
	}


	protected void actionPreferences()
	{
		// TODO
	}


	protected void actionClear()
	{
		model.clear();
	}
	

	public void write(LogEntry en)
	{
		model.addEntry(en);
	}
}
