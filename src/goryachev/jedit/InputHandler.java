/*
 * InputHandler.java - Manages key bindings and executes actions
 * Copyright (C) 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */
package goryachev.jedit;
import goryachev.common.util.Log;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Hashtable;
import javax.swing.JPopupMenu;


/**
 * An input handler converts the user's key strokes into concrete actions.
 * It also takes care of macro recording and action repetition.<p>
 *
 * This class provides all the necessary support code for an input
 * handler, but doesn't actually do any key binding logic. It is up
 * to the implementations of this class to do so.
 *
 * @see org.gjt.sp.jedit.textarea.DefaultInputHandler
 */
public abstract class InputHandler
    extends KeyAdapter
{
	/**
	 * Adds the default key bindings to this input handler.
	 * This should not be called in the constructor of this
	 * input handler, because applications might load the
	 * key bindings from a file, etc.
	 */
	public abstract void addDefaultKeyBindings();


	/**
	 * Adds a key binding to this input handler.
	 * @param keyBinding The key binding (the format of this is
	 * input-handler specific)
	 * @param action The action
	 */
	public abstract void addKeyBinding(String keyBinding, ActionListener action);


	/**
	 * Removes a key binding from this input handler.
	 * @param keyBinding The key binding
	 */
	public abstract void removeKeyBinding(String keyBinding);


	/**
	 * Removes all key bindings from this input handler.
	 */
	public abstract void removeAllKeyBindings();
	

	/**
	 * Returns a copy of this input handler that shares the same
	 * key bindings. Setting key bindings in the copy will also
	 * set them in the original.
	 */
	public abstract InputHandler copy();

	
	//
	
	
	/**
	 * If this client property is set to Boolean.TRUE on the text area,
	 * the home/end keys will support 'smart' BRIEF-like behaviour
	 * (one press = start/end of line, two presses = start/end of
	 * viewscreen, three presses = start/end of document). By default,
	 * this property is not set.
	 */
	public static final String SMART_HOME_END_PROPERTY = "InputHandler.homeEnd";

	public static final ActionListener BACKSPACE = new Backspace();
	public static final ActionListener BACKSPACE_WORD = new BackspaceWord();
	public static final ActionListener DELETE = new Delete();
	public static final ActionListener DELETE_WORD = new DeleteWord();
	public static final ActionListener END = new End(false);
	public static final ActionListener DOCUMENT_END = new DocumentEnd(false);
	public static final ActionListener SELECT_END = new End(true);
	public static final ActionListener SELECT_DOC_END = new DocumentEnd(true);
	public static final ActionListener INSERT_BREAK = new InsertBreak();
	public static final ActionListener INSERT_TAB = new InsertTab();
	public static final ActionListener HOME = new Home(false);
	public static final ActionListener DOCUMENT_HOME = new DocumentHome(false);
	public static final ActionListener SELECT_HOME = new Home(true);
	public static final ActionListener SELECT_DOC_HOME = new DocumentHome(true);
	public static final ActionListener NEXT_CHAR = new NextChar(false);
	public static final ActionListener NEXT_LINE = new NextLine(false);
	public static final ActionListener NEXT_PAGE = new NextPage(false);
	public static final ActionListener NEXT_WORD = new NextWord(false);
	public static final ActionListener SELECT_NEXT_CHAR = new NextChar(true);
	public static final ActionListener SELECT_NEXT_LINE = new NextLine(true);
	public static final ActionListener SELECT_NEXT_PAGE = new NextPage(true);
	public static final ActionListener SELECT_NEXT_WORD = new NextWord(true);
	public static final ActionListener OVERWRITE = new Overwrite();
	public static final ActionListener PREV_CHAR = new PrevChar(false);
	public static final ActionListener PREV_LINE = new PrevLine(false);
	public static final ActionListener PREV_PAGE = new PrevPage(false);
	public static final ActionListener PREV_WORD = new PrevWord(false);
	public static final ActionListener SELECT_PREV_CHAR = new PrevChar(true);
	public static final ActionListener SELECT_PREV_LINE = new PrevLine(true);
	public static final ActionListener SELECT_PREV_PAGE = new PrevPage(true);
	public static final ActionListener SELECT_PREV_WORD = new PrevWord(true);
	public static final ActionListener REPEAT = new Repeat();
	public static final ActionListener TOGGLE_RECT = new ToggleRect();
	public static final ActionListener INSERT_CHAR = new InsertChar();

	private static Hashtable actions = initActions();
	protected ActionListener grabAction;
	protected boolean repeat;
	protected int repeatCount;
	protected InputHandler.MacroRecorder recorder;	
	

	private static Hashtable initActions()
	{
		Hashtable m = new Hashtable();
		m.put("backspace", BACKSPACE);
		m.put("backspace-word", BACKSPACE_WORD);
		m.put("delete", DELETE);
		m.put("delete-word", DELETE_WORD);
		m.put("end", END);
		m.put("select-end", SELECT_END);
		m.put("document-end", DOCUMENT_END);
		m.put("select-doc-end", SELECT_DOC_END);
		m.put("insert-break", INSERT_BREAK);
		m.put("insert-tab", INSERT_TAB);
		m.put("home", HOME);
		m.put("select-home", SELECT_HOME);
		m.put("document-home", DOCUMENT_HOME);
		m.put("select-doc-home", SELECT_DOC_HOME);
		m.put("next-char", NEXT_CHAR);
		m.put("next-line", NEXT_LINE);
		m.put("next-page", NEXT_PAGE);
		m.put("next-word", NEXT_WORD);
		m.put("select-next-char", SELECT_NEXT_CHAR);
		m.put("select-next-line", SELECT_NEXT_LINE);
		m.put("select-next-page", SELECT_NEXT_PAGE);
		m.put("select-next-word", SELECT_NEXT_WORD);
		m.put("overwrite", OVERWRITE);
		m.put("prev-char", PREV_CHAR);
		m.put("prev-line", PREV_LINE);
		m.put("prev-page", PREV_PAGE);
		m.put("prev-word", PREV_WORD);
		m.put("select-prev-char", SELECT_PREV_CHAR);
		m.put("select-prev-line", SELECT_PREV_LINE);
		m.put("select-prev-page", SELECT_PREV_PAGE);
		m.put("select-prev-word", SELECT_PREV_WORD);
		m.put("repeat", REPEAT);
		m.put("toggle-rect", TOGGLE_RECT);
		m.put("insert-char", INSERT_CHAR);
		return m;
	}


	/**
	 * Returns a named text area action.
	 * @param name The action name
	 */
	public static ActionListener getAction(String name)
	{
		return (ActionListener)actions.get(name);
	}


	/**
	 * Returns the name of the specified text area action.
	 * @param listener The action
	 */
	public static String getActionName(ActionListener listener)
	{
		Enumeration en = getActions();
		while(en.hasMoreElements())
		{
			String name = (String)en.nextElement();
			ActionListener _listener = getAction(name);
			if(_listener == listener)
			{
				return name;
			}
		}
		return null;
	}


	/**
	 * Returns an enumeration of all available actions.
	 */
	public static Enumeration getActions()
	{
		return actions.keys();
	}


	/**
	 * Grabs the next key typed event and invokes the specified
	 * action with the key as a the action command.
	 * @param action The action
	 */
	public void grabNextKeyStroke(ActionListener listener)
	{
		grabAction = listener;
	}


	/**
	 * Returns if repeating is enabled. When repeating is enabled,
	 * actions will be executed multiple times. This is usually
	 * invoked with a special key stroke in the input handler.
	 */
	public boolean isRepeatEnabled()
	{
		return repeat;
	}


	/**
	 * Enables repeating. When repeating is enabled, actions will be
	 * executed multiple times. Once repeating is enabled, the input
	 * handler should read a number from the keyboard.
	 */
	public void setRepeatEnabled(boolean repeat)
	{
		this.repeat = repeat;
	}


	/**
	 * Returns the number of times the next action will be repeated.
	 */
	public int getRepeatCount()
	{
		return (repeat ? Math.max(1, repeatCount) : 1);
	}


	/**
	 * Sets the number of times the next action will be repeated.
	 * @param repeatCount The repeat count
	 */
	public void setRepeatCount(int repeatCount)
	{
		this.repeatCount = repeatCount;
	}


	/**
	 * Returns the macro recorder. If this is non-null, all executed
	 * actions should be forwarded to the recorder.
	 */
	public InputHandler.MacroRecorder getMacroRecorder()
	{
		return recorder;
	}


	/**
	 * Sets the macro recorder. If this is non-null, all executed
	 * actions should be forwarded to the recorder.
	 * @param recorder The macro recorder
	 */
	public void setMacroRecorder(InputHandler.MacroRecorder recorder)
	{
		this.recorder = recorder;
	}


	/**
	 * Executes the specified action, repeating and recording it as
	 * necessary.
	 * @param listener The action listener
	 * @param source The event source
	 * @param actionCommand The action command
	 */
	public void executeAction(ActionListener listener, Object source, String actionCommand)
	{
		// create event
		ActionEvent evt = new ActionEvent(source, ActionEvent.ACTION_PERFORMED, actionCommand);

		// don't do anything if the action is a wrapper
		// (like EditAction.Wrapper)
		if(listener instanceof Wrapper)
		{
			listener.actionPerformed(evt);
			return;
		}

		// remember old values, in case action changes them
		boolean oldRepeat = repeat;
		int oldRepeatCount = getRepeatCount();

		// execute the action
		if(listener instanceof InputHandler.NonRepeatable)
		{
			listener.actionPerformed(evt);
		}
		else
		{
			for(int i = 0; i < Math.max(1, repeatCount); i++)
			{
				listener.actionPerformed(evt);
			}
		}

		// do recording. Notice that we do no recording whatsoever
		// for actions that grab keys
		if(grabAction == null)
		{
			if(recorder != null)
			{
				if(!(listener instanceof InputHandler.NonRecordable))
				{
					if(oldRepeatCount != 1)
					{
						recorder.actionPerformed(REPEAT, String.valueOf(oldRepeatCount));
					}

					recorder.actionPerformed(listener, actionCommand);
				}
			}

			// If repeat was true originally, clear it
			// Otherwise it might have been set by the action, etc
			if(oldRepeat)
			{
				repeat = false;
				repeatCount = 0;
			}
		}
	}


	/**
	 * Returns the text area that fired the specified event.
	 * @param evt The event
	 */
	public static JEditTextArea getTextArea(EventObject evt)
	{
		if(evt != null)
		{
			Object o = evt.getSource();
			if(o instanceof Component)
			{
				// find the parent text area
				Component c = (Component)o;
				for(;;)
				{
					if(c instanceof JEditTextArea)
					{
						return (JEditTextArea)c;
					}
					else if(c == null)
					{
						break;
					}
					if(c instanceof JPopupMenu)
					{
						c = ((JPopupMenu)c).getInvoker();
					}
					else
					{
						c = c.getParent();
					}
				}
			}
		}

		// this shouldn't happen
		Log.print("BUG: getTextArea() returning null. Report this to Slava Pestov <sp@gjt.org>");
		return null;
	}


	/**
	 * If a key is being grabbed, this method should be called with
	 * the appropriate key event. It executes the grab action with
	 * the typed character as the parameter.
	 */
	protected void handleGrabAction(KeyEvent evt)
	{
		// Clear it *before* it is executed so that executeAction()
		// resets the repeat count
		ActionListener _grabAction = grabAction;
		grabAction = null;
		executeAction(_grabAction, evt.getSource(), String.valueOf(evt.getKeyChar()));
	}


	//
	

	/**
	 * If an action implements this interface, it should not be repeated.
	 * Instead, it will handle the repetition itself.
	 */
	public interface NonRepeatable
	{
	}
	
	
	//
	

	/**
	 * If an action implements this interface, it should not be recorded
	 * by the macro recorder. Instead, it will do its own recording.
	 */
	public interface NonRecordable
	{
	}
	
	
	//
	

	/**
	 * For use by EditAction.Wrapper only.
	 * @since jEdit 2.2final
	 */
	public interface Wrapper
	{
	}
	
	
	//
	

	/**
	 * Macro recorder.
	 */
	public interface MacroRecorder
	{
		void actionPerformed(ActionListener listener, String actionCommand);
	}
	
	
	//
	

	public static class Backspace
	    implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}

			if(textArea.getSelectionStart() != textArea.getSelectionEnd())
			{
				textArea.setSelectedText("");
			}
			else
			{
				int caret = textArea.getCaretPosition();
				if(caret == 0)
				{
					textArea.getToolkit().beep();
					return;
				}
				try
				{
					textArea.getDocument().remove(caret - 1, 1);
				}
				catch(Exception e)
				{
					Log.err(e);
				}
			}
		}
	}
	
	
	//
	

	public static class BackspaceWord
	    implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int start = textArea.getSelectionStart();
			if(start != textArea.getSelectionEnd())
			{
				textArea.setSelectedText("");
			}

			int line = textArea.getCaretLine();
			int lineStart = textArea.getLineStartOffset(line);
			int caret = start - lineStart;

			String lineText = textArea.getLineText(textArea.getCaretLine());

			if(caret == 0)
			{
				if(lineStart == 0)
				{
					textArea.getToolkit().beep();
					return;
				}
				caret--;
			}
			else
			{
				String noWordSep = (String)textArea.getDocument().getProperty("noWordSep");
				caret = JEditUtilities.findWordStart(lineText, caret, noWordSep);
			}

			try
			{
				textArea.getDocument().remove(caret + lineStart, start - (caret + lineStart));
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
	}
	
	
	//
	

	public static class Delete
	    implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}

			if(textArea.getSelectionStart() != textArea.getSelectionEnd())
			{
				textArea.setSelectedText("");
			}
			else
			{
				int caret = textArea.getCaretPosition();
				if(caret == textArea.getDocumentLength())
				{
					textArea.getToolkit().beep();
					return;
				}
				try
				{
					textArea.getDocument().remove(caret, 1);
				}
				catch(Exception e)
				{
					Log.err(e);
				}
			}
		}
	}
	
	
	//
	

	public static class DeleteWord
	    implements ActionListener
	{
		public void actionPerformed(ActionEvent ev)
		{
			JEditTextArea textArea = getTextArea(ev);
			int start = textArea.getSelectionStart();
			if(start != textArea.getSelectionEnd())
			{
				textArea.setSelectedText("");
			}

			int line = textArea.getCaretLine();
			int lineStart = textArea.getLineStartOffset(line);
			int caret = start - lineStart;

			String lineText = textArea.getLineText(textArea.getCaretLine());

			if(caret == lineText.length())
			{
				if(lineStart + caret == textArea.getDocumentLength())
				{
					textArea.getToolkit().beep();
					return;
				}
				caret++;
			}
			else
			{
				String noWordSep = (String)textArea.getDocument().getProperty("noWordSep");
				caret = JEditUtilities.findWordEnd(lineText, caret, noWordSep);
			}

			try
			{
				textArea.getDocument().remove(start, (caret + lineStart) - start);
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
	}
	
	
	//
	

	public static class End
	    implements ActionListener
	{
		private boolean select;


		public End(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent ev)
		{
			JEditTextArea textArea = getTextArea(ev);
			int caret = textArea.getCaretPosition();
			int lastOfLine = textArea.getLineEndOffset(textArea.getCaretLine()) - 1;
			int lastVisibleLine = textArea.getFirstLine() + textArea.getVisibleLines();
			if(lastVisibleLine >= textArea.getLineCount())
			{
				lastVisibleLine = Math.min(textArea.getLineCount() - 1, lastVisibleLine);
			}
			else
			{
				lastVisibleLine -= (textArea.getElectricScroll() + 1);
			}

			int lastVisible = textArea.getLineEndOffset(lastVisibleLine) - 1;
			int lastDocument = textArea.getDocumentLength();

			if(caret == lastDocument)
			{
				textArea.getToolkit().beep();
				return;
			}
			else if(!Boolean.TRUE.equals(textArea.getClientProperty(SMART_HOME_END_PROPERTY)))
			{
				caret = lastOfLine;
			}
			else if(caret == lastVisible)
			{
				caret = lastDocument;
			}
			else if(caret == lastOfLine)
			{
				caret = lastVisible;
			}
			else
			{
				caret = lastOfLine;
			}

			if(select)
			{
				textArea.select(textArea.getMarkPosition(), caret);
			}
			else
			{
				textArea.setCaretPosition(caret);
			}
		}
	}
	
	
	//
	

	public static class DocumentEnd
	    implements ActionListener
	{
		private boolean select;


		public DocumentEnd(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent ev)
		{
			JEditTextArea textArea = getTextArea(ev);
			if(select)
			{
				textArea.select(textArea.getMarkPosition(), textArea.getDocumentLength());
			}
			else
			{
				textArea.setCaretPosition(textArea.getDocumentLength());
			}
		}
	}
	
	
	//
	

	public static class Home
	    implements ActionListener
	{
		private boolean select;


		public Home(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int firstLine = textArea.getFirstLine();
			int firstOfLine = textArea.getLineStartOffset(textArea.getCaretLine());
			int firstVisibleLine = (firstLine == 0 ? 0 : firstLine + textArea.getElectricScroll());
			int firstVisible = textArea.getLineStartOffset(firstVisibleLine);

			if(caret == 0)
			{
				textArea.getToolkit().beep();
				return;
			}
			else if(!Boolean.TRUE.equals(textArea.getClientProperty(SMART_HOME_END_PROPERTY)))
			{
				caret = firstOfLine;
			}
			else if(caret == firstVisible)
			{
				caret = 0;
			}
			else if(caret == firstOfLine)
			{
				caret = firstVisible;
			}
			else
			{
				caret = firstOfLine;
			}

			if(select)
			{
				textArea.select(textArea.getMarkPosition(), caret);
			}
			else
			{
				textArea.setCaretPosition(caret);
			}
		}
	}
	
	
	//
	

	public static class DocumentHome
	    implements ActionListener
	{
		private boolean select;


		public DocumentHome(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			if(select)
			{
				textArea.select(textArea.getMarkPosition(), 0);
			}
			else
			{
				textArea.setCaretPosition(0);
			}
		}
	}
	
	
	//
	

	public static class InsertBreak
	    implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}

			textArea.setSelectedText("\n");
		}
	}
	
	
	//
	

	public static class InsertTab
	    implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}

			textArea.overwriteSetSelectedText("\t");
		}
	}
	
	
	//
	

	public static class NextChar
	    implements ActionListener
	{
		private boolean select;


		public NextChar(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			if(caret == textArea.getDocumentLength())
			{
				textArea.getToolkit().beep();
				return;
			}

			if(select)
			{
				textArea.select(textArea.getMarkPosition(), caret + 1);
			}
			else
			{
				textArea.setCaretPosition(caret + 1);
			}
		}
	}
	
	
	//
	

	public static class NextLine
	    implements ActionListener
	{
		private boolean select;


		public NextLine(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();
			if(line == textArea.getLineCount() - 1)
			{
				textArea.getToolkit().beep();
				return;
			}

			int magic = textArea.getMagicCaretPosition();
			if(magic == -1)
			{
				magic = textArea.offsetToX(line, caret - textArea.getLineStartOffset(line));
			}

			caret = textArea.getLineStartOffset(line + 1) + textArea.xToOffset(line + 1, magic);
			if(select)
			{
				textArea.select(textArea.getMarkPosition(), caret);
			}
			else
			{
				textArea.setCaretPosition(caret);
			}
			textArea.setMagicCaretPosition(magic);
		}
	}
	
	
	//
	

	public static class NextPage
	    implements ActionListener
	{
		private boolean select;


		public NextPage(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int lineCount = textArea.getLineCount();
			int firstLine = textArea.getFirstLine();
			int visibleLines = textArea.getVisibleLines();
			int line = textArea.getCaretLine();

			firstLine += visibleLines;

			if(firstLine + visibleLines >= lineCount - 1)
			{
				firstLine = lineCount - visibleLines;
			}

			textArea.setFirstLine(firstLine);

			int caret = textArea.getLineStartOffset(Math.min(textArea.getLineCount() - 1, line + visibleLines));
			if(select)
			{
				textArea.select(textArea.getMarkPosition(), caret);
			}
			else
			{
				textArea.setCaretPosition(caret);
			}
		}
	}
	
	
	//
	

	public static class NextWord
	    implements ActionListener
	{
		private boolean select;


		public NextWord(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();
			int lineStart = textArea.getLineStartOffset(line);
			caret -= lineStart;

			String lineText = textArea.getLineText(textArea.getCaretLine());

			if(caret == lineText.length())
			{
				if(lineStart + caret == textArea.getDocumentLength())
				{
					textArea.getToolkit().beep();
					return;
				}
				caret++;
			}
			else
			{
				String noWordSep = (String)textArea.getDocument().getProperty("noWordSep");
				caret = JEditUtilities.findWordEnd(lineText, caret, noWordSep);
			}

			if(select)
			{
				textArea.select(textArea.getMarkPosition(), lineStart + caret);
			}
			else
			{
				textArea.setCaretPosition(lineStart + caret);
			}
		}
	}
	
	
	//
	

	public static class Overwrite
	    implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			textArea.setOverwriteEnabled(!textArea.isOverwriteEnabled());
		}
	}
	
	
	//
	

	public static class PrevChar
	    implements ActionListener
	{
		private boolean select;


		public PrevChar(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			if(caret == 0)
			{
				textArea.getToolkit().beep();
				return;
			}

			if(select)
			{
				textArea.select(textArea.getMarkPosition(), caret - 1);
			}
			else
			{
				textArea.setCaretPosition(caret - 1);
			}
		}
	}
	
	
	//
	

	public static class PrevLine
	    implements ActionListener
	{
		private boolean select;


		public PrevLine(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();
			if(line == 0)
			{
				textArea.getToolkit().beep();
				return;
			}

			int magic = textArea.getMagicCaretPosition();
			if(magic == -1)
			{
				magic = textArea.offsetToX(line, caret - textArea.getLineStartOffset(line));
			}

			caret = textArea.getLineStartOffset(line - 1) + textArea.xToOffset(line - 1, magic);
			if(select)
			{
				textArea.select(textArea.getMarkPosition(), caret);
			}
			else
			{
				textArea.setCaretPosition(caret);
			}
			textArea.setMagicCaretPosition(magic);
		}
	}
	
	
	//
	

	public static class PrevPage
	    implements ActionListener
	{
		private boolean select;


		public PrevPage(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int firstLine = textArea.getFirstLine();
			int visibleLines = textArea.getVisibleLines();
			int line = textArea.getCaretLine();

			if(firstLine < visibleLines)
			{
				firstLine = visibleLines;
			}

			textArea.setFirstLine(firstLine - visibleLines);

			int caret = textArea.getLineStartOffset(Math.max(0, line - visibleLines));
			if(select)
			{
				textArea.select(textArea.getMarkPosition(), caret);
			}
			else
			{
				textArea.setCaretPosition(caret);
			}
		}
	}
	
	
	//
	

	public static class PrevWord
	    implements ActionListener
	{
		private boolean select;


		public PrevWord(boolean select)
		{
			this.select = select;
		}


		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();
			int lineStart = textArea.getLineStartOffset(line);
			caret -= lineStart;

			String lineText = textArea.getLineText(textArea.getCaretLine());

			if(caret == 0)
			{
				if(lineStart == 0)
				{
					textArea.getToolkit().beep();
					return;
				}
				caret--;
			}
			else
			{
				String noWordSep = (String)textArea.getDocument().getProperty("noWordSep");
				caret = JEditUtilities.findWordStart(lineText, caret, noWordSep);
			}

			if(select)
			{
				textArea.select(textArea.getMarkPosition(), lineStart + caret);
			}
			else
			{
				textArea.setCaretPosition(lineStart + caret);
			}
		}
	}
	
	
	//
	

	public static class Repeat
	    implements ActionListener, InputHandler.NonRecordable
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			textArea.getInputHandler().setRepeatEnabled(true);
			String actionCommand = evt.getActionCommand();
			if(actionCommand != null)
			{
				textArea.getInputHandler().setRepeatCount(Integer.parseInt(actionCommand));
			}
		}
	}
	
	
	//
	

	public static class ToggleRect
	    implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			textArea.setSelectionRectangular(!textArea.isSelectionRectangular());
		}
	}
	
	
	//
	

	public static class InsertChar
	    implements ActionListener, InputHandler.NonRepeatable
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			String str = evt.getActionCommand();
			int repeatCount = textArea.getInputHandler().getRepeatCount();

			if(textArea.isEditable())
			{
				StringBuffer buf = new StringBuffer();
				for(int i = 0; i < repeatCount; i++)
				{
					buf.append(str);
				}
				textArea.overwriteSetSelectedText(buf.toString());
			}
			else
			{
				textArea.getToolkit().beep();
			}
		}
	}
}
