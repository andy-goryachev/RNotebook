// Copyright © 2014-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.i18n.Menus;
import goryachev.swing.Accelerator;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


public class Accelerators
{
	public static final String EDITOR = "Editor";
	public static final String MAIN_MENU = "Main Menu";
	
	public static Accelerator CLOSE_WINDOW = new Accelerator("close.window", MAIN_MENU, "Close");
	public static Accelerator COMMIT = new Accelerator("commit", EDITOR, "Commit Cell", KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK);
	public static Accelerator FIND = new Accelerator("find", MAIN_MENU, "Find", KeyEvent.VK_F, true);
	public static Accelerator INSERT_CELL_ABOVE = new Accelerator("insert.above", EDITOR, "Insert Cell Above", KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK);
	public static Accelerator INSERT_CELL_BELOW = new Accelerator("insert.below", EDITOR, "Insert Cell Below", KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK);
	public static Accelerator PREFERENCES = new Accelerator("preferences", MAIN_MENU, "Preferences", KeyEvent.VK_PERIOD, true);
	public static Accelerator RUN_ALL = new Accelerator("run.all", MAIN_MENU, "Run All");
	public static Accelerator RUN_CELL = new Accelerator("run.cell", MAIN_MENU, "Run Cell", KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK);
	public static Accelerator RUN_IN_PLACE = new Accelerator("run.in.place", MAIN_MENU, "Run in Place");
	public static Accelerator SAVE = new Accelerator("save", MAIN_MENU, Menus.Save, KeyEvent.VK_S, true);
	public static Accelerator SAVE_AS = new Accelerator("save.as", MAIN_MENU, Menus.SaveAs);
	public static Accelerator SELECT_NEXT_CELL = new Accelerator("select.next.cell", MAIN_MENU, "Select Next Cell", KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK);
	public static Accelerator SELECT_PREV_CELL = new Accelerator("select.prev.cell", MAIN_MENU, "Select Previous Cell", KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK);
	public static Accelerator TO_CODE = new Accelerator("to.code", EDITOR, "Convert Cell to Code", KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK);
	public static Accelerator TO_H1 = new Accelerator("to.h1", EDITOR, "Convert Cell to Heading 1", KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK);
	public static Accelerator TO_H2 = new Accelerator("to.h2", EDITOR, "Convert Cell to Heading 2", KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK);
	public static Accelerator TO_H3 = new Accelerator("to.h3", EDITOR, "Convert Cell to Heading 3", KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK);
	public static Accelerator TO_TEXT = new Accelerator("to.text", EDITOR, "Convert Cell to Text", KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK);
}
