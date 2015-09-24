package dv606.lecture5;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class ShowTime extends Activity {
    /** Called when the activity is first created. */
    Handler handler = new Handler();
    Date date;
    String str;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        
        /* Find text display */
        final TextView display = (TextView) findViewById(R.id.display_text);
        date = new Date(System.currentTimeMillis());
        //String str = DateFormat.getDateInstance().format(date);
        str = DateFormat.getTimeInstance().format(date);
        display.setText(str);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        date = new Date(System.currentTimeMillis());
                        //String str = DateFormat.getDateInstance().format(date);
                        str = DateFormat.getTimeInstance().format(date);
                        display.setText(str);

                    }
                });
            }
        },0, 6000);
    }
}