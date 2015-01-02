// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CMenu;
import goryachev.common.ui.CMenuBar;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.CToolBar;
import goryachev.common.ui.Menus;
import goryachev.common.ui.TButton;
import goryachev.common.ui.Theme;
import goryachev.notebook.editor.NotebookPanel;
import goryachev.notebook.icons.NotebookIcons;
import java.awt.Component;
import javax.swing.JMenuBar;


public class NotebookWindow
	extends AppFrame
{
	public final CAction openAction = new CAction() { public void action() { actionOpen(); } };
	public final CAction saveAction = new CAction() { public void action() { actionSave(); } };
	public final CAction saveAsAction = new CAction() { public void action() { actionSaveAs(); } };
	public final NotebookPanel notebookPanel;
	
	
	public NotebookWindow()
	{
		super("NotebookWindow");
		
		setTitle(Application.getTitle() + " - " + Application.getVersion());
		setMinimumSize(500, 300);
		setSize(700, 900);
		
		notebookPanel = new NotebookPanel();
		
		setJMenuBar(createMenu());
		setNorth(createToolbar());
		setCenter(notebookPanel);
		setSouth(createStatusBar(true));
		
		setData(Demo.createDataBook());
		updateActions();
	}
	
	
	private JMenuBar createMenu()
	{
		CMenuBar mb = Theme.menubar();
		CMenu m;

		// file
		mb.add(m = new CMenu(Menus.File));
		m.add(new CMenuItem(Menus.Open, openAction));
		m.addSeparator();
		m.add(new CMenuItem(Menus.Save,  saveAction));
		m.add(new CMenuItem(Menus.SaveAs,  saveAsAction));
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
		t.add(new TButton(NotebookIcons.Cut, "Cut", true, CAction.TBD));
		t.add(new TButton(NotebookIcons.Copy, "Copy", true, CAction.TBD));
		t.add(new TButton(NotebookIcons.Paste, "Paste", true, CAction.TBD));
		t.space();
		t.add(new TButton(NotebookIcons.MoveUp, "Move Section Up", true, CAction.TBD));
		t.add(new TButton(NotebookIcons.MoveDown, "Move Section Down", true, CAction.TBD));
		t.space();
		t.add(new TButton(NotebookIcons.InsertAbove, "Insert Section Above", true, CAction.TBD));
		t.add(new TButton(NotebookIcons.InsertBelow, "Insert Section Below", true, CAction.TBD));
		t.space();
		t.add(new TButton(NotebookIcons.Start, "Run", true, notebookPanel.runCurrentAction));
		t.add(new TButton(NotebookIcons.Stop, "Interrupt", true, CAction.TBD));
		t.space();
		t.add(notebookPanel.typeField);
		return t;
	}
	
	
	protected void updateActions()
	{
		// TODO
	}
	
	
	public void setData(DataBook b)
	{
		notebookPanel.setDataBook(b);
	}
	
	
	protected void actionOpen()
	{
		// TODO
	}
	
	
	protected void actionSave()
	{
		// TODO
	}
	
	
	protected void actionSaveAs()
	{
		// TODO
	}
}
