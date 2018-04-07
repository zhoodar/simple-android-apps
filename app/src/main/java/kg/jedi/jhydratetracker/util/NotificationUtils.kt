package kg.jedi.jhydratetracker.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat.*
import android.support.v4.content.ContextCompat
import kg.jedi.jhydratetracker.Actions.ACTION_DISMISS_NOTIFICATION
import kg.jedi.jhydratetracker.Actions.ACTION_INCREMENT_WATER_COUNT
import kg.jedi.jhydratetracker.MainActivity
import kg.jedi.jhydratetracker.R
import kg.jedi.jhydratetracker.service.WaterReminderIntentService


object NotificationUtils {

    private val WATER_REMINDER_NOTIFICATION_ID = 9494
    private val WATER_REMINDER_PENDING_INTENT_ID = 2018

    private val ACTION_DRINK_PENDING_INTENT_ID = 1
    private val ACTION_IGNORE_PENDING_INTENT_ID = 23
    private val WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel"

    fun clearAllNotifications(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    fun remindUserBecauseItsTime(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(mChannel)
        }
        val notificationBuilder = Builder(context, WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_body))
                .setSmallIcon(R.drawable.ic_drop_w)
                .setLargeIcon(largeIcon(context))
                .setStyle(BigTextStyle().bigText(
                        context.getString(R.string.notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(drinkWaterAction(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.priority = PRIORITY_HIGH
        }

        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun drinkWaterAction(context: Context): Action {
        val incrementWaterCountIntent = Intent(context, WaterReminderIntentService::class.java)
        incrementWaterCountIntent.action = ACTION_INCREMENT_WATER_COUNT
        val incrementWaterPendingIntent = PendingIntent.getService(
                context,
                ACTION_DRINK_PENDING_INTENT_ID,
                incrementWaterCountIntent,
                PendingIntent.FLAG_CANCEL_CURRENT)
        return Action(R.drawable.ic_drop_w,
                context.getString(R.string.notify_action_yes),
                incrementWaterPendingIntent)
    }

    private fun ignoreReminderAction(context: Context): Action {
        val ignoreReminderIntent = Intent(context, WaterReminderIntentService::class.java)
        ignoreReminderIntent.action = ACTION_DISMISS_NOTIFICATION
        val ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        return Action(R.drawable.ic_cancel_black_24px,
                context.getString(R.string.notify_action_no),
                ignoreReminderPendingIntent)
    }

    private fun contentIntent(context: Context): PendingIntent {
        val startActivityIntent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
                context,
                WATER_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun largeIcon(context: Context): Bitmap {
        val res = context.resources
        return BitmapFactory.decodeResource(res, R.drawable.ic_drop_w)
    }

}
