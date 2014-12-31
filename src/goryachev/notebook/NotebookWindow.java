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
import goryachev.notebook.OBSOLETE.NotebookPanel_OLD;
import goryachev.notebook.editor.NotebookPanel;
import goryachev.notebook.icons.NotebookIcons;
import java.awt.Component;
import javax.swing.JMenuBar;


public class NotebookWindow
	extends AppFrame
{
	public final CAction dummyAction = new CAction() { public void action() { } };
	public final CAction openAction = new CAction() { public void action() { actionOpen(); } };
	public final CAction saveAction = new CAction() { public void action() { actionSave(); } };
	public final CAction saveAsAction = new CAction() { public void action() { actionSaveAs(); } };
	public final NotebookPanel_OLD notebookPanel;
	//public final NotebookPanel notebookPanel;
	
	
	public NotebookWindow()
	{
		super("NotebookWindow");
		
		setTitle(Application.getTitle() + " - " + Application.getVersion());
		setMinimumSize(500, 300);
		setSize(700, 900);
		
		notebookPanel = new NotebookPanel_OLD();
		//notebookPanel = new NotebookPanel();
		
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
		
		// view
		mb.add(m = new CMenu(Menus.View));
		
		// insert
		mb.add(m = new CMenu(Menus.Insert));
		
		// cell
		mb.add(m = new CMenu("Cell"));
		m.add("Execute");
		
		// engine
		mb.add(m = new CMenu("Engine"));
		
		// help
		mb.add(new HelpMenu());
		
		return mb;
	}
	
	
	private Component createToolbar()
	{
		CToolBar t = Theme.toolbar();
		t.add(new TButton(NotebookIcons.Save, "Save", true, saveAction));
		t.space();
		t.add(new TButton(NotebookIcons.Cut, "Cut", true, dummyAction));
		t.add(new TButton(NotebookIcons.Copy, "Copy", true, dummyAction));
		t.add(new TButton(NotebookIcons.Paste, "Paste", true, dummyAction));
		t.space();
		t.add(new TButton(NotebookIcons.MoveUp, "Move Section Up", true, dummyAction));
		t.add(new TButton(NotebookIcons.MoveDown, "Move Section Down", true, dummyAction));
		t.space();
		t.add(new TButton(NotebookIcons.InsertAbove, "Insert Section Above", true, dummyAction));
		t.add(new TButton(NotebookIcons.InsertBelow, "Insert Section Below", true, dummyAction));
		t.space();
		t.add(new TButton(NotebookIcons.Start, "Run", true, notebookPanel.runCurrentAction));
		t.add(new TButton(NotebookIcons.Stop, "Interrupt", true, dummyAction));
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
