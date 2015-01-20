// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.Appearance;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.Menus;
import goryachev.common.ui.dialogs.options.COptionDialog;
import goryachev.common.ui.dialogs.options.OptionTreeBuilder;
import goryachev.common.ui.options.edit.DateFormatOptionEditor;
import goryachev.common.ui.options.edit.KeyBindingsEditor;
import goryachev.common.ui.options.edit.NumberFormatOptionEditor;
import goryachev.common.ui.options.edit.TimeFormatOptionEditor;
import goryachev.common.ui.theme.ThemeOptions;
import goryachev.common.util.TXT;
import goryachev.notebook.storage.StorageEditor;
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
			b.addOption(new StorageEditor());
		}
		b.end();
		
		// connectors
		b.addChild("Connectors");
		{
			//b.addOption(new BooleanOptionEditor(Options.weekStartsMonday, "Week starts Monday"));
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
