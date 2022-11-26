package akashsarkar188.expensedaroga.utils

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.screens.addTransaction.AddTransactionActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import kotlin.random.Random

object NotificationHelper {

    fun openTransactionsInBubble(context: Context, monthYear: String) {
        // declare all the vars and val required
        var chatPartner: Person? = null
        val shortcutId: String = "monthly_transactions"
        var shortcut: ShortcutInfoCompat? = null
        var bubbleData: NotificationCompat.BubbleMetadata? = null
        val notificationManager: NotificationManager
        val targetIntent = Intent(context, AddTransactionActivity::class.java).putExtra(BUNDLE_MONTH_YEAR_STRING, monthYear)
        val bubbleIntent = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val category = "com.example.category.TEXT_SHARE_TARGET"
        val icon = IconCompat.createWithAdaptiveBitmap(
            context.resources.assets.open("darogaSquare.jpg").use { input ->
                BitmapFactory.decodeStream(input)
            })

        // vars for notification
        val channelId: String = "GeneralNotification"
        val channelName: String = "General Notifications"
        val channelDescriptions: String = "General notifications to keep you updated"
        val channel: NotificationChannel

        // lets create a person to be shown in bubble
        chatPartner = Person.Builder()
            .setName("Daroga")
            .setImportant(true)
            .setBot(false)
            .setIcon(icon)
            .build()

        // lets create a shortcut
        shortcut = ShortcutInfoCompat.Builder(context, shortcutId)
            .setCategories(setOf(category))
            .setIntent(Intent(Intent.ACTION_DEFAULT))
            .setLongLived(true)
            .setIcon(icon)
            .setShortLabel("Daroga")
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)

        // bubble data
        bubbleData = NotificationCompat.BubbleMetadata.Builder(
            bubbleIntent,
            icon
        )
            .setDesiredHeight(800)
            .setIntent(bubbleIntent)
            .setAutoExpandBubble(true)
            .setSuppressNotification(true)
            .build()

        // setup notification channel for android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                channel.setAllowBubbles(true)
            }
            channel.description = channelDescriptions

            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // messaging style as chat
        val style: NotificationCompat.MessagingStyle =
            NotificationCompat.MessagingStyle(chatPartner)
                .setGroupConversation(false)

        val notificationCompatBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(
                context,
                channelId
            )
                .setSmallIcon(R.drawable.send_icon)
                .setContentTitle("daroga ji")
                .setContentText(":)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setBubbleMetadata(bubbleData)
                .setShortcutId(shortcutId)
                .setStyle(style)
                .addPerson(chatPartner)

        with(NotificationManagerCompat.from(context)) {
            notify(1, notificationCompatBuilder.build())
        }
    }
}