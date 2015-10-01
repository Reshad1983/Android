package dv606.widget;

import java.util.Calendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

public class TimeWidget extends AppWidgetProvider {
	
	private static final String LOG = "dv606.widget.TimeWidget";

	public void updateTime(Context context, AppWidgetManager appWidgetManager,
				int[] appWidgetIds) {
		Log.w(LOG, "updateTime(3) method called");


		String time = DateFormat.format("hh:mm:ss", Calendar.getInstance().getTime()).toString();
		
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++)
		{
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
			//views.setTextViewText(R.id.time, time);
			//registering onClickListener
			Intent clickIntent = new Intent(context, TimeWidget.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.cityName_id, pendingIntent);
			
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);

	}

	public void updateTime(Context context) {
		Log.w(LOG, "onUpdate(1) method called");

		ComponentName thisWidget = new ComponentName(context, TimeWidget.class);
		AppWidgetManager appWidgetManager =	AppWidgetManager.getInstance(context);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		updateTime(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++)
		{
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
			//registering onClickListener
			Intent clickIntent = new Intent(context, TimeWidget.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.cityName_id, pendingIntent);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);


		SharedPreferences pref = context.getSharedPreferences(ConfigActivity.PREFS_NAME, 0);
		String cityName  = pref.getString(ConfigActivity.PREF_CITY_NAME, null);
		Log.w(LOG, "City name" + cityName);




		Log.w(LOG, "onReceive method called: " + intent.getAction());
		
		if (intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED))
			updateTime(context);
	}
}