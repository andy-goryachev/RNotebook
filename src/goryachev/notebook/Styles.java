// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.swing.Colors;
import goryachev.swing.Gray;
import goryachev.swing.Theme;
import goryachev.swing.ThemeKey;
import goryachev.swing.theme.ThemeColor;
import java.awt.Color;


public class Styles
{
	// FIX non-theme colors
	public static final Color booleanColor = Color.blue;
	public static final Color codeColor = ThemeColor.shadow(ThemeKey.TEXT_BG, 12);
	public static final Color dateColor = Color.orange.darker();
	public static final Color errorColor = Color.red;
	public static final Color marginLineColor = ThemeColor.create(Color.red, 128, ThemeKey.TEXT_BG);
	public static final Color marginTextColor = Color.lightGray;
	public static final Color numberColor = new Color(0xa04dbe);
	public static final Color nullColor = Color.red;
	public static final Color plotBackgroundColor = Color.white;
	public static final Color plotGridColor = new Gray(221);
	public static final float plotTitleFontSize = 1.5f;
	public static final Color plotViewerBackgroundColor = new Gray(242);
	public static final Color resultColor = Colors.eclipseGreen;
	public static final Color sectionBorderColor = Theme.TARGET_COLOR;
	public static final Color textColor = ThemeColor.create(ThemeKey.TEXT_FG, 0.8, ThemeKey.TEXT_BG);
}
