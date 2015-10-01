package com.example.reshad.assignment_1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Arrays;

/**
 * Created by reshad on 2015-10-01.
 */
public class NewAppWidget extends AppWidgetProvider
{

    private static final String LOG = "com.example.reshad";
    private String cityName;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.w(LOG, "onUpdate method called: " + Arrays.toString(appWidgetIds));

        final int  n = appWidgetIds.length;
        for(int i = 0; i < n; i++)
        {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget_layout);

/*
            //registering onClickListener
            Intent clickIntent = new Intent(context, NewAppWidget.class);
            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_temp, pendingIntent);
*/

            //registring onClickListener
            Intent clickIntent = new Intent(context, VaxjoWeather.class);
            clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            clickIntent.putExtra("City", cityName);
            PendingIntent startVaxjoWeather = PendingIntent.getActivity(context, appWidgetId,clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_city, startVaxjoWeather);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences ref = context.getSharedPreferences(ConfigActivity.PREFS_NAME, 0);
        cityName = ref.getString(ConfigActivity.PREF_CITY_NAME, "");
        super.onReceive(context, intent);
    }
}
