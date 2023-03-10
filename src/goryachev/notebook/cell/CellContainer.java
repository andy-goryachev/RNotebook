// Copyright © 2014-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;


public class CellContainer
    extends JPanel
    implements Scrollable
{
	public CellContainer()
	{
		super(new VerLayout());
		setOpaque(false);
	}


	public Dimension getPreferredScrollableViewportSize()
	{
		return null;
	}
	
	
	public int getScrollableUnitIncrement(Rectangle r, int orientation, int direction)
	{
		return 10;
	}
	

	public int getScrollableBlockIncrement(Rectangle r, int orientation, int direction)
	{
		switch(orientation)
		{
		case SwingConstants.HORIZONTAL: return (r.width * 80 / 100);
		case SwingConstants.VERTICAL:   return (r.height * 80 / 100);
		}
		return 10;
	}


	public boolean getScrollableTracksViewportWidth()
	{
		return true;
	}


	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}
}
