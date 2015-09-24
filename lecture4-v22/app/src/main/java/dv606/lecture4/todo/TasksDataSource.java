package dv606.lecture4.todo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TasksDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private TasksDbHelper dbHelper;
	  private String[] allColumns = { TasksDbHelper.COLUMN_ID,
			  TasksDbHelper.COLUMN_TASK };

	  public TasksDataSource(Context context) {
	    dbHelper = new TasksDbHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public TodoTask createTask(String task) {
	    ContentValues values = new ContentValues();
	    values.put(TasksDbHelper.COLUMN_TASK, task);
	    long insertId = database.insert(TasksDbHelper.TASKS_TABLE_NAME, null, values);
	    Cursor cursor = database.query(TasksDbHelper.TASKS_TABLE_NAME,
	        allColumns, TasksDbHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    TodoTask newTask = cursorToTask(cursor);
	    cursor.close();
	    return newTask;
	  }

	  public void deleteTask(TodoTask task) {
	    long id = task.getId();
	    System.out.println("Task deleted with id: " + id);
	    database.delete(TasksDbHelper.TASKS_TABLE_NAME, TasksDbHelper.COLUMN_ID
	    		+ " = " + id, null);
	  }
	  
	  public TodoTask getTask(long taskId) {
		  String restrict = TasksDbHelper.COLUMN_ID + "=" + taskId;
		  Cursor cursor = database.query(true, TasksDbHelper.TASKS_TABLE_NAME, allColumns, restrict, 
		    		                        null, null, null, null, null);
		  if (cursor != null && cursor.getCount() > 0) {
			  cursor.moveToFirst();
			  TodoTask task = cursorToTask(cursor);
			  return task;
		  }
		  // Make sure to close the cursor
		  cursor.close();
		  return null;
	  }
	  
	  public boolean updateTask(long taskId, String task) {
		  ContentValues args = new ContentValues();
		  args.put(TasksDbHelper.COLUMN_TASK, task);

		  String restrict = TasksDbHelper.COLUMN_ID + "=" + taskId;
		  return database.update(TasksDbHelper.TASKS_TABLE_NAME, args, restrict , null) > 0;
	  } 

	  public List<TodoTask> getAllTasks() {
	    List<TodoTask> tasks = new ArrayList<TodoTask>();

	    Cursor cursor = database.query(TasksDbHelper.TASKS_TABLE_NAME,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      TodoTask task = cursorToTask(cursor);
	      tasks.add(task);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return tasks;
	  }

	  private TodoTask cursorToTask(Cursor cursor) {
		  TodoTask task = new TodoTask();
		  task.setId(cursor.getLong(0));
		  task.setTask(cursor.getString(1));
		  return task;
	  }
	} 
