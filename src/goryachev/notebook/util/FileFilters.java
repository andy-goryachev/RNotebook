// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.common.ui.CAcceptAllFileFilter;
import goryachev.common.ui.CExtensionFileFilter;
import goryachev.common.util.CList;
import goryachev.common.util.TXT;
import javax.swing.filechooser.FileFilter;


public class FileFilters
{
	public static final String EXTENSION = ".nbook";

	public static final FileFilter ALL_FILES = new CAcceptAllFileFilter();
	public static final FileFilter CSV = new CExtensionFileFilter(TXT.get("FileFilters.csv", "Comma-separated Values (*.csv)"), ".csv");
	public static final FileFilter DOCX = new CExtensionFileFilter(TXT.get("FileFilters.docx", "Microsoft Word (*.docx)"), ".docx");
	public static final FileFilter HTML = new CExtensionFileFilter(TXT.get("FileFilters.html", "Hypertext Markup Files (*.html, *.htm)"), ".html", ".htm");
	public static final FileFilter JPEG = new CExtensionFileFilter(TXT.get("FileFilters.jpeg images", "JPEG images (*.jpg)"), ".jpg", ".jpeg");
	public static final FileFilter JSON = new CExtensionFileFilter(TXT.get("FileFilters.json", "JSON (*.json)"), ".json");
	public static final FileFilter NOTEBOOK_FILES_FILTER = new CExtensionFileFilter("Notebook Files" + " (*" + EXTENSION + ")", EXTENSION);
	public static final FileFilter PNG = new CExtensionFileFilter(TXT.get("FileFilters.png images", "PNG images (*.png)", ".png"));
	public static final FileFilter TEXT = new CExtensionFileFilter(TXT.get("FileFilters.text", "Text Files (*.txt)"), ".txt");
	public static final FileFilter TMX = new CExtensionFileFilter(TXT.get("FileFilters.tmx", "Translation Memory Exchange (*.tmx)"), ".tmx");
	public static final FileFilter TSV = new CExtensionFileFilter(TXT.get("FileFilters.tsv", "Tab-separated Values (*.tsv, *.tab)"), ".tsv", ".tab");
	public static final FileFilter XLSX = new CExtensionFileFilter(TXT.get("FileFilters.xlsx", "Microsoft Excel (*.xlsx)", ".xlsx"));
	public static final FileFilter XML = new CExtensionFileFilter(TXT.get("FileFilters.xml", "XML (*.xml)"), ".xml");
	
	
	public static CList<FileFilter> imageFileFilters()
    {
		CList<FileFilter> fs = new CList();
		fs.add(PNG);
		fs.add(JPEG);
	    return fs;
    }
}
