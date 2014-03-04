// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package research.notebook;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CFocusMonitor;
import goryachev.common.ui.CMenu;
import goryachev.common.ui.CMenuBar;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.CToolBar;
import goryachev.common.ui.Menus;
import goryachev.common.ui.TButton;
import goryachev.common.ui.Theme;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import research.notebook.icons.ToolboxIcons;


public class NotebookWindow
	extends AppFrame
{
	public final CAction dummyAction = new CAction() { public void action() { } };
	
	public final CAction runCurrentAction = new CAction() { public void action() { actionRunCurrent(); } };
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
		
		setData(createDataBook());
	}
	
	
	private JMenuBar createMenu()
	{
		CMenuBar mb = Theme.menubar();
		CMenu m;

		// file
		mb.add(m = new CMenu(Menus.File));
		m.add(new CMenuItem(Menus.Open));
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
		
		// engine
		mb.add(m = new CMenu("Engine"));
		
		// help
		mb.add(m = new CMenu(Menus.Help));
		
		return mb;
	}
	
	
	private Component createToolbar()
	{
		CToolBar t = Theme.toolbar();
		t.add(new TButton(ToolboxIcons.Save, "Save", true, dummyAction));
		t.space();
		t.add(new TButton(ToolboxIcons.Cut, "Cut", true, dummyAction));
		t.add(new TButton(ToolboxIcons.Copy, "Copy", true, dummyAction));
		t.add(new TButton(ToolboxIcons.Paste, "Paste", true, dummyAction));
		t.space();
		t.add(new TButton(ToolboxIcons.MoveUp, "Move Section Up", true, dummyAction));
		t.add(new TButton(ToolboxIcons.MoveDown, "Move Section Down", true, dummyAction));
		t.space();
		t.add(new TButton(ToolboxIcons.InsertAbove, "Insert Section Above", true, dummyAction));
		t.add(new TButton(ToolboxIcons.InsertBelow, "Insert Section Below", true, dummyAction));
		t.space();
		t.add(new TButton(ToolboxIcons.Start, "Run", true, runCurrentAction));
		t.add(new TButton(ToolboxIcons.Stop, "Interrupt", true, dummyAction));
		t.space();
		t.add(notebookPanel.typeField);
		return t;
	}
	
	
	public void setData(DataBook b)
	{
		notebookPanel.setData(b);
	}
	
	
	public DataBook createDataBook()
	{
		DataBook b = new DataBook();
		b.addSection(SectionType.H1, "Example");
		b.addSection(SectionType.TEXT, "Here we have some text, which should be helpful when you have a need to display some text.\nThis text is simply a text area with line wrapping enabled.");
		b.addSection(SectionType.CODE, "var a = 5;\nprint(a);");
		b.addSection(SectionType.CODE, "// there will be syntax highlighting\n// that's for sure\nprint('a');\nprint('b' + 3);\nprint('Hello, world!');");
		b.addSection(SectionType.TEXT, "And this is how the notebook page will look like.\nThe end.");
		return b;
	}
	
	
	protected void actionRunCurrent()
	{
		JComponent c = CFocusMonitor.getLastTextComponent();
		CodeSection s = CodeSections.get(c);
		if(s != null)
		{
			s.runSection();
		}
	}
}
