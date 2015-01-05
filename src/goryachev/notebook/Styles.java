// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.ui.Colors;
import goryachev.common.ui.Gray;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import java.awt.Color;


public class Styles
{
	public static final Color codeColor = new Gray(248);
	public static final Color errorColor = Color.red;
	public static final Color marginLineColor = new Color(244, 128, 128);
	public static final Color marginTextColor = Color.lightGray;
	public static final Color numberColor = new Color(0xa04dbe);
	public static final Color resultColor = Colors.eclipseGreen;
	public static final Color sectionBorderColor = Theme.hoverColor();
	public static final Color textColor = UI.mix(64, Theme.textBG(), Theme.textFG());
}
