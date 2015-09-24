package dv606.lecture5;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmTest extends Activity {
TextView alarm_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_test);
        alarm_view = (TextView)findViewById(R.id.alarm_test_id);
        alarm_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("dv606.lecture5.ALARM_BROADCAST");


                intent.putExtra("message", "The one-shot alarm has gone off");
                PendingIntent alarmIntent = PendingIntent.getBroadcast(AlarmTest.this, 0, intent, 0);

                // Schedule the alarm
                long startTime = 3 * 1000 + System.currentTimeMillis();
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, startTime, alarmIntent);

                // Tell the user about what we did
                String msg = "One-shot alarm will go off in 3 seconds.";
                Toast.makeText(AlarmTest.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
