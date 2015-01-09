// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.FileTools;
import goryachev.common.util.Parsers;
import goryachev.json.JsonEncoder;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import goryachev.notebook.Schema;
import goryachev.notebook.js.JsError;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.classes.DTable;
import java.awt.image.BufferedImage;
import java.io.File;


/** DataBook writer */
public class DataBookJsonWriter
{
	private final DataBook data;
	private final JsonEncoder wr;
	
	
	protected DataBookJsonWriter(DataBook b, JsonEncoder wr)
	{
		this.data = b;
		this.wr = wr;
	}


	protected void writeCell(DataBook.Cell c) throws Exception
	{
		wr.beginObject();
		{
			wr.write(Schema.KEY_CELL_TYPE, CellType.toSectionCode(c.type));
			wr.write(Schema.KEY_CELL_SOURCE, c.text);
			
			if(c.sequence > 0)
			{
				wr.write(Schema.KEY_CELL_SEQUENCE, c.sequence);
			}
			
			// output
			CList<Object> rs = c.results;
			if((rs != null) && (rs.size() > 0))
			{
				wr.name(Schema.KEY_CELL_OUTPUT);
				wr.beginArray();
				{
					for(Object r: rs)
					{
						writeResult(r);
					}
				}
				wr.endArray();
			}
		}
		wr.endObject();
	}
	
	
	protected void writeResult(Object x) throws Exception
	{
		wr.beginObject();
		{
			if(x instanceof BufferedImage)
			{
				byte[] b = ImageTools.toPNG((BufferedImage)x);
				wr.write(Schema.KEY_OUTPUT_TYPE, Schema.RESULT_IMAGE);
				wr.writeByteArray(Schema.KEY_OUTPUT_IMAGE, b);
			}
			else if(x instanceof JsError)
			{
				String msg = ((JsError)x).error;
				wr.write(Schema.KEY_OUTPUT_TYPE, Schema.RESULT_ERROR);
				wr.write(Schema.KEY_OUTPUT_TEXT, msg);
			}
			else if(x instanceof DTable)
			{
				DTable t = (DTable)x;
				
				wr.write(Schema.KEY_OUTPUT_TYPE, Schema.RESULT_TABLE);
				
				// columns
				wr.name(Schema.KEY_OUTPUT_TABLE_COLUMNS);
				wr.beginArray();
				{
					for(int i=0; i<t.getColumnCount(); i++)
					{
						wr.value(t.getColumnName(i));
					}
				}
				wr.endArray();
				
				// rows
				wr.name(Schema.KEY_OUTPUT_TABLE_ROWS);
				wr.beginArray();
				{
					for(int i=0; i<t.getRowCount(); i++)
					{
						wr.beginArray();
						{
							Object[] cells = t.getDataRow(i);
							if(cells != null)
							{
								for(int j=0; j<cells.length; j++)
								{
									wr.value(JsUtil.encodeTableCell(cells[j]));
								}
							}
						}
						wr.endArray();
					}
				}
				wr.endArray();
			}
			else 
			{
				wr.write(Schema.KEY_OUTPUT_TYPE, Schema.RESULT_TEXT);
				wr.write(Schema.KEY_OUTPUT_TEXT, Parsers.parseString(x));
			}
		}
		wr.endObject();
	}
	

	public void write() throws Exception
	{
		wr.beginObject();
		{
			if(data != null)
			{
				// file attributes
				wr.write(Schema.KEY_TYPE, Schema.TYPE);
				wr.write(Schema.KEY_VERSION, Schema.VERSION);

				// sections
				wr.name(Schema.KEY_CELLS);
				wr.beginArray();
				{
					int sz = data.size();
					for(int i = 0; i < sz; i++)
					{
						DataBook.Cell c = data.getCell(i);
						writeCell(c);
					}
				}
				wr.endArray();
			}
		}
		wr.endObject();
	}
	
	
	public static void saveJSON(DataBook b, File f) throws Exception
	{
		FileTools.createBackup(f);
		JsonEncoder wr = new JsonEncoder(f);
		try
		{
			new DataBookJsonWriter(b, wr).write();
		}
		finally
		{
			CKit.close(wr);
		}
	}
}
