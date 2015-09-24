package dv606.lecture4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dv606.lecture4.files.FileIOActivity;
import dv606.lecture4.files.ReadResourceFile;
import dv606.lecture4.preferences.SimplePrefs;
import dv606.lecture4.todo.TodoTasksActivity;
import dv606.lecture4.provider.ContactsActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainList extends ListActivity {
	private List<String> activities = new ArrayList<String>();
	private Map<String,Class> name2class = new HashMap<String,Class>();
	
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /* Add Activities to list */
        setup_activities();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.main_list_item, activities));
        
        /* Attach list item listener */
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClick()); 
    }
    
    /* Private Help Entities */  
    private class OnItemClick implements OnItemClickListener {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		/* Find selected activity */
    		String activity_name = activities.get(position);
    		Class activity_class = name2class.get(activity_name);

    		/* Start new Activity */
    		Intent intent = new Intent(MainList.this,activity_class);
    		MainList.this.startActivity(intent);
    	}   	
    }
    
    /* Diagnostics while developing */
    private void showToast(String msg) {
    	Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    
    private void setup_activities() {
    	addActivity("ToDo List", TodoTasksActivity.class);
    	addActivity("Read from Resources", ReadResourceFile.class);
    	addActivity("File IO", FileIOActivity.class);
    	addActivity("Simple Preferences", SimplePrefs.class);
    	addActivity("Reading Contacts",ContactsActivity.class);
    }
    
    private void addActivity(String name, Class activity) {
    	activities.add(name);
    	name2class.put(name, activity);    	
    }
}