// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.BackgroundThread;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import goryachev.notebook.DataBook;
import goryachev.notebook.SectionType;
import goryachev.notebook.Styles;
import goryachev.notebook.js.ScriptBody;
import goryachev.notebook.js.ScriptLogger;
import goryachev.notebook.js.img.JsImage;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;


public class CodePanel
	extends SectionPanel
	implements ScriptLogger
{
	public final JTextArea textField;
	public final JLabel inField;
	public final JLabel marginField;
	private final JTextArea resultField;
	private final SB log;
	private static CBorder BORDER = new CBorder(2, 4);
	private transient ScriptBody script;
	private transient JComponent resultComponent;
	
	
	public CodePanel(String text)
	{
		log = new SB();
		
		textField = new JTextArea(text);
		textField.setFont(Theme.monospacedFont());
		textField.setBackground(Styles.codeColor);
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		setTop(textField);
		
		inField = new JLabel("In (*):");
		inField.setBorder(BORDER);
		inField.setForeground(Styles.marginTextColor);
		inField.setHorizontalAlignment(JLabel.RIGHT);
		setLeft(inField);
		
		marginField = new JLabel(">");
		marginField.setBorder(BORDER);
		marginField.setForeground(Styles.marginTextColor);
		setRight(marginField);
		
		resultField = new JTextArea();
		resultField.setFont(Theme.monospacedFont());
		resultField.setForeground(Styles.resultColor);
		resultField.setLineWrap(true);
		resultField.setWrapStyleWord(true);
		resultField.setEditable(false);
	}
	
	
	public SectionType getType()
	{
		return SectionType.CODE;
	}
	
	
	public void initialize(NotebookPanel np)
	{
		// TODO setup popup menus
	}
	
	
	public void saveSection(DataBook b)
	{
		b.addSection(SectionType.CODE, getText());
		// TODO result
	}
	
	
	public String getText()
	{
		return textField.getText();
	}
	
	
	public boolean isRunning()
	{
		return script != null;
	}
	
	
	protected ScriptBody newProcess()
	{
		String script = textField.getText();
		ScriptBody p = new ScriptBody(this, script); 
		return p;
	}
	
	
	public synchronized void print(String s)
	{
		if(log.isNotEmpty())
		{
			log.nl();
		}
		log.append(s);
	}

	
	public synchronized void printError(String s)
	{
		if(log.isNotEmpty())
		{
			log.nl();
		}
		log.append(s);
	}
	
	
	protected void error(ScriptBody p, Throwable e)
	{
		if(script == p)
		{
			marginField.setText("ERR");
			
			resultField.setText(CKit.stackTrace(e));
			resultField.setForeground(Styles.errorColor);
			resultField.setCaretPosition(0);
			
			setResult(resultField);
		}
	}
	
	
	protected void setResult(JComponent c)
	{
		if(resultComponent != null)
		{
			remove(resultComponent);
		}
		
		script = null;
		
		resultComponent = c;
		add(resultComponent);
		UI.validateAndRepaint(this);
		
		NotebookPanel np = NotebookPanel.get(this);
		if(np != null)
		{
			np.updateActions();
		}
	}
	
	
	protected synchronized void start()
	{
		log.clear();
		marginField.setText("*");
	}
	
	
	public void runScript()
	{
		if(script == null)
		{
			final ScriptBody p = newProcess();
			script = p;
			
			start();
			
			new BackgroundThread("script")
			{
				private Object rv;
				
				public void process() throws Throwable
				{
					rv = p.process();
				}
				
				public void success()
				{
					finished(p, rv);
				}
				
				public void onError(Throwable e)
				{
					error(p, e);
				}
			}.start();
		}
	}
	
	
	protected void finished(ScriptBody p, Object r)
	{
		if(script == p)
		{
			marginField.setText("=");
			
			JComponent v = createViewer(r);
			setResult(v);
		}
	}
	
	
	protected JComponent createViewer(Object r)
	{
		if(r instanceof JsImage)
		{
			BufferedImage im = ((JsImage)r).getBufferedImage();
			
			JLabel t = new JLabel();
			t.setIcon(new ImageIcon(im));
			return t;
		}
		else
		{
			if(log.isNotEmpty())
			{
				log.nl();
			}
			log.a(r);

			resultField.setText(log.getAndClear());
			resultField.setForeground(Styles.resultColor);
			return resultField;
		}
	}
}
