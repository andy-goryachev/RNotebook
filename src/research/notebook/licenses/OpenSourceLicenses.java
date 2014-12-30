// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package research.notebook.licenses;
import goryachev.common.ui.Application;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CButtonPanel;
import goryachev.common.ui.Menus;
import goryachev.common.ui.dialogs.license.MultiPageDialog;
import goryachev.common.util.CKit;
import goryachev.common.util.img.jhlabs.PixelUtils;
import goryachev.common.util.img.mortennobel.Lanczos3Filter;
import goryachev.json.gson.JsonReader;
import info.clearthought.layout.TableLayout;
import java.awt.Component;


public class OpenSourceLicenses
{
	public static final CAction openDialogAction = new CAction() { public void action() { actionLicense(getSourceWindow()); } };
	
	
	private static void licenses(MultiPageDialog d)
	{
		//d.addPage("Apache Imaging", CKit.readStringQuiet(OpenSourceLicenses.class, "apache imaging license.txt"));
		
		//d.addPage("Apache POI", CKit.readStringQuiet(OpenSourceLicenses.class, "apache poi license.txt"));
		
		//d.addPage("BouncyCastle", LICENSE.licenseText);
		
		d.addPage("Clearthought Table Layout", CKit.readStringQuiet(TableLayout.class, "License.txt"));
		
		// https://code.google.com/p/google-gson/
		d.addPage("Gson", CKit.readStringQuiet(JsonReader.class, "License.txt"));
		
		//d.addPage("ICEpdf", CKit.readStringQuiet(OpenSourceLicenses.class, "ice pdf license.txt"));
		d.addPage("java-image-scaling", CKit.readStringQuiet(Lanczos3Filter.class, "license.txt"));
		
		d.addPage("JH Labs", CKit.readStringQuiet(PixelUtils.class, "License.txt"));
		
		//d.addPage("jrawio", CKit.readStringQuiet(OpenSourceLicenses.class, "jrawio license.txt"));
		
		// not used
		//d.addPage("JSoup", CKit.readStringQuiet(Jsoup.class, "LICENSE"));
		
		// https://drewnoakes.com/code/exif/
		// https://github.com/drewnoakes/metadata-extractor
		//d.addPage("metadata-extractor", CKit.readStringQuiet(OpenSourceLicenses.class, "drewnoakes metadata-extractor license.txt"));
		
		//d.addPage("TwelveMonkeys ImageIO", CKit.readStringQuiet(OpenSourceLicenses.class, "twelve monkeys license.txt"));
	}

	
	public static void actionLicense(Component parent)
	{
		MultiPageDialog d = new MultiPageDialog(parent, "OpenSourceLicenses");

		CButtonPanel bp = new CButtonPanel(10);
		bp.setBorder(new CBorder(10));
		bp.add(new CButton(Menus.OK, d.closeAction, true));
		d.panel.setSouth(bp);
		
		d.setTitle(Menus.OpenSourceLicenses + " - " + Application.getTitle());
		d.setSize(900, 500);
		
		licenses(d);
		
		d.autoResizeSplit();
		d.open();
	}
}
