package com.example.notifycation

import android.app.Notification
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Message
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Notifications(
        private val context: Context,
        // 通知チャンネルの表示名
        private val mName: String,
        // 通知チャンネルの説明
        private val mDescription: String,
        // 通知バッチを表示するか
        private val showNotificationBadge: Boolean = true
) {
    companion object {
        const val CHANNEL_ID = "simple-notification-channel"
        private const val GROUP_NOTIFICATION = "com.android.example.notification"
    }

    // タイトルとメッセージだけのシンプルな通知
    fun simpleNotification(): Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_24px)
                .setContentTitle("Notification Title")
                .setContentText("Notification Content Text.\nHello from Fragment")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return builder.build()
    }

    fun progressNotification(progressCurrent: Int): Notification {
        val PROGRESS_MAX = 100

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_24px)
                .setContentTitle("Notification Title")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (progressCurrent == 100) {
            // インジケーターを隠すときは setProgress(0, 0, false) を呼び出す
            builder.setProgress(0, 0, false)
        } else {
            builder.setProgress(PROGRESS_MAX, progressCurrent, false)
        }

        return builder.build()
    }

    fun indeterminateProgressNotification(): Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_24px)
                .setContentTitle("Notification Title")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(0, 0, true)

        return builder.build()
    }

    // ボタンを押すことでアクティビティのバックスタックを含むアクティビティを起動
    fun actionButtonNotification(): Notification {
        // インテントを作成
        val intent = Intent(context, MessageActivity::class.java)
                .putExtra(Intent.ACTION_SEND, "Hello From Notification!")

        val pendingIntent = TaskStackBuilder.create(context).run {
            // バックスタックにインフレートされるインテントを追加
            addNextIntentWithParentStack(intent)
            // バックスタック全体を含むPendingIntentを取得
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_24px)
                .setContentTitle("Notification Title")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_notifications_24px, "open", pendingIntent)

        return builder.build()
    }

    fun contentIntentNotification(): Notification {
        // インテントを作成
        val intent = Intent(context, MessageActivity::class.java)
                .putExtra(Intent.ACTION_SEND, "Hello From Notification!")

        val pendingIntent = TaskStackBuilder
                .create(context)
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_24px)
                .setContentTitle("Tap me!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

        return builder.build()
    }

    // 折りたたみ可能な通知
    fun expandablePictureNotification(bmp: Bitmap): Notification {
        val style = NotificationCompat.BigPictureStyle()
                .bigPicture(bmp)
                .bigLargeIcon(null)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_24px)
                .setContentTitle("Notification Title")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // アイコンを配置する
                .setLargeIcon(bmp)
                .setStyle(style)

        return builder.build()
    }

    fun expandableTextNotification(): Notification {
        val style = NotificationCompat.BigTextStyle()
                .bigText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_24px)
                .setContentTitle("Notification Title")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(style)

        return builder.build()
    }

    //　通知グループ
    fun groupNotification(message: String): Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_24px)
                .setContentText(message)
                .setGroup(GROUP_NOTIFICATION)

        return builder.build()
    }

    // Android 7.0（API レベル 24）以前のデバイス用にサマリーを作成する
    fun summaryNotification(message: List<String>): Notification {
        val messageLines = NotificationCompat.InboxStyle()
                .setBigContentTitle("${message.size} new messages")
        message.forEach { message -> messageLines.addLine("Summary: $message") }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_24px)
                .setGroup(GROUP_NOTIFICATION)
                .setContentTitle("Notification Group")
                .setContentText("Summary")
                .setStyle(messageLines)
                .setGroupSummary(true)

        return builder.build()
    }

    // NotificationManagerに対して,通知を渡すことで表示できる
    fun showNotification(notification: Notification, id: Int = R.string.app_name) {
        MyNotificationChannel.createNotificationChannel(
                "message notification",
                "This notification is test notification",
                CHANNEL_ID,
                context
        )
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id, notification)
    }
}