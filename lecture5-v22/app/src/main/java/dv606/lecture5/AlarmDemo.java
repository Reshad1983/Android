/*
 * Example stolen from ApiDemos.  
 * Slightly modified.
 * 
 */
package dv606.lecture5;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
* Example of scheduling one-shot and repeating alarms. 
*/
public class AlarmDemo extends Activity {

 @Override
	protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.alarm_controller);

     // Attach button listeners
     Button button = (Button)findViewById(R.id.one_shot);
     button.setOnClickListener(oneShotListener);
     button = (Button)findViewById(R.id.start_repeating);
     button.setOnClickListener(startRepeatingListener);
     button = (Button)findViewById(R.id.stop_repeating);
     button.setOnClickListener(stopRepeatingListener);
 }

 private OnClickListener oneShotListener = new OnClickListener() {
     public void onClick(View v) {
         Intent intent = new Intent("dv606.lecture5.ALARM_BROADCAST");


 		 intent.putExtra("message", "The one-shot alarm has gone off");
         PendingIntent alarmIntent = PendingIntent.getBroadcast(AlarmDemo.this,0, intent, 0);

         // Schedule the alarm
         long startTime = 3*1000 + System.currentTimeMillis();
         AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
         am.set(AlarmManager.RTC_WAKEUP, startTime, alarmIntent);

         // Tell the user about what we did
         String msg = "One-shot alarm will go off in 3 seconds.";
         Toast.makeText(AlarmDemo.this, msg,Toast.LENGTH_LONG).show();
     }
 };

 private OnClickListener startRepeatingListener = new OnClickListener() {
     public void onClick(View v) {
         Intent intent = new Intent("dv606.lecture5.ALARM_BROADCAST");
 		 intent.putExtra("message", "Repeated alarm has gone off");
         PendingIntent alarmIntent = PendingIntent.getBroadcast(AlarmDemo.this,0, intent, 0);

         // Schedule the alarm using time since device boot
         // Starting with API 19, scheduling repeating alarms is going to be inexact
         // Starting with API 22, Android will not allow you to schedule
         // very frequent alarms - it is increasing min interval to 60 seconds as of API 22
         // (more info: https://code.google.com/p/android/issues/detail?id=161244)
         long startTime = 3*1000 + SystemClock.elapsedRealtime();
         AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
         am.setRepeating(AlarmManager.ELAPSED_REALTIME, startTime, 60*1000, alarmIntent);

         // Tell the user about what we did.
         String msg = "Repeating alarm in 3 seconds and every 60 seconds after";
         Toast.makeText(AlarmDemo.this, msg,Toast.LENGTH_LONG).show();
     }
 };

 private OnClickListener stopRepeatingListener = new OnClickListener() {
     public void onClick(View v) {
         // Create the same intent as before
         Intent intent = new Intent("dv606.lecture5.ALARM_BROADCAST");
         PendingIntent alarmIntent = PendingIntent.getBroadcast(AlarmDemo.this,0,intent,0);
         
         // And cancel the alarm.
         AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
         am.cancel(alarmIntent);

         // Tell the user about what we did.
         String msg = "Repeating alarm has been unscheduled";
         Toast.makeText(AlarmDemo.this, msg,Toast.LENGTH_LONG).show();
     }
 };
}

