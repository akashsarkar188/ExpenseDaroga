package akashsarkar188.expensedaroga

import akashsarkar188.expensedaroga.addTransaction.AddTransactionActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openAddTransaction(true, "Oct 2022")
    }

    private fun openAddTransaction(showInBubble: Boolean, monthYearString: String) {

        if (showInBubble) {
            var chatPartner: Person? = null
            var shortcutId: String? = null
            var shortcut: ShortcutInfoCompat? = null
            var bubbleData: NotificationCompat.BubbleMetadata? = null
            var notificationManager: NotificationManager

            val targetIntent = Intent(this, AddTransactionActivity::class.java)
            val bubbleIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_MUTABLE)
            val category = "com.example.category.IMG_SHARE_TARGET"
            val icon = IconCompat.createWithAdaptiveBitmap(
                resources.assets.open("darogaSquare.jpg").use { input ->
                    BitmapFactory.decodeStream(input)
                }
            )
            chatPartner = Person.Builder()
                .setName("Daroga")
                .setImportant(true)
                .setBot(false)
                .setIcon(icon)
                .build()

            shortcutId = "expense.daroga.shortcut"

            shortcut = ShortcutInfoCompat.Builder(this, shortcutId)
                .setCategories(setOf(category))
                .setIntent(Intent(Intent.ACTION_DEFAULT))
                .setLongLived(true)
                .setIcon(icon)
                .setShortLabel("Daroga")
                .build()

            ShortcutManagerCompat.pushDynamicShortcut(this, shortcut)

            bubbleData = NotificationCompat.BubbleMetadata.Builder(
                bubbleIntent,
                icon
            )
                .setDesiredHeight(800)
                .setIntent(bubbleIntent)
                .setAutoExpandBubble(true)
                .setSuppressNotification(true)
                .build()

            var channelId: String = "GeneralNotification"
            var channelName: String = "General Notifications"
            var channelDescriptions: String = "General notifications to keep you updated"
            var channel: NotificationChannel


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel =
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    channel.setAllowBubbles(true)
                }
                channel.description = channelDescriptions

                notificationManager =
                    this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }


            val style: NotificationCompat.MessagingStyle =
                NotificationCompat.MessagingStyle(chatPartner)
                    .setGroupConversation(false)

            var notificationCompatBuilder: NotificationCompat.Builder

            notificationCompatBuilder =
                NotificationCompat.Builder(
                    this,
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

            with(NotificationManagerCompat.from(this)) {
                notify(1, notificationCompatBuilder.build())
            }

        } else {
            startActivity(Intent(this, AddTransactionActivity()::class.java))
        }
    }
}