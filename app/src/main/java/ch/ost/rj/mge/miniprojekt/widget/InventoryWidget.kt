package ch.ost.rj.mge.miniprojekt.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.activities.CreateItem

/**
 * Implementation of App Widget functionality.
 */
class InventoryWidget : AppWidgetProvider() {
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
    val flags = 0
    val requestCode = 0

    val intent = Intent(context, CreateItem::class.java)
    val pendingIntent = PendingIntent.getActivity(context,  requestCode, intent, flags)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.inventory_widget)
    views.setOnClickPendingIntent(R.id.button_add_item_widget, pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)


}