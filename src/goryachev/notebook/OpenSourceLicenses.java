// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.Menus;
import goryachev.common.ui.dialogs.license.MultiPageDialog;
import goryachev.common.util.CKit;
import goryachev.common.util.img.jhlabs.PixelUtils;
import goryachev.common.util.img.mortennobel.Lanczos3Filter;
import goryachev.json.gson.JsonReader;
import java.awt.Component;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jsoup.Jsoup;
import org.mozilla.javascript.RhinoException;


public class OpenSourceLicenses
{
	public static final CAction openDialogAction = new CAction() { public void action() { actionLicense(getSourceWindow()); } };
	
	
	private static void licenses(MultiPageDialog d)
	{
		//d.addPage("Apache Imaging", CKit.readStringQuiet(OpenSourceLicenses.class, "apache imaging license.txt"));
		
		//d.addPage("Apache POI", CKit.readStringQuiet(OpenSourceLicenses.class, "apache poi license.txt"));
		
		//d.addPage("BouncyCastle", LICENSE.licenseText);
		
		// https://code.google.com/p/google-gson/
		d.addPage("Gson", CKit.readStringQuiet(JsonReader.class, "License.txt"));
		
		//d.addPage("ICEpdf", CKit.readStringQuiet(OpenSourceLicenses.class, "ice pdf license.txt"));
		d.addPage("java-image-scaling", CKit.readStringQuiet(Lanczos3Filter.class, "license.txt"));
		
		d.addPage("JH Labs Image Filters", CKit.readStringQuiet(PixelUtils.class, "License.txt"));
		
		//d.addPage("jrawio", CKit.readStringQuiet(OpenSourceLicenses.class, "jrawio license.txt"));
		
		d.addPage("JSoup", CKit.readStringQuiet(Jsoup.class, "LICENSE"));
		
		// https://drewnoakes.com/code/exif/
		// https://github.com/drewnoakes/metadata-extractor
		//d.addPage("metadata-extractor", CKit.readStringQuiet(OpenSourceLicenses.class, "drewnoakes metadata-extractor license.txt"));
		
		// mozilla rhino
		d.addPage("Mozilla Rhino", CKit.readStringQuiet(RhinoException.class, "LICENSE.txt"));
		
		// http://fifesoft.com/rsyntaxtextarea/
		d.addPage("RSyntaxTextArea", CKit.readStringQuiet(RSyntaxTextArea.class, "RSyntaxTextArea.License.txt"));
		
		//d.addPage("TwelveMonkeys ImageIO", CKit.readStringQuiet(OpenSourceLicenses.class, "twelve monkeys license.txt"));
	}

	
	public static void actionLicense(Component parent)
	{
		MultiPageDialog d = new MultiPageDialog(parent, "OpenSourceLicenses");

		d.buttonPanel().addButton(Menus.OK, d.closeDialogAction, true);
		
		d.setTitle(Menus.OpenSourceLicenses + " - " + Application.getTitle());
		d.setSize(800, 500);
		d.split.setDividerLocation(300);
		
		licenses(d);
		
		d.autoResizeSplit();
		d.open();
	}
}
