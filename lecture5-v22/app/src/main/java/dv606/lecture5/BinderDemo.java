/**
 * 
 */
package dv606.lecture5;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author jlnmsi
 *
 */
public class BinderDemo extends Activity {
	private Activity main_activity;
	private TextView data_display;
	
	private boolean binded = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binding);
        
        main_activity = this;  // Simplifies Intent handling
        
        /* Assign listener to button */
        Button button = (Button)findViewById(R.id.start_button);
        button.setOnClickListener(new StartClick());  
        
        button = (Button)findViewById(R.id.pull_button);
        button.setOnClickListener(new PullClick()); 
        data_display = (TextView)findViewById(R.id.data_display);
        
        button = (Button)findViewById(R.id.stop_button);
        button.setOnClickListener(new StopClick()); 
        
   
    }
    
    
    /*
     * Binding to the Service. This gives a reference to the service
     * which can be used to pull data (call getCount()) from it.
     */
    private SlowCountService service = null;
    
    private ServiceConnection connection = new ServiceConnection() {
    	//@Override  // Called when connection is made
    	public void onServiceConnected(ComponentName cName, IBinder binder) {
    		service = ((SlowCountService.SlowBinder)binder).getService();
    	}
    	//@Override   //
    	public void onServiceDisconnected(ComponentName cName) {
    		service = null;
    	}
    };
    
    
    /*
     * Button Listeners
     */
    
    private class StartClick implements View.OnClickListener {   	
    	public void onClick(View v) {
    		Intent intent = new Intent(main_activity,SlowCountService.class);
    		main_activity.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    		System.out.println("Binding to SlowCountService");
    	}
    }
    private class PullClick implements View.OnClickListener {   	
    	public void onClick(View v) {
    		int data = service.getCount();
    		data_display.setText(Integer.toString(data));
    	}
    }
    
    /* In order to avoid an exception we must unbind the connection before we exit the current activity.
     * I do it by pushing the "Unbind" button before I leave the activity. However, in order to make sure 
     * that it is always done, it should probably take place in the onStop() or onDestroy() methods.
     */
    private class StopClick implements View.OnClickListener {   	
    	public void onClick(View v) {
    		main_activity.unbindService(connection);
    		System.out.println("Unbinding SlowCountService");
    	}
    }
    
    
}

