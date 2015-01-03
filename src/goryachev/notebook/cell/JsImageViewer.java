// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class JsImageViewer
	extends JLabel
{
	public JsImageViewer(BufferedImage im)
	{
		setIcon(new ImageIcon(im));
	}
}
