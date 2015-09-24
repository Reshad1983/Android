/**
 * 
 */
package dv606.lecture5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;



/**
 * @author jlnmsi
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		String msg = intent.getStringExtra("message");
		System.out.println(msg);
		Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
	}

}

