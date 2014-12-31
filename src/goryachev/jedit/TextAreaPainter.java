/*
 * TextAreaPainter.java - Paints the text area
 * Copyright (C) 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */
package goryachev.jedit;
import goryachev.common.util.Log;
import goryachev.jedit.syntax.TokenMarker;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.Utilities;


/**
 * The text area repaint manager. It performs double buffering and paints
 * lines of text.
 */
public class TextAreaPainter
    extends JComponent
    implements TabExpander
{
	protected JEditTextArea textArea;
	protected SyntaxStyle[] styles;
	protected Color caretColor;
	protected Color selectionColor;
	protected Color lineHighlightColor;
	protected Color bracketHighlightColor;
	protected Color eolMarkerColor;
	protected boolean blockCaret;
	protected boolean lineHighlight;
	protected boolean bracketHighlight;
	protected boolean paintInvalid;
	protected boolean eolMarkers;
	protected int cols;
	protected int rows;
	protected int tabSize;
	protected FontMetrics fontMetrics;
	protected TextAreaHighlight highlights;

	int currentLineIndex;
	Token currentLineTokens;
	Segment currentLine;

	
	/**
	 * Creates a new repaint manager. This should be not be called
	 * directly.
	 */
	public TextAreaPainter(JEditTextArea textArea, TextAreaDefaults defaults)
	{
		this.textArea = textArea;

		setAutoscrolls(true);
		setDoubleBuffered(true);
		setOpaque(true);

		ToolTipManager.sharedInstance().registerComponent(this);

		currentLine = new Segment();
		currentLineIndex = -1;

		setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

		setFont(new Font("Monospaced", Font.PLAIN, 12)); // FIX
		setForeground(Color.black);
		setBackground(Color.white);

		blockCaret = defaults.blockCaret;
		styles = defaults.styles;
		cols = defaults.cols;
		rows = defaults.rows;
		caretColor = defaults.caretColor;
		selectionColor = defaults.selectionColor;
		lineHighlightColor = defaults.lineHighlightColor;
		lineHighlight = defaults.lineHighlight;
		bracketHighlightColor = defaults.bracketHighlightColor;
		bracketHighlight = defaults.bracketHighlight;
		paintInvalid = defaults.paintInvalid;
		eolMarkerColor = defaults.eolMarkerColor;
		eolMarkers = defaults.eolMarkers;
	}


	/**
	 * Returns if this component can be traversed by pressing the
	 * Tab key. This returns false.
	 */
	public boolean isManagingFocus()
	{
		return false;
	}


	/**
	 * Returns the syntax styles used to paint colorized text. Entry <i>n</i>
	 * will be used to paint tokens with id = <i>n</i>.
	 * @see org.gjt.sp.jedit.syntax.Token
	 */
	public SyntaxStyle[] getStyles()
	{
		return styles;
	}


	/**
	 * Sets the syntax styles used to paint colorized text. Entry <i>n</i>
	 * will be used to paint tokens with id = <i>n</i>.
	 * @param styles The syntax styles
	 * @see org.gjt.sp.jedit.syntax.Token
	 */
	public void setStyles(SyntaxStyle[] styles)
	{
		this.styles = styles;
		repaint();
	}


	/**
	 * Returns the caret color.
	 */
	public Color getCaretColor()
	{
		return caretColor;
	}


	/**
	 * Sets the caret color.
	 * @param caretColor The caret color
	 */
	public void setCaretColor(Color caretColor)
	{
		this.caretColor = caretColor;
		invalidateSelectedLines();
	}


	/**
	 * Returns the selection color.
	 */
	public Color getSelectionColor()
	{
		return selectionColor;
	}


	/**
	 * Sets the selection color.
	 * @param selectionColor The selection color
	 */
	public void setSelectionColor(Color selectionColor)
	{
		this.selectionColor = selectionColor;
		invalidateSelectedLines();
	}


	/**
	 * Returns the line highlight color.
	 */
	public Color getLineHighlightColor()
	{
		return lineHighlightColor;
	}


	/**
	 * Sets the line highlight color.
	 * @param lineHighlightColor The line highlight color
	 */
	public void setLineHighlightColor(Color lineHighlightColor)
	{
		this.lineHighlightColor = lineHighlightColor;
		invalidateSelectedLines();
	}


	/**
	 * Returns true if line highlight is enabled, false otherwise.
	 */
	public boolean isLineHighlightEnabled()
	{
		return lineHighlight;
	}


	/**
	 * Enables or disables current line highlighting.
	 * @param lineHighlight True if current line highlight should be enabled,
	 * false otherwise
	 */
	public void setLineHighlightEnabled(boolean lineHighlight)
	{
		this.lineHighlight = lineHighlight;
		invalidateSelectedLines();
	}


	/**
	 * Returns the bracket highlight color.
	 */
	public Color getBracketHighlightColor()
	{
		return bracketHighlightColor;
	}


	/**
	 * Sets the bracket highlight color.
	 * @param bracketHighlightColor The bracket highlight color
	 */
	public void setBracketHighlightColor(Color bracketHighlightColor)
	{
		this.bracketHighlightColor = bracketHighlightColor;
		invalidateLine(textArea.getBracketLine());
	}


	/**
	 * Returns true if bracket highlighting is enabled, false otherwise.
	 * When bracket highlighting is enabled, the bracket matching the
	 * one before the caret (if any) is highlighted.
	 */
	public boolean isBracketHighlightEnabled()
	{
		return bracketHighlight;
	}


	/**
	 * Enables or disables bracket highlighting.
	 * When bracket highlighting is enabled, the bracket matching the
	 * one before the caret (if any) is highlighted.
	 * @param bracketHighlight True if bracket highlighting should be
	 * enabled, false otherwise
	 */
	public void setBracketHighlightEnabled(boolean bracketHighlight)
	{
		this.bracketHighlight = bracketHighlight;
		invalidateLine(textArea.getBracketLine());
	}


	/**
	 * Returns true if the caret should be drawn as a block, false otherwise.
	 */
	public boolean isBlockCaretEnabled()
	{
		return blockCaret;
	}


	/**
	 * Sets if the caret should be drawn as a block, false otherwise.
	 * @param blockCaret True if the caret should be drawn as a block,
	 * false otherwise.
	 */
	public void setBlockCaretEnabled(boolean blockCaret)
	{
		this.blockCaret = blockCaret;
		invalidateSelectedLines();
	}


	/**
	 * Returns the EOL marker color.
	 */
	public Color getEOLMarkerColor()
	{
		return eolMarkerColor;
	}


	/**
	 * Sets the EOL marker color.
	 * @param eolMarkerColor The EOL marker color
	 */
	public void setEOLMarkerColor(Color eolMarkerColor)
	{
		this.eolMarkerColor = eolMarkerColor;
		repaint();
	}


	/**
	 * Returns true if EOL markers are drawn, false otherwise.
	 */
	public boolean getEOLMarkersPainted()
	{
		return eolMarkers;
	}


	/**
	 * Sets if EOL markers are to be drawn.
	 * @param eolMarkers True if EOL markers should be drawn, false otherwise
	 */
	public void setEOLMarkersPainted(boolean eolMarkers)
	{
		this.eolMarkers = eolMarkers;
		repaint();
	}


	/**
	 * Returns true if invalid lines are painted as red tildes (~),
	 * false otherwise.
	 */
	public boolean getInvalidLinesPainted()
	{
		return paintInvalid;
	}


	/**
	 * Sets if invalid lines are to be painted as red tildes.
	 * @param paintInvalid True if invalid lines should be drawn, false otherwise
	 */
	public void setInvalidLinesPainted(boolean paintInvalid)
	{
		this.paintInvalid = paintInvalid;
	}


	/**
	 * Adds a custom highlight painter.
	 * @param highlight The highlight
	 */
	public void addCustomHighlight(TextAreaHighlight highlight)
	{
		highlight.init(textArea, highlights);
		highlights = highlight;
	}
	

	/**
	 * Returns the tool tip to display at the specified location.
	 * @param evt The mouse event
	 */
	public String getToolTipText(MouseEvent evt)
	{
		if(highlights != null)
		{
			return highlights.getToolTipText(evt);
		}
		else
		{
			return null;
		}
	}


	/**
	 * Returns the font metrics used by this component.
	 */
	public FontMetrics getFontMetrics()
	{
		if(fontMetrics == null)
		{
			fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(getFont());
		}
		return fontMetrics;
	}
	

	/**
	 * Sets the font for this component. This is overridden to update the
	 * cached font metrics and to recalculate which lines are visible.
	 * @param font The font
	 */
	public void setFont(Font font)
	{
		super.setFont(font);
		fontMetrics = null;
	}


	/**
	 * Repaints the text.
	 * @param g The graphics context
	 */
	public void paint(Graphics g)
	{
		FontMetrics fm = getFontMetrics();
		tabSize = fm.charWidth(' ') * ((Integer)textArea.getDocument().getProperty(PlainDocument.tabSizeAttribute)).intValue();

		Rectangle clipRect = g.getClipBounds();

		g.setColor(getBackground());
		g.fillRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height);

		// We don't use yToLine() here because that method doesn't
		// return lines past the end of the document
		int height = fm.getHeight();
		int firstLine = textArea.getFirstLine();
		int firstInvalid = firstLine + clipRect.y / height;
		// Because the clipRect's height is usually an even multiple
		// of the font height, we subtract 1 from it, otherwise one
		// too many lines will always be painted.
		int lastInvalid = firstLine + (clipRect.y + clipRect.height - 1) / height;

		try
		{
			TokenMarker tokenMarker = textArea.getDocument().getTokenMarker();
			int x = textArea.getHorizontalOffset();

			for(int line = firstInvalid; line <= lastInvalid; line++)
			{
				paintLine(g, tokenMarker, line, x);
			}

			if(tokenMarker != null && tokenMarker.isNextLineRequested())
			{
				int h = clipRect.y + clipRect.height;
				repaint(0, h, getWidth(), getHeight() - h);
			}
		}
		catch(Exception e)
		{
			Log.err(e);
			Log.print("Error repainting line" + " range {" + firstInvalid + "," + lastInvalid + "}:");
		}
	}


	/**
	 * Marks a line as needing a repaint.
	 * @param line The line to invalidate
	 */
	public void invalidateLine(int line)
	{
		FontMetrics fm = getFontMetrics();
		repaint(0, textArea.lineToY(line) + fm.getMaxDescent() + fm.getLeading(), getWidth(), fm.getHeight());
	}


	/**
	 * Marks a range of lines as needing a repaint.
	 * @param firstLine The first line to invalidate
	 * @param lastLine The last line to invalidate
	 */
	public void invalidateLineRange(int firstLine, int lastLine)
	{
		FontMetrics fm = getFontMetrics();
		repaint(0, textArea.lineToY(firstLine) + fm.getMaxDescent() + fm.getLeading(), getWidth(), (lastLine - firstLine + 1) * fm.getHeight());
	}


	/**
	 * Repaints the lines containing the selection.
	 */
	public void invalidateSelectedLines()
	{
		invalidateLineRange(textArea.getSelectionStartLine(), textArea.getSelectionEndLine());
	}


	/**
	 * Implementation of TabExpander interface. Returns next tab stop after
	 * a specified point.
	 * @param x The x co-ordinate
	 * @param tabOffset Ignored
	 * @return The next tab stop after <i>x</i>
	 */
	public float nextTabStop(float x, int tabOffset)
	{
		int offset = textArea.getHorizontalOffset();
		int ntabs = ((int)x - offset) / tabSize;
		return (ntabs + 1) * tabSize + offset;
	}


	/**
	 * Returns the painter's preferred size.
	 */
	public Dimension getPreferredSize()
	{
		Dimension dim = new Dimension();
		FontMetrics fm = getFontMetrics();
		dim.width = fm.charWidth('w') * cols;
		dim.height = fm.getHeight() * rows;
		return dim;
	}


	/**
	 * Returns the painter's minimum size.
	 */
	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}


	protected void paintLine(Graphics g, TokenMarker tokenMarker, int line, int x)
	{
		Font defaultFont = getFont();
		Color defaultColor = getForeground();

		currentLineIndex = line;
		int y = textArea.lineToY(line);

		if(line < 0 || line >= textArea.getLineCount())
		{
			if(paintInvalid)
			{
				paintHighlight(g, line, y);
				styles[Token.INVALID].setGraphicsFlags(g, defaultFont);
				FontMetrics fm = getFontMetrics();
				g.drawString("~", 0, y + fm.getHeight());
			}
		}
		else if(tokenMarker == null)
		{
			paintPlainLine(g, line, defaultFont, defaultColor, x, y);
		}
		else
		{
			paintSyntaxLine(g, tokenMarker, line, defaultFont, defaultColor, x, y);
		}
	}


	protected void paintPlainLine(Graphics g, int line, Font defaultFont, Color defaultColor, int x, int y)
	{
		paintHighlight(g, line, y);
		textArea.getLineText(line, currentLine);

		g.setFont(defaultFont);
		g.setColor(defaultColor);

		FontMetrics fm = getFontMetrics();
		y += fm.getHeight();
		x = Utilities.drawTabbedText(currentLine, x, y, g, this, 0);

		if(eolMarkers)
		{
			g.setColor(eolMarkerColor);
			g.drawString(".", x, y);
		}
	}


	protected void paintSyntaxLine(Graphics g, TokenMarker tokenMarker, int line, Font defaultFont, Color defaultColor, int x, int y)
	{
		textArea.getLineText(currentLineIndex, currentLine);
		currentLineTokens = tokenMarker.markTokens(currentLine, currentLineIndex);

		paintHighlight(g, line, y);

		g.setFont(defaultFont);
		g.setColor(defaultColor);
		
		FontMetrics fm = getFontMetrics();
		y += fm.getHeight();
		x = SyntaxUtilities.paintSyntaxLine(currentLine, currentLineTokens, styles, this, g, x, y);

		if(eolMarkers)
		{
			g.setColor(eolMarkerColor);
			g.drawString(".", x, y);
		}
	}


	protected void paintHighlight(Graphics g, int line, int y)
	{
		if(line >= textArea.getSelectionStartLine() && line <= textArea.getSelectionEndLine())
		{
			paintLineHighlight(g, line, y);
		}

		if(highlights != null)
		{
			highlights.paintHighlight(g, line, y);
		}

		if(bracketHighlight && line == textArea.getBracketLine())
		{
			paintBracketHighlight(g, line, y);
		}

		if(line == textArea.getCaretLine())
		{
			paintCaret(g, line, y);
		}
	}


	protected void paintLineHighlight(Graphics g, int line, int y)
	{
		FontMetrics fm = getFontMetrics();
		int height = fm.getHeight();
		y += fm.getLeading() + fm.getMaxDescent();

		int selectionStart = textArea.getSelectionStart();
		int selectionEnd = textArea.getSelectionEnd();

		if(selectionStart == selectionEnd)
		{
			if(lineHighlight)
			{
				g.setColor(lineHighlightColor);
				g.fillRect(0, y, getWidth(), height);
			}
		}
		else
		{
			g.setColor(selectionColor);

			int selectionStartLine = textArea.getSelectionStartLine();
			int selectionEndLine = textArea.getSelectionEndLine();
			int lineStart = textArea.getLineStartOffset(line);

			int x1, x2;
			if(textArea.isSelectionRectangular())
			{
				int lineLen = textArea.getLineLength(line);
				x1 = textArea._offsetToX(line, Math.min(lineLen, selectionStart - textArea.getLineStartOffset(selectionStartLine)));
				x2 = textArea._offsetToX(line, Math.min(lineLen, selectionEnd - textArea.getLineStartOffset(selectionEndLine)));
				if(x1 == x2)
				{
					x2++;
				}
			}
			else if(selectionStartLine == selectionEndLine)
			{
				x1 = textArea._offsetToX(line, selectionStart - lineStart);
				x2 = textArea._offsetToX(line, selectionEnd - lineStart);
			}
			else if(line == selectionStartLine)
			{
				x1 = textArea._offsetToX(line, selectionStart - lineStart);
				x2 = getWidth();
			}
			else if(line == selectionEndLine)
			{
				x1 = 0;
				x2 = textArea._offsetToX(line, selectionEnd - lineStart);
			}
			else
			{
				x1 = 0;
				x2 = getWidth();
			}

			// "inlined" min/max()
			g.fillRect(x1 > x2 ? x2 : x1, y, x1 > x2 ? (x1 - x2) : (x2 - x1), height);
		}
	}


	protected void paintBracketHighlight(Graphics g, int line, int y)
	{
		int position = textArea.getBracketPosition();
		if(position == -1)
		{
			return;
		}
		
		FontMetrics fm = getFontMetrics();
		y += fm.getLeading() + fm.getMaxDescent();
		int x = textArea._offsetToX(line, position);
		g.setColor(bracketHighlightColor);
		// Hack!!! Since there is no fast way to get the character
		// from the bracket matching routine, we use ( since all
		// brackets probably have the same width anyway
		g.fillRect(x, y, fm.charWidth('(') - 1, fm.getHeight() - 1);
	}


	protected void paintCaret(Graphics g, int line, int y)
	{
		if(textArea.isCaretVisible())
		{
			FontMetrics fm = getFontMetrics();
			int offset = textArea.getCaretPosition() - textArea.getLineStartOffset(line);
			int caretX = textArea._offsetToX(line, offset);
			int caretWidth = ((blockCaret || textArea.isOverwriteEnabled()) ? fm.charWidth('w') : 1);
			y += fm.getLeading() + fm.getMaxDescent();
			int height = fm.getHeight();

			g.setColor(caretColor);

			if(textArea.isOverwriteEnabled())
			{
				g.fillRect(caretX, y + height - 1, caretWidth, 1);
			}
			else
			{
				g.drawRect(caretX, y, caretWidth - 1, height - 1);
			}
		}
	}
}