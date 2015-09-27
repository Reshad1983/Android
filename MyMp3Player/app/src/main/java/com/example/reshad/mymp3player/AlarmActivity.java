package com.example.reshad.mymp3player;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmActivity extends AppCompatActivity {
    TextView alarm_view;
    Date date;
    ArrayList<PendingIntent> pIntent_list;
    private Calendar c;
    public static final String HOUR = "reshad.assignment.hour";
    public static final String ALARM = "reshad.assignment.alarm";
    public static final String MINUTE = "reshad.assignment.minute";
    private LinearLayout layout;
    private AlarmManager alarmManager;
    final Handler handler = new Handler();
    private boolean mIsLargeLayout;
    private Ringtone ringtone;
    private int requestID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Intent intent = getIntent();
        pIntent_list = new ArrayList<>();
        layout = (LinearLayout)findViewById(R.id.linear_id);
        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
        alarm_view = (TextView)findViewById(R.id.alarm_id);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        /*
        * When alarm goes off there comes a dialog box having the time
        * of alarm and a button to cancel it
        * */

        if(intent.hasExtra(ALARM))
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Alarm");// Get instance of Vibrator from current Context
            final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            // Start immediately
            // Vibrate for 200 milliseconds
            // Sleep for 500 milliseconds
            long[] pattern = { 0, 200, 500 };

            // The "0" means to repeat the pattern starting at the beginning
            // CUIDADO: If you start at the wrong index (e.g., 1) then your pattern will be off --
            // You will vibrate for your pause times and pause for your vibrate times !
                        v.vibrate(pattern, 0);
                        //In another part of your code, you can handle turning off the vibrator as shown below:

                   // view sourceprint?
            // Stop the Vibrator in the middle of whatever it is doing
            // CUIDADO: Do *not* do this immediately after calling .vibrate().
            // Otherwise, it may not have time to even begin vibrating!
            c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Stockholm"));
            dialog.setMessage("Alarm at " + c.get(Calendar.HOUR_OF_DAY) + " : " + c.get(Calendar.MINUTE) + " has gone off!");
            dialog.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ringtone.stop();
                    v.cancel();
                    dialog.dismiss();
                    finish();
                }
            });
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            ringtone = RingtoneManager.getRingtone(this, alarmUri);
            ringtone.play();
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();

        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Display display = getWindowManager().getDefaultDisplay();
                            Point point = new Point();
                            display.getSize(point);
                            int width = point.x;
                            alarm_view.setTextSize(width / 12);
                            c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Stockholm"));
                            int c_hour = c.get(Calendar.HOUR_OF_DAY);
                            int c_minute = c.get(Calendar.MINUTE);
                            int c_sec = c.get(Calendar.SECOND);
                            alarm_view.setText(c_hour + " : " + c_minute + " : " + c_sec);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 0, 5000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(null);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(null);
        super.onPause();
    }
    Dialog dialog;
    TimePicker timePicker;
    int currentApiVersion;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_alarm_id) {
            createAlarmDialog(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pIntent_list.size() > 0)
        {
            layout.removeViewAt(0);
            pIntent_list.get(0).cancel();
            pIntent_list.remove(0);
        }
    }


    private void createAlarmDialog(View vy) {
        final View myView = vy;
        dialog = new Dialog(AlarmActivity.this);
        dialog.setTitle("Alarm");
        dialog.setContentView(R.layout.activity_new_alarm);
        timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Stockholm"));
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        currentApiVersion= android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
        Button set_btn = (Button)dialog.findViewById(R.id.set_alarm_id);
        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour;
                int min;

                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    hour = timePicker.getHour();
                    min = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    min = timePicker.getCurrentMinute();
                }
                createAlarm(hour, min, myView);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void createAlarm(int hour, int min, View v)
    {
        if(v != null)
        {
            removeIntent(v, 0);
        }
        int intent_hour = c.get(Calendar.HOUR_OF_DAY);
        int intent_minute = c.get(Calendar.MINUTE);
        View myLayout = getLayoutInflater().inflate(R.layout.layout, layout, false);

        TextView timeView = (TextView)myLayout.findViewById(R.id.alarm_time_id);
        timeView.setText(hour + " : " + min + " ");
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlarmDialog(v);
            }
        });


        Button remove_btn = (Button)myLayout.findViewById(R.id.toggleButton);
        remove_btn.setOnClickListener(new toggleAlarm());
        hour = Math.abs(hour - intent_hour);
        min = Math.abs(min - intent_minute);

        Intent alarm_intent = new Intent("reshad.assingment.ALARM_BROADCAST");

        PendingIntent pIntent = PendingIntent.
                getBroadcast(AlarmActivity.this, requestID, alarm_intent, 0);
        int hashCode = pIntent.hashCode();
        remove_btn.setId(hashCode);
        timeView.setId(hashCode);
        requestID++;
        int sec = c.get(Calendar.SECOND);
        long startTime = (hour * 360000) + (min * 60000) - (sec * 1000)+ System.currentTimeMillis();
        pIntent_list.add(pIntent);
        //Remove existing alarm at the same time
        alarmManager.cancel(pIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime, pIntent);
        Toast.makeText(AlarmActivity.this,
                "Alarm goes off in " + hour + " hours and "+ min + " minutes",
                Toast.LENGTH_LONG).show();
        myLayout.setId(toChar(hour + " : " + min + " "));
        layout.addView(myLayout);
        layoutSort();
    }

    private int toChar(String s) {
        int returnValue = 0;
        char[] letters = s.toCharArray();
        for (char ch : letters) {
            returnValue += ch;
        }
        return returnValue;
    }

    private void layoutSort() {
        ArrayList<Integer> dateList = new ArrayList<>();
        ArrayList<LinearLayout> views = new ArrayList<>();
        for(int i = 0; i < layout.getChildCount(); i++)
        {
            LinearLayout myLayout = (LinearLayout)layout.getChildAt(i);
            views.add(myLayout);
            dateList.add(myLayout.getId());
        }
        layout.removeAllViews();
        Collections.sort(dateList);
        for (int id : dateList){
            for(int i = 0; i < views.size(); i++)
            {
                if(id == views.get(i).getId())
                {
                    layout.addView(views.get(i));
                }
            }
        }

        System.out.print("done");
    }

    private class toggleAlarm implements View.OnClickListener {
        @Override
        public void onClick(View v) {
                removeIntent(v, 0);
        }
    }

    private void removeIntent(View view, int hash) {
        int id;

        if(view == null)
        {
            id = hash;
        } else
        {
            id = view.getId();
        }
        for(int i = 0; i < pIntent_list.size(); i++)
        {
            PendingIntent intent = pIntent_list.get(i);
            int hashCode = intent.hashCode();
            if (id == hashCode)
            {
                LinearLayout myLayout = (LinearLayout)view.getParent();
                layout.removeView(myLayout); alarmManager.cancel(intent);
                pIntent_list.remove(i);
                i--;
                if(view == null)
                {
                    Toast.makeText(getApplicationContext(), "Alarm has been removed!", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
