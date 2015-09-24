package dv606.lecture4.todo;

import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import dv606.lecture4.R;

public class TodoTasksActivity extends Activity {
	private TasksDataSource datasource;
	private List<TodoTask> values;
	private ArrayAdapter<TodoTask> listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.setContentView(R.layout.todo_tasks);
		
        datasource = new TasksDataSource(this);
        datasource.open();

        // get all tasks
        values = datasource.getAllTasks();

        // fill ListView with elements
        ListView list = (ListView)findViewById(R.id.list);
        listAdapter = new ArrayAdapter<TodoTask>(this,
            android.R.layout.simple_list_item_1, values);
        list.setAdapter(listAdapter);
        registerForContextMenu(list);
    }

    public void onClick(View view) {
        // handles add button click
        if (view.getId() == R.id.add) {
        	EditText text = (EditText)findViewById(R.id.todo);
        	String todo = text.getText().toString().trim();
        	if (!todo.isEmpty()) {
        		// Save the new task to the database
        		TodoTask task = datasource.createTask(todo);
        		listAdapter.add(task);
                listAdapter.notifyDataSetChanged();
        	}
        }
      }

    
    @Override
    protected void onResume() {
      datasource.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      datasource.close();
      super.onPause();
    }
    
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);  
    	if (v.getId()==R.id.list) {
    		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    		menu.setHeaderTitle(values.get(info.position).toString());
    		menu.add(0, 0, 0, "Delete");
	    }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
      if (item.getItemId() == 0) { //delete task
    	  TodoTask task = values.get(info.position);
    	  datasource.deleteTask(task);
    	  listAdapter.remove(task);
      }
      return true;
    }

}
