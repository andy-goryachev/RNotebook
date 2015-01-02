// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.common.util.CList;
import goryachev.json.JsonDecoder;
import goryachev.notebook.DataBook;
import goryachev.notebook.Schema;
import java.io.Reader;


/** Reads DataBook JSON file */
public class DataBookJsonReader
	extends JsonDecoder
{
	public DataBookJsonReader(Reader rd)
	{
		super(rd);
	}
	
	
	public DataBook parse() throws Exception
	{
//		CList<TCard> board = null;
//		CList<Task> history = null;
//		String id = null;
//		String ver = null;
//		
//		beginObject();
//		while(inObject())
//		{
//			String s = nextName();
//			if(Schema.KEY_TASK_BOARD.equals(s))
//			{
//				board = parseCards();
//			}
//			else if(Schema.KEY_TASK_HISTORY.equals(s))
//			{
//				history = parseTasks();
//			}
//			else if(Schema.KEY_ID.equals(s))
//			{
//				id = nextString();
//			}
//			else if(Schema.KEY_VERSION.equals(s))
//			{
//				ver = nextString();
//			}
//		}
//		endObject();
		
		return new DataBook();
	}


//	protected CList<TCard> parseCards() throws Exception
//    {
//		CList<TCard> cards = new CList();
//		
//		beginArray();
//		while(inArray())
//		{
//			TCard c = parseTCard();
//			cards.add(c);
//		}
//		endArray();
//		
//		return cards;
//    }
//	
//	
//	protected TCard parseTCard() throws Exception
//    {
//		String id = null;
//		String title = null;
//		int x = 0;
//		int y = 0;
//		CList<Task> tasks = null;
//		
//		beginObject();
//		while(inObject())
//		{
//			String s = nextName();
//			if(Schema.KEY_ID.equals(s))
//			{
//				id = nextString();
//			}
//			else if(Schema.KEY_CARD_X.equals(s))
//			{
//				x = nextInt();
//			}
//			else if(Schema.KEY_CARD_Y.equals(s))
//			{
//				y = nextInt();
//			}
//			else if(Schema.KEY_TITLE.equals(s))
//			{
//				title = nextString();
//			}
//			else if(Schema.KEY_CARD_TASKS.equals(s))
//			{
//				tasks = parseTasks();
//			}
//		}
//		endObject();
//		
//		TCard c = new TCard(id, x, y, title);
//		if(tasks != null)
//		{
//			for(Task t: tasks)
//			{
//				c.addTask(t);
//			}
//		}
//		return c;
//    }
//	
//	
//	protected CList<Task> parseTasks() throws Exception
//    {
//		CList<Task> tasks = new CList();
//		
//		beginArray();
//		while(inArray())
//		{
//			Task t = parseTask();
//			tasks.add(t);
//		}
//		endArray();
//		
//		return tasks;
//    }
//	
//	
//	protected Task parseTask() throws Exception
//	{
//		String id = null;
//		String title = null;
//		String description = null;
//		long created = 0;
//		Long completed = null;
//		
//		beginObject();
//		{
//			while(inObject())
//			{
//				String s = nextName();
//				if(Schema.KEY_ID.equals(s))
//				{
//					id = nextString();
//				}
//				else if(Schema.KEY_TITLE.equals(s))
//				{
//					title = nextString();
//				}
//				else if(Schema.KEY_TASK_CREATED.equals(s))
//				{
//					created = nextLong();
//				}
//				else if(Schema.KEY_TASK_COMPLETED.equals(s))
//				{
//					completed = nextLongObject();
//				}
//				else if(Schema.KEY_DESCRIPTION.equals(s))
//				{
//					description = nextString();
//				}
//			}
//		}
//		endObject();
//		
//		return new Task(id, title, created, completed, description);
//	}
}
