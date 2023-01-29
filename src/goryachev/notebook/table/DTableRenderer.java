// Copyright (c) 2006-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.table;
import goryachev.common.log.Log;
import goryachev.notebook.Styles;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.NBUtil;
import goryachev.swing.CAlignment;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import goryachev.swing.table.CTableRendererBorder;
import java.awt.Color;
import java.awt.Component;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.mozilla.javascript.IdScriptableObject;


// TODO date/time colors, boolean
public class DTableRenderer
	extends DefaultTableCellRenderer
{
	static Log log = Log.get("DTableRenderer");
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
			}

			if(sel)
			{
				Color bg = isBackgroundSet() ? getBackground() : t.getBackground();
				if(focus)
				{
					setBackground(UI.mix(bg, selectedFocusedMix, t.getSelectionBackground()));
				}
				else
				{
					setBackground(UI.mix(bg, selectedMix, t.getSelectionBackground()));
				}
			}
			else
			{
				setBackground(t.getBackground());
			}
				
			// icon

			Icon ic = NBUtil.parseIcon(val);
			setIcon(ic);
				
			// alignment
			
			setHorizontalAlignment(LEADING);
			setHorizontalTextPosition(TRAILING);
				
			// text
				
			String text;
			CAlignment alignment;
			Color fg;
			
			if(val instanceof IdScriptableObject)
			{
				val = JsUtil.decodeIdScriptableObject((IdScriptableObject)val);
			}
			
			if(val instanceof Number)
			{
				text = Theme.formatNumber(val);
				alignment = CAlignment.TRAILING;
				fg = Styles.numberColor;
			}
			else if(val == null)
			{
				text = "null";
				alignment = CAlignment.LEADING;
				fg = Styles.nullColor;
			}
			else if(val instanceof Boolean)
			{
				text = val.toString();
				alignment = CAlignment.LEADING;
				fg = Styles.booleanColor;
			}
			else if(val instanceof Date)
			{
				text = val.toString();
				alignment = CAlignment.LEADING;
				fg = Styles.dateColor;
			}
			else
			{
				text = formatText(val);
				alignment = CAlignment.LEADING;
				fg = Theme.TEXT_FG;
			}
			
			setHorizontalAlignment(alignment.getAlignment());
			setText(text);
			setForeground(fg);
		}
		catch(Exception e)
		{
			log.error(e);
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
			setForeground(UI.mix(getForeground(), fraction, c));
		}
	}
	
	
	public void mixBackground(int fraction, Color c)
	{
		if(c != null)
		{
			setBackground(UI.mix(getBackground(), fraction, c));
		}
	}
}
