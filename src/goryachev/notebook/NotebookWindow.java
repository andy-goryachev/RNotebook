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
import goryachev.common.ui.ChoiceDialog;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.Menus;
import goryachev.common.ui.TButton;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.dialogs.CFileChooser;
import goryachev.common.ui.options.RecentFilesOption;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import goryachev.common.util.TXT;
import goryachev.notebook.editor.NotebookPanel;
import goryachev.notebook.icons.NotebookIcons;
import goryachev.notebook.util.DataBookJsonReader;
import goryachev.notebook.util.DataBookJsonWriter;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import javax.swing.JMenuBar;


public class NotebookWindow
	extends AppFrame
{
	public final CAction newAction = new CAction() { public void action() { actionNew(); } };
	public final CAction openAction = new CAction() { public void action() { actionOpen(); } };
	public final CAction saveAction = new CAction() { public void action() { actionSave(); } };
	public final CAction saveAsAction = new CAction() { public void action() { actionSaveAs(); } };
	public final NotebookPanel np;
	protected final RecentFilesOption recentFilesOption;
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
				
		np = new NotebookPanel();
		
		setJMenuBar(createMenu());
		setNorth(createToolbar());
		setCenter(np);
		setSouth(createStatusBar(true));
		
		setTitle(Application.getTitle() + " - " + Application.getVersion());
		setMinimumSize(500, 300);
		setSize(700, 900);

		//setDataBook(Demo.createDataBook());
		
		updateActions();
	}
	
	
	private JMenuBar createMenu()
	{
		CMenuBar mb = Theme.menubar();
		CMenu m;

		// file
		mb.add(m = new CMenu(Menus.File));
		m.add(new CMenuItem(Menus.New, newAction));
		m.add(new CMenuItem(Menus.Open, openAction));
		m.add(recentFilesOption.recentFilesMenu());
		m.addSeparator();
		m.add(new CMenuItem(Menus.Save, Accelerators.SAVE, saveAction));
		m.add(new CMenuItem(Menus.SaveAs, Accelerators.SAVE_AS, saveAsAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Preferences, Accelerators.PREFERENCES, OptionsDialog.openDialogAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Close, Accelerators.CLOSE_WINDOW, closeAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Exit, Application.exitAction));
		
		// edit
		mb.add(m = new CMenu(Menus.Edit));
		m.add(new CMenuItem("Cut Cell", CAction.DISABLED));
		m.add(new CMenuItem("Copy Cell", CAction.DISABLED));
		m.add(new CMenuItem("Paste Cell Above", CAction.DISABLED));
		m.add(new CMenuItem("Paste Cell Below", CAction.DISABLED));
		m.add(new CMenuItem("Delete", np.deleteCellAction));
		m.addSeparator();
		m.add(new CMenuItem("Split Cell", CAction.DISABLED));
		m.add(new CMenuItem("Merge Cell Above", CAction.DISABLED));
		m.add(new CMenuItem("Merge Cell Below", CAction.DISABLED));
		m.addSeparator();
		m.add(new CMenuItem("Move Cell Up", CAction.DISABLED));
		m.add(new CMenuItem("Move Cell Down", CAction.DISABLED));
		m.addSeparator();
		m.add(new CMenuItem("Select Previous Cell", CAction.DISABLED));
		m.add(new CMenuItem("Select Next Cell", CAction.DISABLED));
		
		// view
		mb.add(m = new CMenu(Menus.View));
		m.add(new CMenuItem("Toggle Header ?", CAction.DISABLED));
		m.add(new CMenuItem("Toggle Toolbar ?", CAction.DISABLED));
		
		// insert
		mb.add(m = new CMenu(Menus.Insert));
		m.add(new CMenuItem("Insert Cell Above", np.insertCellAboveAction));
		m.add(new CMenuItem("Insert Cell Below", np.insertCellBelowAction));
		
		// cell
		mb.add(m = new CMenu("Cell"));
		m.add(new CMenuItem("Run", np.runCurrentAction));
		m.add("Run in Place");
		m.add("Run All");
		m.addSeparator();
		m.add(new CMenuItem("Code", np.toCodeAction));
		m.add("Markdown");
		m.add(new CMenuItem("Raw Text", np.toTextAction));
		m.add(new CMenuItem("Heading 1", np.toH1Action));
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
		t.add(new TButton(NotebookIcons.InsertAbove, "Insert Cell Above", true, np.insertCellAboveAction));
		t.add(new TButton(NotebookIcons.InsertBelow, "Insert Cell Below", true, np.insertCellBelowAction));
		t.space();
		t.add(new TButton(NotebookIcons.Start, "Run", true, np.runCurrentAction));
		t.add(new TButton(NotebookIcons.Stop, "Interrupt", true, CAction.DISABLED));
		t.space();
		t.add(np.typeField);
		return t;
	}
	
	
	public boolean onWindowClosing()
	{
		// TODO check if running
		
		if(isModified())
		{
			ChoiceDialog d = new ChoiceDialog(this, "File Modified", TXT.get("MainWindow.open.file modified", "{0} has been modified.  Save changes?", getFileName()));
			d.addButton(Menus.DiscardChanges, 2);
			d.addButton(Menus.Cancel, 1);
			d.addButton(Menus.Save, 0, true);
			int rv = d.openChoiceDialog();
			switch(rv)
			{
			case 0:
				actionSave();
				break;
			case 1:
				return false;
			case 2:
				break;
			default:
				return false;
			}
		}
		
		return true;
	}
	
	
	protected void updateActions()
	{
		// TODO
	}
	
	
	public void setDataBook(DataBook b)
	{
		np.setDataBook(b);
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
	
	
	public String getFileName()
	{
		if(file != null)
		{
			return file.getName();
		}
		return "Untitled";
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
			sb.a("*");
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
	
	
	/** on exiting check if modified and ask to save/save all. return true if the app can be closed */
	public static boolean askToSaveAllOnExit()
    {
		// TODO store open board file names
		
		boolean saveAll = false;
		
		for(NotebookWindow w: UI.getWindowsOfType(NotebookWindow.class))
		{
			if(w.isModified())
			{
				if(saveAll)
				{
					w.actionSave();
				}
				else
				{
					ChoiceDialog d = new ChoiceDialog(w, "File Modified", TXT.get("MainWindow.save on exit.file exists", "{0} has been modified.  Save changes?", w.getFileName()));
					d.addButton(Menus.Cancel, 3);
					d.addButton(Menus.DiscardChanges, 2);
					d.addButton(Menus.SaveAll, 1, Color.magenta);
					d.addButton(Menus.Save, 0, true);
					int rv = d.openChoiceDialog();
					switch(rv)
					{
					case 0:
						w.actionSave();
						break;
					case 1:
						w.actionSave();
						saveAll = true;
						break;
					case 2:
						break;
					default:
						return false;
					}
				}
			}
		}
		
	    return true;
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
				DataBook b = np.getDataBook();
				DataBookJsonWriter.saveJSON(b, file);
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
		// TODO perhaps warn the user
//		if(f != null)
//		{
//			for(NotebookWindow w: UI.getWindowsOfType(NotebookWindow.class))
//			{
//				if(f.equals(w.getFile()))
//				{
//					w.toFront();
//					return;
//				}
//			}
//		}
		
		NotebookWindow w = new NotebookWindow();
		UI.cascade(this, w);
		w.open();
		w.openFile(f);
	}
	
	
	protected void actionNew()
	{
		openNewWindow(null);
	}
	
	
	// f may be null
	protected void openFile(File file)
	{
		// FIX open in the same window
		
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
