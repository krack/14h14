package fr.spiritofborepaire.bertille;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * App widget with on place for launch classic SMS.
 * 
 */
public class SimpleLauncherAppWidget extends AppWidgetProvider {
    // the remote view of widget.
    private RemoteViews views;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	// There may be multiple widgets active, so update all of them
	final int N = appWidgetIds.length;
	for (int i = 0; i < N; i++) {
	    this.updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
	}
    }

    /**
     * Update widget.
     * 
     * @param context the context of widget
     * @param appWidgetManager the appWidget manager
     * @param appWidgetId the id of widget
     */
    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
	this.views = new RemoteViews(context.getPackageName(), R.layout.simple_launcher_app_widget);

	// Add action
	this.addWidgetClickAction(context);

	// Instruct the widget manager to update the widget
	appWidgetManager.updateAppWidget(appWidgetId, this.views);
    }

    /**
     * Add click action of widget : launch SMS widget receiver
     * 
     * @param context the context of widget
     */
    private void addWidgetClickAction(Context context) {
	Intent defineIntent = new Intent(context, SmsWidgetReceiver.class);
	PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, defineIntent, 0);
	this.views.setOnClickPendingIntent(R.id.icone, pendingIntent);
    }
}
