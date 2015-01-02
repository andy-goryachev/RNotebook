// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.io.CReader;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CExtensionFileFilter;
import goryachev.common.ui.CMenu;
import goryachev.common.ui.CMenuBar;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.CToolBar;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.Menus;
import goryachev.common.ui.TButton;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.dialogs.CFileChooser;
import goryachev.common.ui.options.RecentFilesOption;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import goryachev.notebook.editor.NotebookPanel;
import goryachev.notebook.icons.NotebookIcons;
import goryachev.notebook.util.DataBookJsonReader;
import goryachev.notebook.util.DataBookJsonWriter;
import java.awt.Component;
import java.io.File;
import javax.swing.JMenuBar;


public class NotebookWindow
	extends AppFrame
{
	public final CAction openAction = new CAction() { public void action() { actionOpen(); } };
	public final CAction saveAction = new CAction() { public void action() { actionSave(); } };
	public final CAction saveAsAction = new CAction() { public void action() { actionSaveAs(); } };
	public final NotebookPanel notebookPanel;
	private final RecentFilesOption recentFilesOption;
	private static final String KEY_LAST_FILE = "last.file";
	public static final String EXTENSION = ".nbook";
	public static final CExtensionFileFilter FILE_FILTER = new CExtensionFileFilter("Notebook Files" + " (*" + EXTENSION + ")", EXTENSION);
	private File file;
	private boolean modified;
	
	
	public NotebookWindow()
	{
		super("NotebookWindow");
		
		recentFilesOption = new RecentFilesOption("recent.files")
		{
			protected void onRecentFileSelected(File f)
			{
				openNewWindow(f);
			}
		};
				
		notebookPanel = new NotebookPanel();
		
		setJMenuBar(createMenu());
		setNorth(createToolbar());
		setCenter(notebookPanel);
		setSouth(createStatusBar(true));
		
		setTitle(Application.getTitle() + " - " + Application.getVersion());
		setMinimumSize(500, 300);
		setSize(700, 900);

		setDataBook(Demo.createDataBook());
		updateActions();
	}
	
	
	private JMenuBar createMenu()
	{
		CMenuBar mb = Theme.menubar();
		CMenu m;

		// file
		mb.add(m = new CMenu(Menus.File));
		m.add(new CMenuItem(Menus.Open, openAction));
		m.add(recentFilesOption.recentFilesMenu());
		m.addSeparator();
		m.add(new CMenuItem(Menus.Save,  saveAction));
		m.add(new CMenuItem(Menus.SaveAs,  saveAsAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Preferences,  CAction.DISABLED));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Close, closeAction));
		
		// edit
		mb.add(m = new CMenu(Menus.Edit));
		m.add("Cut Cell");
		m.add("Copy Cell");
		m.add("Paste Cell Above");
		m.add("Paste Cell Below");
		m.add("Delete");
		m.addSeparator();
		m.add("Split Cell");
		m.add("Merge Cell Above");
		m.add("Merge Cell Below");
		m.addSeparator();
		m.add("Move Cell Up");
		m.add("Move Cell Down");
		m.addSeparator();
		m.add("Select Previous Cell");
		m.add("Select Next Cell");
		
		// view
		mb.add(m = new CMenu(Menus.View));
		m.add("Toggle Header ?");
		m.add("Toggle Toolbar ?");
		
		// insert
		mb.add(m = new CMenu(Menus.Insert));
		m.add("Insert Cell Above");
		m.add("Insert Cell Below");
		
		// cell
		mb.add(m = new CMenu("Cell"));
		m.add(new CMenuItem("Run", notebookPanel.runCurrentAction));
		m.add("Run in Place");
		m.add("Run All");
		m.addSeparator();
		m.add(new CMenuItem("Code", notebookPanel.toCodeAction));
		m.add("Markdown");
		m.add(new CMenuItem("Raw Text", notebookPanel.toTextAction));
		m.add(new CMenuItem("Heading 1", notebookPanel.toH1Action));
		m.add("Heading 2");
		m.add("Heading 3");
		m.add("Heading 4");
		m.add("Heading 5");
		m.add("Heading 6");
		
		// engine
		mb.add(m = new CMenu("Engine"));
		m.add("Interrupt");
		m.add("Restart");
		
		// help
		mb.add(new HelpMenu());
		
		return mb;
	}
	
	
	private Component createToolbar()
	{
		CToolBar t = Theme.toolbar();
		t.add(new TButton(NotebookIcons.Save, "Save", true, saveAction));
		t.space();
		t.add(new TButton(NotebookIcons.Cut, "Cut", true, CAction.DISABLED));
		t.add(new TButton(NotebookIcons.Copy, "Copy", true, CAction.DISABLED));
		t.add(new TButton(NotebookIcons.Paste, "Paste", true, CAction.DISABLED));
		t.space();
		t.add(new TButton(NotebookIcons.MoveUp, "Move Section Up", true, CAction.DISABLED));
		t.add(new TButton(NotebookIcons.MoveDown, "Move Section Down", true, CAction.DISABLED));
		t.space();
		t.add(new TButton(NotebookIcons.InsertAbove, "Insert Section Above", true, CAction.DISABLED));
		t.add(new TButton(NotebookIcons.InsertBelow, "Insert Section Below", true, CAction.DISABLED));
		t.space();
		t.add(new TButton(NotebookIcons.Start, "Run", true, notebookPanel.runCurrentAction));
		t.add(new TButton(NotebookIcons.Stop, "Interrupt", true, CAction.DISABLED));
		t.space();
		t.add(notebookPanel.typeField);
		return t;
	}
	
	
	protected void updateActions()
	{
		// TODO
	}
	
	
	public void setDataBook(DataBook b)
	{
		notebookPanel.setDataBook(b);
	}
	
	
	protected void setFile(File f)
	{
		recentFilesOption.add(f);
		file = f;
	}
	
	
	public File getFile()
	{
		return file;
	}
	
	
	public boolean isModified()
	{
		return modified;
	}
	
	
	public void setModified(boolean on)
	{
		modified = on;
	}
	
	
	protected void updateTitle()
	{
		SB sb = new SB();
		
		boolean dash = false;
		
		if(file != null)
		{
			sb.a(file.getName());
			sb.sp();
			dash = true;
		}
		
		if(isModified())
		{
			sb.a("* ");
			dash = true;
		}
		
		if(dash)
		{
			sb.a(" - ");
		}
		
		sb.a(Application.getTitle());
		sb.a(" ");
		sb.a(Application.getVersion());
		setTitle(sb.toString());
	}
	
	
	// opens in new window
	protected void actionOpen()
	{
		// TODO check if open
		
		CFileChooser fc = new CFileChooser(this, KEY_LAST_FILE);
		fc.setTitle(Menus.Open);
		fc.setApproveButtonText(Menus.Open);
		fc.setFileFilter(FILE_FILTER);
		File f = fc.openFileChooser();
		if(f != null)
		{
			openNewWindow(f);
		}
	}
	
	
	protected void actionSave()
	{
		// stopEditing();
		
		if(file == null)
		{
			actionSaveAs();
		}
		else
		{
			try
			{
				DataBookJsonWriter.saveJSON(notebookPanel.getDataBook(), file);
				setModified(false);
				updateTitle();
			}
			catch(Exception e)
			{
				Dialogs.err(this, e);
			}
		}
	}
	
	
	protected void actionSaveAs()
	{
		CFileChooser fc = new CFileChooser(this, KEY_LAST_FILE);
		fc.setTitle(Menus.SaveAs);
		fc.setApproveButtonText(Menus.Save);
		fc.setFileFilter(FILE_FILTER);
		File f = fc.openFileChooser();
		if(f != null)
		{
			f = CKit.ensureExtension(f, EXTENSION);
			
			if(f.exists())
			{
				if(!Dialogs.checkFileExistsOverwrite(this, f))
				{
					return;
				}
			}
			
			setFile(f);
			actionSave();
		}
	}
	
	
	protected void openNewWindow(File f)
	{
		// check if already open
		if(f != null)
		{
			for(NotebookWindow w: UI.getWindowsOfType(NotebookWindow.class))
			{
				if(f.equals(w.getFile()))
				{
					w.toFront();
					return;
				}
			}
		}
		
		NotebookWindow w = new NotebookWindow();
		UI.cascade(this, w);
		w.open();
		w.openFile(f);
	}
	
	
	// f may be null
	protected void openFile(File file)
	{
		// always in new window
//		if(isModified())
//		{
//			ChoiceDialog d = new ChoiceDialog(this, "File Modified", TXT.get("MainWindow.open.file modified", "{0} has been modified.  Save changes?", getFileName()));
//			d.addButton(Menus.Save, 0, true);
//			d.addButton(Menus.Cancel, 1);
//			d.addButton(Menus.DiscardChanges, 2);
//			int rv = d.openChoiceDialog();
//			switch(rv)
//			{
//			case 0:
//				save();
//				break;
//			case 1:
//				return;
//			case 2:
//				break;
//			default:
//				return;
//			}
//		}
		
		try
		{
			CReader rd = file == null ? null : new CReader(file);
			
			try
			{
				DataBook b = (rd == null ? new DataBook() : new DataBookJsonReader(rd).parse());

				setFile(file);
				setDataBook(b);
				updateTitle();
			}
			finally
			{
				CKit.close(rd);
			}
		}
		catch(Exception e)
		{
			Dialogs.err(this, e);
		}
	}
}
