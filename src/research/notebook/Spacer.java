// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package research.notebook;
import java.awt.Dimension;
import javax.swing.JComponent;


public class Spacer
	extends JComponent
{
	public Spacer(int width)
	{
		setPreferredSize(new Dimension(width, 1));
		setOpaque(false);
	}
}
