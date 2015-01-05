/*
 * 02/26/2004
 *
 * SyntaxScheme.java - The set of colors and tokens used by an RSyntaxTextArea
 * to color tokens.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;
import goryachev.common.util.Log;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import javax.swing.text.StyleContext;


/**
 * The set of colors and styles used by an <code>RSyntaxTextArea</code> to
 * color tokens.<p>
 * You can use this class to programmatically set the fonts and colors used in
 * an RSyntaxTextArea, but for more powerful, externalized control, consider
 * using {@link Theme}s instead.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see Theme
 */
public class SyntaxScheme
    implements Cloneable, TokenTypes
{
	protected Style[] styles;


	/**
	 * Creates a color scheme that either has all color values set to
	 * a default value or set to <code>null</code>.
	 *
	 * @param useDefaults If <code>true</code>, all color values will
	 *        be set to default colors; if <code>false</code>, all colors
	 *        will be initially <code>null</code>.
	 */
	public SyntaxScheme(boolean useDefaults)
	{
		styles = new Style[DEFAULT_NUM_TOKEN_TYPES];
		if(useDefaults)
		{
			restoreDefaults(null);
		}
	}


	/**
	 * Creates a default color scheme.
	 *
	 * @param baseFont The base font to use.  Keywords will be a bold version
	 *        of this font, and comments will be an italicized version of this
	 *        font.
	 */
	public SyntaxScheme(Font baseFont)
	{
		this(baseFont, true);
	}


	/**
	 * Creates a default color scheme.
	 *
	 * @param baseFont The base font to use.  Keywords will be a bold version
	 *        of this font, and comments will be an italicized version of this
	 *        font.
	 * @param fontStyles Whether bold and italic should be used in the scheme
	 *        (vs. all tokens using a plain font).
	 */
	public SyntaxScheme(Font baseFont, boolean fontStyles)
	{
		styles = new Style[DEFAULT_NUM_TOKEN_TYPES];
		restoreDefaults(baseFont, fontStyles);
	}


	/**
	 * Restores all colors and fonts to their default values.
	 *
	 * @param baseFont The base font to use when creating this scheme.  If
	 *        this is <code>null</code>, then a default monospaced font is
	 *        used.
	 * @param fontStyles Whether bold and italic should be used in the scheme
	 *        (vs. all tokens using a plain font).
	 */
	public void restoreDefaults(Font f, boolean fontStyles)
	{
		Color comment = Color.red;
		Color keyword = Color.gray;
		Color normal = Color.black;
		Color arg = Color.blue;
		Color error = Color.magenta;

		styles[COMMENT_EOL] = new Style(comment);
		styles[COMMENT_MULTILINE] = new Style(comment);
		styles[COMMENT_DOCUMENTATION] = new Style(comment);
		styles[COMMENT_KEYWORD] = new Style(comment);
		styles[COMMENT_MARKUP] = new Style(comment);
		styles[RESERVED_WORD] = new Style(keyword);
		styles[RESERVED_WORD_2] = new Style(keyword);
		styles[FUNCTION] = new Style(normal);
		styles[LITERAL_BOOLEAN] = new Style(arg);
		styles[LITERAL_NUMBER_DECIMAL_INT] = new Style(arg);
		styles[LITERAL_NUMBER_FLOAT] = new Style(arg);
		styles[LITERAL_NUMBER_HEXADECIMAL] = new Style(arg);
		styles[LITERAL_STRING_DOUBLE_QUOTE] = new Style(arg);
		styles[LITERAL_CHAR] = new Style(arg);
		styles[LITERAL_BACKQUOTE] = new Style(arg);
		styles[DATA_TYPE] = new Style(normal);
		styles[VARIABLE] = new Style(normal);
		styles[REGEX] = new Style(arg);
		styles[ANNOTATION] = new Style(Color.gray);
		styles[IDENTIFIER] = new Style(null);
		styles[WHITESPACE] = new Style(Color.gray);
		styles[SEPARATOR] = new Style(normal);
		styles[OPERATOR] = new Style(normal);
		styles[PREPROCESSOR] = new Style(keyword);
		styles[MARKUP_TAG_DELIMITER] = new Style(keyword);
		styles[MARKUP_TAG_NAME] = new Style(keyword);
		styles[MARKUP_TAG_ATTRIBUTE] = new Style(keyword);
		styles[MARKUP_TAG_ATTRIBUTE_VALUE] = new Style(arg);
		styles[MARKUP_COMMENT] = new Style(comment);
		styles[MARKUP_DTD] = new Style(normal);
		styles[MARKUP_PROCESSING_INSTRUCTION] = new Style(keyword);
		styles[MARKUP_CDATA] = new Style(keyword);
		styles[MARKUP_CDATA_DELIMITER] = new Style(keyword);
		styles[MARKUP_ENTITY_REFERENCE] = new Style(normal);
		styles[ERROR_IDENTIFIER] = new Style(error);
		styles[ERROR_NUMBER_FORMAT] = new Style(error);
		styles[ERROR_STRING_DOUBLE] = new Style(error);
		styles[ERROR_CHAR] = new Style(error);

		// Issue #34: If an application modifies TokenTypes to add new built-in
		// token types, we'll get NPEs if not all styles are initialized.
		for(int i=0; i<styles.length; i++)
		{
			if(styles[i] == null)
			{
				styles[i] = new Style();
			}
		}
	}


	/**
	 * Changes the "base font" for this syntax scheme.  This is called by
	 * <code>RSyntaxTextArea</code> when its font changes via
	 * <code>setFont()</code>.  This looks for tokens that use a derivative of
	 * the text area's old font (but bolded and/or italicized) and make them
	 * use the new font with those stylings instead.  This is desirable because
	 * most programmers prefer a single font to be used in their text editor,
	 * but might want bold (say for keywords) or italics.
	 *
	 * @param old The old font of the text area.
	 * @param font The new font of the text area.
	 */
	protected void changeBaseFont(Font old, Font font)
	{
		for(int i=0; i<styles.length; i++)
		{
			Style style = styles[i];
			if(style != null && style.font != null)
			{
				if(style.font.getFamily().equals(old.getFamily()) && style.font.getSize() == old.getSize())
				{
					int s = style.font.getStyle(); // Keep bold or italic
					StyleContext sc = StyleContext.getDefaultStyleContext();
					style.font = sc.getFont(font.getFamily(), s, font.getSize());
				}
			}
		}
	}


	/**
	 * Returns a deep copy of this color scheme.
	 *
	 * @return The copy.
	 */
	@Override
	public Object clone()
	{
		SyntaxScheme shcs = null;
		try
		{
			shcs = (SyntaxScheme)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			Log.err(e);
			return null;
		}
		
		shcs.styles = new Style[styles.length];
		for(int i=0; i<styles.length; i++)
		{
			Style s = styles[i];
			if(s != null)
			{
				shcs.styles[i] = (Style)s.clone();
			}
		}
		return shcs;
	}


	/**
	 * Tests whether this color scheme is the same as another color scheme.
	 *
	 * @param otherScheme The color scheme to compare to.
	 * @return <code>true</code> if this color scheme and
	 *         <code>otherScheme</code> are the same scheme;
	 *         <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object otherScheme)
	{
		// No need for null check; instanceof takes care of this for us,
		// i.e. "if (!(null instanceof Foo))" evaluates to "true".
		if(!(otherScheme instanceof SyntaxScheme))
		{
			return false;
		}

		Style[] otherSchemes = ((SyntaxScheme)otherScheme).styles;

		int length = styles.length;
		for(int i = 0; i < length; i++)
		{
			if(styles[i] == null)
			{
				if(otherSchemes[i] != null)
				{
					return false;
				}
			}
			else if(!styles[i].equals(otherSchemes[i]))
			{
				return false;
			}
		}
		return true;
	}


	/**
	 * Returns a hex string representing an RGB color, of the form
	 * <code>"$rrggbb"</code>.
	 *
	 * @param c The color.
	 * @return The string representation of the color.
	 */
	private static final String getHexString(Color c)
	{
		return "$" + Integer.toHexString((c.getRGB() & 0xffffff) + 0x1000000).substring(1);
	}


	/**
	 * Returns the specified style.
	 *
	 * @param index The index of the style.
	 * @return The style.
	 * @see #setStyle(int, Style)
	 * @see #getStyleCount()
	 */
	public Style getStyle(int index)
	{
		return styles[index];
	}


	/**
	 * Returns the number of styles.
	 *
	 * @return The number of styles.
	 * @see #getStyle(int)
	 */
	public int getStyleCount()
	{
		return styles.length;
	}


	/**
	 * Used by third party implementors e.g. SquirreL SQL. Most applications do
	 * not need to call this method.
	 * <p>
	 * Note that the returned array is not a copy of the style data; editing the
	 * array will modify the styles used by any <code>RSyntaxTextArea</code>
	 * using this scheme.
	 *
	 * @return The style array.
	 * @see #setStyles(Style[])
	 */
	public Style[] getStyles()
	{
		return styles;
	}


	/**
	 * This is implemented to be consistent with {@link #equals(Object)}.
	 * This is a requirement to keep FindBugs happy.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode()
	{
		// Keep me fast.  Iterating over *all* syntax schemes contained is
		// probably much slower than a "bad" hash code here.
		int hashCode = 0;
		int count = styles.length;
		for(int i = 0; i < count; i++)
		{
			if(styles[i] != null)
			{
				hashCode ^= styles[i].hashCode();
				break;
			}
		}
		return hashCode;
	}


	protected void refreshFontMetrics(Graphics2D g2d)
	{
		// It is assumed that any rendering hints are already applied to g2d.
		for(int i = 0; i < styles.length; i++)
		{
			Style s = styles[i];
			if(s != null)
			{
				s.fontMetrics = s.font == null ? null : g2d.getFontMetrics(s.font);
			}
		}
	}


	/**
	 * Restores all colors and fonts to their default values.
	 *
	 * @param baseFont The base font to use when creating this scheme.  If
	 *        this is <code>null</code>, then a default monospaced font is
	 *        used.
	 */
	public void restoreDefaults(Font baseFont)
	{
		restoreDefaults(baseFont, true);
	}


	/**
	 * Sets a style to use when rendering a token type.
	 *
	 * @param type The token type.
	 * @param style The style for the token type.
	 * @see #getStyle(int)
	 */
	public void setStyle(int type, Style style)
	{
		styles[type] = style;
	}


	/**
	 * Used by third party implementors e.g. SquirreL SQL. Most applications do
	 * not need to call this method; individual styles can be set via
	 * {@link #setStyle(int, Style)}.
	 *
	 * @param styles The new array of styles to use.  Note that this should
	 *        have length of at least
	 *        {@link TokenTypes#DEFAULT_NUM_TOKEN_TYPES}.
	 * @see #setStyle(int, Style)
	 * @see #getStyles()
	 */
	public void setStyles(Style[] styles)
	{
		this.styles = styles;
	}
}