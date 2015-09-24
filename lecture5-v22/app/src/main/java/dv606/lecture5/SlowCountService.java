/**
 * 
 */
package dv606.lecture5;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * @author jlnmsi
 *
 */
public class SlowCountService extends Service {
	private int count = 0;
    public static final int NOTIFICATION_ID = 1543;        // Unique ID
    private NotificationManager notifManager;
    private boolean keepWorking = true;

    @Override
    public void onCreate() {
        // Tell the user we started.
    	System.out.println("onCreate()");

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        Thread thr = new Thread(null, work, "Slow Counting");
        thr.start();
        
      
    }

    /* To pull data in case of binding  */
    public int getCount() {return count;}
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	System.out.println("onStartCommand(...)");
    	//return Service.START_STICKY;
    	return Service.START_NOT_STICKY;  
    }
    
    @Override
    public void onDestroy() {
        keepWorking = false; // Stop thread
        System.out.println("onDestroy(...)");
    }

    /**
     * The function that runs in our worker thread
     */
    Runnable work = new Runnable() {
        public void run() {
            // Normally we would do some work here...  for our sample, we will
            // just repeatedly sleep for 3 seconds.
            Notification.Builder builder = new Notification.Builder(SlowCountService.this);

    		/* 2. Configure Notification Alarm */
            builder.setSmallIcon(R.drawable.ic_action_done)
                    .setWhen(System.currentTimeMillis())
                    .setTicker("Notification no. "+(count++))
                    .setAutoCancel(true);


    		/* 3. Configure Drop-down Action */
            builder.setContentTitle("More information")
                    .setContentText("Click to continue.")
                    .setContentInfo("Click!");
            Intent intent = new Intent(SlowCountService.this, NotificationDisplay.class);   // Notification intent
            PendingIntent notifIntent = PendingIntent.getActivity(SlowCountService.this, 0, intent, 0);
            builder.setContentIntent(notifIntent);

    		/* 4. Create Notification and use Manager to launch it */
            Notification notification = builder.build();

            String ns = Context.NOTIFICATION_SERVICE;
            notifManager = (NotificationManager) getSystemService(ns);
            notifManager.notify(NOTIFICATION_ID, notification);



        	 while (count<300 && keepWorking) {  // 300 is upper limit
             	count += 5;
            	System.out.println(count);
            	//Toast.makeText(SlowCountService.this,"Hello",Toast.LENGTH_SHORT).show();
             	SystemClock.sleep(3000);   // Sleep 3 seconds
            }
        	SlowCountService.this.stopSelf(); // when count = 300
        }
    };

    /**
     * Empty binder suitable when no binding is expected.
     */
//    @Override
//    public IBinder onBind(Intent intent) {
//    	return null;
//    }
    
    /**
     * Binding
     */
    private final IBinder binder = new SlowBinder();
    
    @Override
    public IBinder onBind(Intent intent) {
    	return binder;
    }

    public class SlowBinder extends Binder {
    	SlowCountService getService() {
    		return SlowCountService.this;
    	}
    }
    
}