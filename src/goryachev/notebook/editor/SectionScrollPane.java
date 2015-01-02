// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.Theme;
import goryachev.notebook.Styles;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JViewport;


public class SectionScrollPane
	extends CScrollPane
{
	public SectionScrollPane(Component c)
	{
		super(c);
		setBackground2(Theme.textBG());
	}
	
	
	protected JViewport createViewport()
	{
		return new JViewport()
		{
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				
				// comes from SectionBorder
				int fudge = 2;
				
				int x = getWidth() - SectionLayout.getRightMargin() - fudge;
				g.setColor(Styles.marginLineColor);
				g.drawLine(x, 0, x, getHeight());
			}
		};
	}
}
