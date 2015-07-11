// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.Colors;
import goryachev.common.ui.Gray;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.theme.ThemeColor;
import java.awt.Color;


public class Styles
{
	public static final Color booleanColor = Color.blue;
	public static final Color codeColor = new Gray(248);
	public static final Color dateColor = Color.orange.darker();
	public static final Color errorColor = Color.red;
	public static final Color marginLineColor = new Color(244, 128, 128);
	public static final Color marginTextColor = Color.lightGray;
	public static final Color numberColor = new Color(0xa04dbe);
	public static final Color nullColor = Color.red;
	public static final Color plotBackgroundColor = Color.white;
	public static final Color plotGridColor = new Gray(221);
	public static final float plotTitleFontSize = 1.5f;
	public static final Color plotViewerBackgroundColor = new Gray(242);
	public static final Color resultColor = Colors.eclipseGreen;
	public static final Color sectionBorderColor = Theme.targetColor();
	public static final Color textColor = ThemeColor.create(ThemeKey.COLOR_TEXT_FG, 0.8, ThemeKey.COLOR_TEXT_BG);
}
