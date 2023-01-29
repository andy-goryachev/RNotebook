// Copyright © 2014-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.io.CReader;
import goryachev.common.ui.options.RecentFilesOption;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import goryachev.i18n.Menus;
import goryachev.i18n.TXT;
import goryachev.notebook.cell.NotebookPanel;
import goryachev.notebook.icons.NotebookIcons;
import goryachev.notebook.util.DataBookJsonReader;
import goryachev.notebook.util.DataBookJsonWriter;
import goryachev.notebook.util.FileFilters;
import goryachev.swing.AppFrame;
import goryachev.swing.Application;
import goryachev.swing.CAction;
import goryachev.swing.CMenu;
import goryachev.swing.CMenuBar;
import goryachev.swing.CMenuItem;
import goryachev.swing.CToolBar;
import goryachev.swing.ChoiceDialog;
import goryachev.swing.Dialogs;
import goryachev.swing.TButton;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import goryachev.swing.XAction;
import goryachev.swing.dialogs.CFileChooser;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import javax.swing.JMenuBar;
import research.dhtml3.HDocument;


public class MainWindow
	extends AppFrame
{
	public final CAction exportHtmlAction = new CAction() { public void action() { actionExportHtml(); } };
	public final CAction newAction = new CAction() { public void action() { actionNew(); } };
	public final CAction openAction = new CAction() { public void action() { actionOpen(); } };
	public final CAction saveAction = new CAction() { public void action() { actionSave(); } };
	public final CAction saveAsAction = new CAction() { public void action() { actionSaveAs(); } };
	public final NotebookPanel np;
	protected final RecentFilesOption recentFilesOption;
	private static final String KEY_LAST_FILE = "last.file";
	private static final String KEY_LAST_HTML_FILE = "last.html.file";
	private File file;
	private boolean askedToSave;
	
	
	public MainWindow()
	{
		super("MainWindow");
		
		recentFilesOption = new RecentFilesOption("recent.files")
		{
			protected void onRecentFileSelected(File f)
			{
				openRecentFile(f);
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
		m.add(new CMenuItem("Export PDF", XAction.DISABLED));
		m.add(new CMenuItem("Export HTML", exportHtmlAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Preferences, Accelerators.PREFERENCES, OptionsDialog.openDialogAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Close, Accelerators.CLOSE_WINDOW, closeAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Exit, Application.exitAction));
		
		// edit
		mb.add(m = new CMenu(Menus.Edit));
		m.add(new CMenuItem("Cut Cell", XAction.DISABLED));
		m.add(new CMenuItem("Copy Cell", XAction.DISABLED));
		m.add(new CMenuItem("Paste Cell Above", XAction.DISABLED));
		m.add(new CMenuItem("Paste Cell Below", XAction.DISABLED));
		m.add(new CMenuItem("Delete", np.deleteCellAction));
		m.addSeparator();
		m.add(new CMenuItem("Split Cell", np.splitCellAction));
		m.add(new CMenuItem("Merge Cell Above", np.mergeCellAboveAction));
		m.add(new CMenuItem("Merge Cell Below", np.mergeCellBelowAction));
		m.addSeparator();
		m.add(new CMenuItem(NotebookIcons.MoveUp, "Move Cell Up", np.moveCellUpAction));
		m.add(new CMenuItem(NotebookIcons.MoveDown, "Move Cell Down", np.moveCellDownAction));
		m.addSeparator();
		m.add(new CMenuItem("Select Previous Cell", Accelerators.SELECT_PREV_CELL, np.selectPreviousCellAction));
		m.add(new CMenuItem("Select Next Cell", Accelerators.SELECT_NEXT_CELL, np.selectNextCellAction));
		
		// view
		mb.add(m = new CMenu(Menus.View));
		m.add(new CMenuItem("Toggle Header ?", XAction.DISABLED));
		m.add(new CMenuItem("Toggle Toolbar ?", XAction.DISABLED));
		
		// insert
		mb.add(m = new CMenu(Menus.Insert));
		m.add(new CMenuItem(NotebookIcons.InsertAbove, "Insert Cell Above", Accelerators.INSERT_CELL_ABOVE, np.insertCellAboveAction));
		m.add(new CMenuItem(NotebookIcons.InsertBelow, "Insert Cell Below", Accelerators.INSERT_CELL_BELOW, np.insertCellBelowAction));
		
		// cell
		mb.add(m = new CMenu("Cell"));
		m.add(new CMenuItem("Run", Accelerators.RUN_CELL, np.runCellAction));
		m.add(new CMenuItem(NotebookIcons.Start, "Run in Place", Accelerators.RUN_IN_PLACE, np.runInPlaceAction));
		m.add(new CMenuItem("Run All", Accelerators.RUN_ALL, np.runAllAction));
		m.addSeparator();
		m.add(new CMenuItem("Code", Accelerators.TO_CODE, np.toCodeAction));
//		m.add(new CMenuItem("Markdown", XAction.DISABLED));
		m.add(new CMenuItem("Text", Accelerators.TO_TEXT, np.toTextAction));
		m.add(new CMenuItem("Heading 1", Accelerators.TO_H1, np.toH1Action));
		m.add(new CMenuItem("Heading 2", Accelerators.TO_H2, np.toH2Action));
		m.add(new CMenuItem("Heading 3", Accelerators.TO_H3, np.toH3Action));
//		m.add(new CMenuItem("Heading 4", CAction.DISABLED));
//		m.add(new CMenuItem("Heading 5", CAction.DISABLED));
//		m.add(new CMenuItem("Heading 6", CAction.DISABLED));
		
		// engine
		mb.add(m = new CMenu("Engine"));
		m.add(new CMenuItem(NotebookIcons.Stop, "Interrupt", np.interruptAction));
		m.add(new CMenuItem("Restart", np.restartEngineAction));
		
		// help
		mb.add(new HelpMenu());
		
		return mb;
	}
	
	
	private Component createToolbar()
	{
		CToolBar t = Theme.toolbar();
		t.add(new TButton(NotebookIcons.Save, "Save", true, saveAction));
		t.space();
		t.add(new TButton(NotebookIcons.Cut, "Cut", true, XAction.DISABLED));
		t.add(new TButton(NotebookIcons.Copy, "Copy", true, XAction.DISABLED));
		t.add(new TButton(NotebookIcons.Paste, "Paste", true, XAction.DISABLED));
		t.space();
		t.add(new TButton(NotebookIcons.MoveUp, "Move Cell Up", true, np.moveCellUpAction));
		t.add(new TButton(NotebookIcons.MoveDown, "Move Cell Down", true, np.moveCellDownAction));
		t.space();
		t.add(new TButton(NotebookIcons.InsertAbove, "Insert Cell Above", true, np.insertCellAboveAction));
		t.add(new TButton(NotebookIcons.InsertBelow, "Insert Cell Below", true, np.insertCellBelowAction));
		t.space();
		t.add(new TButton(NotebookIcons.Start, "Run Cell in Place", true, np.runInPlaceAction));
		t.add(new TButton(NotebookIcons.Stop, "Interrupt", true, np.interruptAction));
		t.space();
		t.add(np.typeField);
		return t;
	}
	
	
	public boolean onWindowClosing()
	{
		// TODO check if running
		
		if(askToSave())
		{
			return false;
		}
		
//		if(isModified())
//		{
//			ChoiceDialog d = new ChoiceDialog(this, "File Modified", TXT.get("MainWindow.open.file modified", "{0} has been modified.  Save changes?", getFileName()));
//			d.addButton(Menus.DiscardChanges, 2);
//			d.addButton(Menus.Cancel, 1);
//			d.addButton(Menus.Save, 0, true);
//			int rv = d.openChoiceDialog();
//			switch(rv)
//			{
//			case 0:
//				actionSave();
//				break;
//			case 1:
//				return false;
//			case 2:
//				break;
//			default:
//				return false;
//			}
//		}
		
		return true;
	}
	
	
	public void updateActions()
	{
		updateTitle();
		
		np.updateActions();
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
		return np.isModified();
	}
	
	
	public void setModified(boolean on)
	{
		askedToSave = false;
		if(np.setModified(false))
		{
			updateActions();
		}
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
		
		for(MainWindow w: UI.getWindowsOfType(MainWindow.class))
		{
			if(w.isModified())
			{
				if(saveAll)
				{
					w.actionSave();
				}
				else
				{
					// avoid popping up dialog the second time
					if(w.askedToSave)
					{
						continue;
					}
					
					ChoiceDialog<Integer> d = new ChoiceDialog(w, "File Modified", TXT.get("MainWindow.save on exit.file exists", "{0} has been modified.  Save changes?", w.getFileName()));
					d.setChoiceDefault(-1);
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
		if(askToSave())
		{
			return;
		}
		
		CFileChooser fc = new CFileChooser(this, KEY_LAST_FILE);
		fc.setTitle(Menus.Open);
		fc.setApproveButtonText(Menus.Open);
		fc.setFileFilter(FileFilters.NOTEBOOK_FILES_FILTER);
		File f = fc.openFileChooser();
		if(f != null)
		{
			openFile(f);
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
				updateActions();
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
		fc.setFileFilter(FileFilters.NOTEBOOK_FILES_FILTER);
		File f = fc.openFileChooser();
		if(f != null)
		{
			f = CKit.ensureExtension(f, FileFilters.EXTENSION);
			
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
		// TODO check if current window is blank, open there
		
		MainWindow w = new MainWindow();
		UI.cascade(this, w);
		w.open();
		w.openFile(f);
	}
	
	
	protected void actionNew()
	{
		openNewWindow(null);
	}
	
	
	protected void openRecentFile(File f)
	{
		if(askToSave())
		{
			return;
		}
		
		if(f != null)
		{
			if(f.exists() && f.isFile())
			{
				openFile(f);
				return;
			}
		}
		
		Dialogs.warn(this, "File Not Found", "File not found: " + f);
	}
	
	
	/** returns true if the user decided to stay with the current document */
	protected boolean askToSave()
	{
		if(isModified())
		{
			askedToSave = true;
			
			ChoiceDialog<Integer> d = new ChoiceDialog(this, "File Modified", TXT.get("MainWindow.open.file modified", "{0} has been modified.  Save changes?", getFileName()));
			d.setChoiceDefault(-1);
			d.addButton(Menus.Cancel, 1);
			d.addButton(Menus.DiscardChanges, 2, Theme.DESTRUCTIVE_BUTTON_COLOR);
			d.addButton(Menus.Save, 0, true);
			int rv = d.openChoiceDialog();
			switch(rv)
			{
			case 0:
				actionSave();
				break;
			case 2:
				return false;
			case 1:
			default:
				return true;
			}
		}
		return false;
	}
	
	
	// f may be null
	public void openFile(File file)
	{
		try
		{
			if(file != null)
			{
				DataBookJsonReader rd = new DataBookJsonReader(new CReader(file));
				try
				{
					DataBook b = rd.read();
	
					setFile(file);
					setDataBook(b);
					setModified(false);
					updateActions();
					return;
				}
				finally
				{
					CKit.close(rd);
				}
			}
		}
		catch(Exception e)
		{
			Dialogs.err(this, e);
		}
		
		newFile();
	}
	
	
	public void newFile()
	{
		DataBook b = new DataBook(true);
		setDataBook(b);
		setModified(false);
		updateActions();
	}


	public static MainWindow get(Component c)
	{
		return UI.getAncestorOfClass(MainWindow.class, c);
	}
	
	
	protected void actionExportHtml()
	{
		try
		{
			CFileChooser fc = new CFileChooser(this, KEY_LAST_HTML_FILE);
			fc.setTitle("Export HTML");
			fc.setApproveButtonText("Export");
			fc.setFileFilter(FileFilters.HTML);
			File f = fc.openFileChooser();
			if(f != null)
			{
				f = CKit.ensureExtension(f, ".html");
				
				if(f.exists())
				{
					if(!Dialogs.checkFileExistsOverwrite(this, f))
					{
						return;
					}
				}
				
				HDocument d = np.exportHDocument();
				String s = d.toHtml();
				CKit.write(f, s);
			}
		}
		catch(Exception e)
		{
			Dialogs.err(this, e);
		}
	}
}
