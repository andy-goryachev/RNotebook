// Copyright (c) 2011-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import goryachev.common.util.Parsers;
import goryachev.common.util.fdetect.format.MatcherFTyp;
import goryachev.common.util.fdetect.format.MatcherRIFF;
import goryachev.common.util.fdetect.format.MatcherWM;
import goryachev.common.util.fdetect.format.MatcherZip;
import java.io.File;
import java.util.StringTokenizer;


// http://en.wikipedia.org/wiki/List_of_file_signatures
// http://www.garykessler.net/library/file_sigs.html
// http://www.filesignatures.net/index.php?page=all
public class FileFormatDetector
{
	private CList<FFMatcher> matchers = new CList();
	
	
	public FileFormatDetector()
	{
		init();
	}
	
	
	private void add(final FileType t, String hex)
	{
		add(t, hex, 0);
	}
	
	
	private void add(final FileType t, String hex, final int offset)
	{
		final byte[] pattern = Parsers.parseByteArray(hex);
		
		add(new FFMatcher()
		{
			public FileType match(String filename, byte[] b)
			{
				if(match(b, pattern, offset))
				{
					return t;
				}
				return null;
			}
		});
	}


	private void add(FFMatcher m)
	{
		matchers.add(m);
	}


	public FileType detect(File f) throws Exception
	{
		int sz = (int)Math.min(8192, f.length());
		byte[] b = CKit.readBytes(f, sz);
		return detect(f.getName(), b);
	}
	
	
	public FileType detect(String filename, byte[] b)
	{
		if(b != null)
		{
			FileType t;
			
			for(FFMatcher m: matchers)
			{
				t = m.match(filename, b);
				if(t != null)
				{
					return t;
				}
			}
			
			t = detectText(b);
			if(t != null)
			{
				return t;
			}
		}
		
		return FileType.UNKNOWN;
	}
	
	
	public FileType getType(File f)
	{
		try
		{
			return detect(f);
		}
		catch(Exception e)
		{ }
		
		return FileType.UNKNOWN;
	}
	
	
	private void init()
	{
		// bmp
		add(FileType.BMP, "424D");
		// class
		add(FileType.CLASS, "CAFEBABE");
		// exe
		add(FileType.EXE, "4D5A");
		// flv
		add(FileType.FLV, "464C5601");
		// gif
		add(FileType.GIF, "474946383761");
		add(FileType.GIF, "474946383961");
		// gz, tgz, gzip
		add(FileType.GZ, "1F8B08");
		// ico
		add(FileType.ICO, "00000100");
		// jpg
		add(FileType.JPEG, "FFD8FF");
		// mov
		add(FileType.MOV, "6D6F6F76", 4);
		// mp3
		add(FileType.MP3, "494433");
		add(FileType.MP3, "fffa");
		add(FileType.MP3, "fffb");
		// otf
		add(FileType.OTF, "4f54544f");
		// pdf
		add(FileType.PDF, "25504446");
		// png
		add(FileType.PNG, "89504E470D0A1A0A");
		// ps
		add(FileType.PS, "25215053");
		// psd
		add(FileType.PSD, "38425053");
		// rtf
		add(FileType.RTF, "7B5C72746631");
		// tiff 
		add(FileType.TIFF, "492049");
		add(FileType.TIFF, "49492A00");
		add(FileType.TIFF, "4D4D002A");
		add(FileType.TIFF, "4D4D002B");
		// ttf
		add(FileType.TTF, "0001000000");
		
		// 3gp, mp4, mov
		add(new MatcherFTyp());
		// zip, jar, ect.
		add(new MatcherZip());
		// avi, ...
		add(new MatcherRIFF());
		// avi, wma, wmv
		add(new MatcherWM());
	}
	
	
	protected String detectEncoding(byte[] bytes)
	{
		if(FFMatcher.match(bytes, "efbbbf"))
		{
			return "UTF-8";
		}
		else if(FFMatcher.match(bytes, "feff"))
		{
			return "UTF-16";
		}
		else if(FFMatcher.match(bytes, "fffe"))
		{
			return "UTF-16";
		}
		else
		{
			return null;
		}
	}

	
	protected String toString(byte[] bytes, String enc)
	{
		if(enc != null)
		{
			try
			{
				return new String(bytes, enc);
			}
			catch(Exception e)
			{ }
		}
		
		try
		{
			return new String(bytes, "UTF-8");
		}
		catch(Exception e)
		{ }
		
		try
		{
			return new String(bytes, "ASCII");
		}
		catch(Exception e)
		{ }
		
		return "";
	}


	protected FileType detectText(byte[] bytes)
	{
		int eof = 0;
		int nonascii = 0;
		
		for(byte b: bytes)
		{
			if(b < 0)
			{
				nonascii++;
			}
			else if(b < ' ')
			{
				switch(b)
				{
				case '\t':
				case '\n':
				case '\r':
					break;
				case 0x1a:
					eof++;
					break;
				default:
					// must be a binary
					return null;
				}
			}
		}
		
		if(eof > 1)
		{
			// unlikely in a text file
			return null;
		}
		
		// looks like a text file
		String enc = detectEncoding(bytes);
		String text = toString(bytes, enc).trim().toLowerCase();

		boolean xml = false;
		boolean bracket = false;
		boolean doctype = false;
		StringTokenizer tok = new StringTokenizer(text, "<> :\t\r\n", true);
		while(tok.hasMoreTokens())
		{
			String s = tok.nextToken();
			
			if("?xml".equals(s))
			{
				if(bracket)
				{
					xml = true;
				}
			}
			else if("svg".equals(s))
			{
				if(bracket)
				{
					return FileType.SVG;
				}
			}
			else if("html".equals(s))
			{
				if(doctype || bracket)
				{
					return FileType.HTML;
				}
			}
			else if("xsl".equals(s))
			{
				if(bracket)
				{
					// it's an XSL actually
					return FileType.XML;
				}
			}
			
			doctype = bracket && "!doctype".equals(s);
			bracket = "<".equals(s);
		}
		
		if(xml)
		{
			return FileType.XML;
		}
		else		
		{
			return FileType.TEXT;
		}
	}
}
