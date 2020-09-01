package com.xter.slimnotek.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.xter.slimnotek.MainActivity
import com.xter.slimnotek.R
import com.xter.slimnotek.ui.widget.NoteWidget.Companion.ACTION_ADD

/**
 * Implementation of App Widget functionality.
 */
class NoteWidget : AppWidgetProvider() {

    companion object{
        val ACTION_ADD = "com.xter.slimnote.add_note"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.note_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    val intent = Intent(context, MainActivity::class.java)
    intent.setAction(ACTION_ADD)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    views.setOnClickPendingIntent(R.id.appwidget_add, pendingIntent);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}