// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.i18n.Menus;
import goryachev.i18n.TXT;
import goryachev.notebook.storage.StorageEditor;
import goryachev.swing.Appearance;
import goryachev.swing.Application;
import goryachev.swing.CAction;
import goryachev.swing.dialogs.options.COptionDialog;
import goryachev.swing.dialogs.options.OptionTreeBuilder;
import goryachev.swing.dialogs.options.ThemeOptionEditor;
import goryachev.swing.options.edit.DateFormatOptionEditor;
import goryachev.swing.options.edit.KeyBindingsEditor;
import goryachev.swing.options.edit.NumberFormatOptionEditor;
import goryachev.swing.options.edit.TimeFormatOptionEditor;
import goryachev.swing.theme.ThemeOptions;
import java.awt.Component;


public class OptionsDialog
{
	public static CAction openDialogAction = new CAction() { public void action() { openDialog(getSourceWindow()); }};
	
	
	public static void openDialog(Component parent)
	{
		OptionTreeBuilder b = new OptionTreeBuilder();

		// appearance
		b.addChild(Menus.Appearance);
		{
			// theme
			b.addChild(Menus.Theme);
			{
				b.addOption(new ThemeOptionEditor());
			}
			b.end();
			
			// interface
			b.addSection(Menus.Interface);
			b.addOption(Menus.InterfaceLanguage, Appearance.getLanguageEditor());
			b.setRestartRequired();
			
			// formatting
			b.addSection(Menus.Formatting);
			b.addOption(Menus.NumberFormat, new NumberFormatOptionEditor(ThemeOptions.numberFormat));
			b.addOption(Menus.TimeFormat, new TimeFormatOptionEditor(ThemeOptions.timeFormat));
			b.addOption(Menus.DateFormat, new DateFormatOptionEditor(ThemeOptions.dateFormat));
		}
		b.end();
		
		// secure storage
		b.addChild("Key-Value Storage");
		{
			b.addOption(new StorageEditor(RNotebookApp.getStorage()));
		}
		b.end();
		
		// keys
		b.addChild(Menus.Keys);
		{
			b.addOption(new KeyBindingsEditor());
			b.setRestartRequired();
		}
		b.end();
		
		//

		// dialog
		COptionDialog d = new COptionDialog(parent, TXT.get("OptionsDialog.title", "Preferences - {0}", Application.getTitle()), b.getRoot(), "Preferences");
		d.setSize(1000, 650);
		d.expandTree();
		d.open();
		
		// TODO check modified options
//		CUnique<COption> changed = d.getChangedOptions();
		
//		if(d.isChanged())
//		{
//			for(Window w: UI.getVisibleWindows())
//			{
//				if(w instanceof NotebookWindow)
//				{
//					((NotebookWindow)w).reloadOptions();
//				}
//				
//				w.repaint();
//			}
//		}
	}
}
