// Copyright (c) 2006-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CAlignment;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.table.CTableRendererBorder;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import goryachev.common.util.Parsers;
import goryachev.common.util.TextTools;
import goryachev.notebook.Styles;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


// TODO foreground, date/time colors, boolean
public class DTableRenderer
	extends DefaultTableCellRenderer
{
	protected CTableRendererBorder border = new CTableRendererBorder();
	private int selectedMix = 72;
	private int selectedFocusedMix = 210;
	
	
	public DTableRenderer()
	{
		setHorizontalAlignment(LEADING);
		setVerticalAlignment(TOP);
		UI.disableHtml(this);
	}
	

	public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int col)
	{
		try
		{
			border.setFocused(focus);
			setBorder(border);

			setFont(t.getFont());
				
			// colors
				
			setBackground(t.getBackground());
			
			JTable.DropLocation d = t.getDropLocation();
			if(d != null && !d.isInsertRow() && !d.isInsertColumn() && (d.getRow() == row) && (d.getColumn() == col))
			{
				mixBackground(48, Color.black);
				sel = true;
				// fg = DefaultLookup.getColor(this, ui, "Table.dropCellForeground");
				// bg = DefaultLookup.getColor(this, ui, "Table.dropCellBackground");
			}

			if(sel)
			{
				Color bg = isBackgroundSet() ? getBackground() : t.getBackground();
				if(focus)
				{
					setBackground(UI.mix(selectedFocusedMix, t.getSelectionBackground(), bg));
				}
				else
				{
					setBackground(UI.mix(selectedMix, t.getSelectionBackground(), bg));
				}
			}
			else
			{
				setBackground(t.getBackground());
			}
				
			// icon

			Icon ic = Parsers.parseIcon(val);
			setIcon(ic);
				
			// alignment
			
			setHorizontalAlignment(LEADING);
			setHorizontalTextPosition(TRAILING);
				
			// text
				
			String text;
			CAlignment alignment;
			Color fg;
			
			if(val instanceof DecoratedCell)
			{
				DecoratedCell c = (DecoratedCell)val;
				text = c.text;
				alignment = c.alignment;
				fg = Theme.textFG();
			}
			else if(val instanceof Number)
			{
				text = Theme.formatNumber(val);
				alignment = CAlignment.TRAILING;
				fg = Styles.numberColor;
			}
			else
			{
				text = formatText(val);
				alignment = CAlignment.LEADING;
				fg = Theme.textFG();
			}
			
			setHorizontalAlignment(alignment.getAlignment());
			setText(text);
			setForeground(fg);
			
			// tooltip
			//setToolTipText(tooltip);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		return this;
	}
	
	
	protected String formatText(Object x)
	{
		if(x == null)
		{
			return null;
		}
		
		String s = x.toString();
		
		if(s.indexOf('\n') >= 0)
		{
			s = s.replace("\n", "\\n");
		}
		
		if(s.indexOf('\r') >= 0)
		{
			s = s.replace("\r", "\\r");
		}
		
		if(s.indexOf('\t') >= 0)
		{
			s = s.replace("\t", "\\t");
		}
		
		return s;
	}
	
	
	public void mixForeground(int fraction, Color c)
	{
		if(c != null)
		{
			setForeground(UI.mix(fraction, c, getForeground()));
		}
	}
	
	
	public void mixBackground(int fraction, Color c)
	{
		if(c != null)
		{
			setBackground(UI.mix(fraction, c, getBackground()));
		}
	}
	

//	protected void paintComponent(Graphics g)
//	{
//		if(ui != null)
//		{
//			Graphics g2 = g.create();
//			try
//			{
//				if(isOpaque())
//				{
//					g.setColor(getBackground());
//					g.fillRect(0, 0, getWidth(), getHeight());
//				}
//				
//				// custom background
//				if(handler != null)
//				{
//					handler.paintBackground(value, this, g);
//				}
//
//				ui.paint(g, this);
//			}
//			finally
//			{
//				g2.dispose();
//			}
//		}
//	}
	
	
	public static class DecoratedCell
	{
		public String text;
		public CAlignment alignment;
	}
}
