// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.ShadowBorder;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class JImageViewer
	extends CPanel
{
	public final JLabel imageField;
	
	
	public JImageViewer(BufferedImage im)
	{
		imageField = new JLabel(new ImageIcon(im));
		imageField.setBorder(new ShadowBorder(5, 100));
		setWest(imageField);
		setOpaque(false);
	}
}
