// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.notebook.Styles;
import goryachev.swing.CScrollPane;
import goryachev.swing.Theme;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JViewport;


public class CellScrollPane
	extends CScrollPane
{
	public CellScrollPane(Component c)
	{
		super(c);
		setBackground2(Theme.TEXT_BG);
	}
	
	
	protected JViewport createViewport()
	{
		return new JViewport()
		{
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				
				int x = getWidth() - CellLayout.getRightMargin() - CellBorder.INSETS.right - 1;
				g.setColor(Styles.marginLineColor);
				g.drawLine(x, 0, x, getHeight());
			}
		};
	}
}
