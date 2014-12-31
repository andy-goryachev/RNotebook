// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.jedit;
import java.awt.Graphics;
import java.awt.event.MouseEvent;


/**
 * Highlight interface.
 */
public interface TextAreaHighlight
{
	/**
	 * Called after the highlight painter has been added.
	 * @param textArea The text area
	 * @param next The painter this one should delegate to
	 */
	public void init(JEditTextArea t, TextAreaHighlight next);


	/**
	 * This should paint the highlight and delgate to the
	 * next highlight painter.
	 * @param gfx The graphics context
	 * @param line The line number
	 * @param y The y co-ordinate of the line
	 */
	public void paintHighlight(Graphics d, int line, int y);


	/**
	 * Returns the tool tip to display at the specified
	 * location. If this highlighter doesn't know what to
	 * display, it should delegate to the next highlight
	 * painter.
	 * @param evt The mouse event
	 */
	public String getToolTipText(MouseEvent ev);
}