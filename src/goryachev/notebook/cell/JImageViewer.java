// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CExtensionFileFilter;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.ImageTools;
import goryachev.common.ui.ShadowBorder;
import goryachev.common.ui.dialogs.CFileChooser;
import goryachev.common.util.CKit;
import goryachev.notebook.util.FileFilters;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class JImageViewer
	extends CPanel
{
	public final CAction saveImageAction = new CAction() { public void action() { actionSaveImage(); } };
	public final BufferedImage image;
	public final JLabel imageField;
	
	
	public JImageViewer(BufferedImage im)
	{
		this.image = im;
		
		imageField = new JLabel(new ImageIcon(im));
		imageField.setBorder(new ShadowBorder(5, 100));
		setWest(imageField);
		setOpaque(false);
	}
	

	protected void actionSaveImage()
	{
		try
		{
			CFileChooser fc = new CFileChooser(this, "save.image.file");
			fc.setTitle("Save Image");
			fc.setApproveButtonText("Save Image");
			fc.setAcceptAllFileFilterUsed(false);
			fc.replaceFileFilters(FileFilters.imageFileFilters());
			File f = fc.openFileChooser();
			if(f != null)
			{
				CExtensionFileFilter ff = (CExtensionFileFilter)fc.getFileFilter();
				if(ff != null)
				{
					f = CKit.ensureExtension(f, ff.getFirstExtension());
				}
				
				if(Dialogs.checkFileExistsOverwrite(this, f))
				{
					ImageTools.write(image, f);
				}
			}
		}
		catch(Exception e)
		{
			Dialogs.err(this, e);
		}
	}
}
