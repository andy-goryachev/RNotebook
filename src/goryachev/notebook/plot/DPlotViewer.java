// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.plot;
import goryachev.common.ui.CPanel;
import goryachev.notebook.Styles;
import goryachev.notebook.cell.CellHandler;
import goryachev.notebook.js.classes.DPlot;
import java.awt.Graphics;
import java.awt.Insets;


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
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Insets m = getInsets();
		int x0 = m.left;
		int y0 = m.top;
		int wi = getWidth() - x0 - m.right;
		int he = getHeight() - y0 - m.bottom;
		
		// title
		
		// legend
		
		// y axis title
		
		// y axis
		
		// x axis title
		
		// x axis
		
		// plot
		g.setColor(Styles.plotBackgroundColor);
		int gp = 50;
		g.fillRect(x0 + gp, y0 + gp, wi - gp - gp, he - gp - gp);
		g.setColor(Styles.plotGridColor);
		g.drawRect(x0 + gp, y0 + gp, wi - gp - gp, he - gp - gp);
	}
}
