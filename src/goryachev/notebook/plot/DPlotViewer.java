// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.plot;
import goryachev.notebook.Styles;
import goryachev.notebook.cell.CellHandler;
import goryachev.notebook.js.classes.DPlot;
import goryachev.swing.CPanel;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;


public class DPlotViewer
    extends CPanel
{
	private final DPlot plot;
	
	
	public DPlotViewer(DPlot p, CellHandler h)
	{
		this.plot = p;
		
		setBackground(Styles.plotViewerBackgroundColor);
		setPreferredSize(-1, 300);
	}
	
	
	public void paintComponent(Graphics gg)
	{
		super.paintComponent(gg);
		
		Graphics2D g = (Graphics2D)gg;
		
		int padding = plot.getPadding();
		Insets m = getInsets();
		int x0 = m.left + padding;
		int y0 = m.top + padding;
		int wi = getWidth() - x0 - m.right - padding;
		int he = getHeight() - y0 - m.bottom - padding;
		
		// title
		String s = plot.getTitle();
		if(s != null)
		{
			g.setFont(UI.deriveFont(Theme.boldFont(), Styles.plotTitleFontSize));
			g.setColor(Theme.TEXT_FG);
			Rectangle2D r = g.getFontMetrics().getStringBounds(s, g);
			g.drawString(s, (float)(getWidth()/2.0 - r.getWidth()/2.0), (float)(y0 - r.getY()));
			
			y0 += (r.getHeight() + padding);
			he -= (r.getHeight() + padding);
		}
		
		// legend
		
		// y axis title
		
		// y axis
		
		// x axis title
		
		// x axis
		
		// plot
		g.setColor(Styles.plotBackgroundColor);
		g.fillRect(x0, y0, wi, he);
		g.setColor(Styles.plotGridColor);
		g.drawRect(x0, y0, wi, he);
	}
}
