package dv606.lecture4.files;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import dv606.lecture4.R;


/**
 * A simple example program using data streams to read 
 * data (text lines) from a file.
 *   
 * @author jonasl
 */
public class FileIOActivity extends Activity {
	private final static String IO_FILE = "lines.txt"; 
	
	private ListView listView;
	private EditText editText;
	private ArrayList<String> text_lines = new ArrayList<String>();
	private ListAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_io);
        
        read_file();  // Read previous lines
        
        /* Configure list/adapter */  
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,text_lines);         
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        
        /* Configure text field */
        editText = (EditText)findViewById(R.id.edit);
        editText.setOnKeyListener(new OnEnter());
    }
    
    @Override
    protected void onStop(){
       super.onStop();
       
    	try {
    		/* Open output stream */
    		FileOutputStream output = openFileOutput(IO_FILE, MODE_PRIVATE);
    		DataOutputStream dout = new DataOutputStream(output);
    		
    		dout.writeInt(text_lines.size());  // Write line count
    		//System.out.println("Writing: "+text_lines.size());
    		
    		for (String line : text_lines) {  // Write lines
    			dout.writeUTF(line);
    		}
    		
    		dout.flush(); // Flush stream ...
    		dout.close(); // ... and close.
    	}
    	catch (IOException exc) {
    		exc.printStackTrace();
    	}
    
    }
    
    private void read_file() {
    	try {
    		/* Open input stream */ 
    		FileInputStream input = openFileInput(IO_FILE);
    		DataInputStream din = new DataInputStream(input);
    		
    		int sz = din.readInt();  // Read line count
    		//System.out.println("Reading: "+sz);
    		
    		for (int i=0;i<sz;i++) { // Read lines
    			String line = din.readUTF();
    			text_lines.add(line);
    		}
    		din.close();
    		
    	}
    	catch (IOException exc) {
    		exc.printStackTrace();
    	}
    }
    
    private class OnEnter implements OnKeyListener {
    	@Override
    	public boolean onKey(View v, int keyCode, KeyEvent event) {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
            	(keyCode == KeyEvent.KEYCODE_ENTER)) {          
         	  String text = editText.getText().toString();
         	  text_lines.add(text);
         	  editText.setText("");  // Reset EditText
         	  listView.setAdapter(adapter);   // Forces ListView update
              return true;
            }
            return false;
        }
    }
}
