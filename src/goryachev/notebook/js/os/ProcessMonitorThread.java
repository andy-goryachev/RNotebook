// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.os;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import goryachev.notebook.js.JsError;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ProcessMonitorThread
	extends Thread
{
	protected final ProcessMonitor parent;
	protected final boolean capture;
	protected final InputStreamReader input;
	protected final SB buffer;
	static Log log = Log.get("ProcessMonitorThread");
	
	
	public ProcessMonitorThread(ProcessMonitor p, String name, boolean capture, InputStream in)
	{
		super(name);
		setDaemon(true);
		
		this.parent = p;
		this.capture = capture;
		this.input = new InputStreamReader(in);
		this.buffer = new SB();
	}
	
	
	public void run()
	{
		try
		{
			while(parent.isRunning())
			{
				int c = input.read();
				if(c < 0)
				{
					return;
				}
				else
				{
					if(capture)
					{
						buffer.append((char)c);
					}
				}
			}
		}
		catch(Throwable e)
		{
			log.error(e);
		}
		finally
		{
			parent.setRunning(false);
			CKit.close(input);
		}
	}
	
	
	public String getBuffer()
	{
		return buffer.toString();
	}
	
	
	public JsError getError()
	{
		if(buffer.length() > 0)
		{
			return new JsError(buffer.toString());
		}
		return null;
	}
}
