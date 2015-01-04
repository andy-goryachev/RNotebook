// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.Accelerator;
import goryachev.common.ui.Menus;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


public class Accelerators
{
	public static final String MAIN_MENU = "Main Menu";
	
	public static Accelerator CLOSE_WINDOW = new Accelerator("close.window", MAIN_MENU, "Close");
	public static Accelerator COMMIT = new Accelerator("commit", MAIN_MENU, "Commit Cell", KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK);
	public static Accelerator FIND = new Accelerator("find", MAIN_MENU, "Find", KeyEvent.VK_F, true);
	public static Accelerator PREFERENCES = new Accelerator("preferences", MAIN_MENU, "Preferences", KeyEvent.VK_PERIOD, true);
	public static Accelerator RUN_ALL = new Accelerator("run.all", MAIN_MENU, "Run All");
	public static Accelerator RUN_CELL = new Accelerator("run.cell", MAIN_MENU, "Run Cell", KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK);
	public static Accelerator RUN_IN_PLACE = new Accelerator("run.in.place", MAIN_MENU, "Run in Place");
	public static Accelerator SAVE = new Accelerator("save", MAIN_MENU, Menus.Save, KeyEvent.VK_S, true);
	public static Accelerator SAVE_AS = new Accelerator("save.as", MAIN_MENU, Menus.SaveAs);
	public static Accelerator SELECT_NEXT_CELL = new Accelerator("select.next.cell", MAIN_MENU, "Select Next Cell", KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK);
	public static Accelerator SELECT_PREV_CELL = new Accelerator("select.prev.cell", MAIN_MENU, "Select Previous Cell", KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK);
}
