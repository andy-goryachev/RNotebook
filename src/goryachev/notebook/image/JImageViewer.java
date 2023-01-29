// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.image;
import goryachev.common.util.CKit;
import goryachev.notebook.util.FileFilters;
import goryachev.swing.CAction;
import goryachev.swing.CExtensionFileFilter;
import goryachev.swing.CPanel;
import goryachev.swing.Dialogs;
import goryachev.swing.ImageTools;
import goryachev.swing.ShadowBorder;
import goryachev.swing.dialogs.CFileChooser;
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
