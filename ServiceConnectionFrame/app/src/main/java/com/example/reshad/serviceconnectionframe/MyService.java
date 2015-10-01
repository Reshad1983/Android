/**
 *
 */
package com.example.reshad.serviceconnectionframe;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * @author jlnmsi
 *
 */
public class MyService extends Service {

    /**
     * Binding
     */
    private  IBinder binder;
    private int count = 23;
    public static final int NOTIFICATION_ID = 1543;        // Unique ID
    private NotificationManager notifManager;
    private boolean keepWorking = true;

    /* To pull data in case of binding  */
    public int getCount() {return count;}

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new MyBinder();
    }

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
     * Empty binder suitable when no binding is expected.
     */
//    @Override
//    public IBinder onBind(Intent intent) {
//    	return null;
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

}