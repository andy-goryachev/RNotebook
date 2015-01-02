// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.common.util.CKit;
import goryachev.common.util.FileTools;
import goryachev.json.JsonEncoder;
import goryachev.notebook.DataBook;
import goryachev.notebook.Schema;
import java.io.File;


/** DataBook writer */
public class DataBookJsonWriter
{
	private DataBookJsonWriter()
	{
	}
	
	
//	private static void writeTask(Task t, JsonEncoder wr) throws Exception
//	{
//		wr.beginObject();
//		{
//			wr.write(Schema.KEY_ID, t.getID());
//			wr.write(Schema.KEY_TITLE, t.getTitle());
//			wr.write(Schema.KEY_TASK_CREATED, t.getDateCreated());
//			wr.write(Schema.KEY_TASK_COMPLETED, t.getDateCompleted());
//			wr.write(Schema.KEY_DESCRIPTION, t.getDescription());
//		}
//		wr.endObject();
//	}
//	
//	
//	private static void writeCard(TCard c, JsonEncoder wr) throws Exception
//	{
//		wr.beginObject();
//		{
//			wr.write(Schema.KEY_ID, c.getID());
//			wr.write(Schema.KEY_CARD_X, c.getX());
//			wr.write(Schema.KEY_CARD_Y, c.getY());
//			wr.write(Schema.KEY_TITLE, c.getTitle());
//			
//			wr.name(Schema.KEY_CARD_TASKS);
//			wr.beginArray();
//			{
//				int sz = c.getTaskCount();
//				for(int i=0; i<sz; i++)
//				{
//					Task t = c.getTask(i);
//					writeTask(t, wr);
//				}
//			}
//			wr.endArray();	
//		}
//		wr.endObject();
//	}
	
	
	public static void saveJSON(DataBook b, File f) throws Exception
	{
		FileTools.createBackup(f);
		
		// write
		JsonEncoder wr = new JsonEncoder(f);
		try
		{
			wr.beginObject();
			{
				if(b != null)
				{
					// file attributes
					wr.write(Schema.KEY_TYPE, Schema.TYPE);
					wr.write(Schema.KEY_VERSION, Schema.VERSION);

					// task board
//					wr.name(Schema.KEY_TASK_BOARD);
//					wr.beginArray();
//					{
//						for(TCard c: b.getCards())
//						{
//							writeCard(c, wr);
//						}
//					}
//					wr.endArray();
//					
//					// history
//					wr.name(Schema.KEY_TASK_HISTORY);
//					wr.beginArray();
//					{
//						for(Task t: b.getHistory())
//						{
//							writeTask(t, wr);
//						}
//					}
//					wr.endArray();
				}
			}
			wr.endObject();
		}
		finally
		{
			CKit.close(wr);
		}
	}
}
