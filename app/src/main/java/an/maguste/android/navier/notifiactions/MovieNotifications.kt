package an.maguste.android.navier.notifiactions

import an.maguste.android.navier.MainActivity
import an.maguste.android.navier.R
import an.maguste.android.navier.data.Movie

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.app.*
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri


/**
 * Handles all operations related to [Notification].
 */
interface Notifications {
    fun initialize()
    fun showNotification(movie: Movie)
    //fun dismissNotification(chatId: Long)
}

class MovieNotifications(private val context: Context) : Notifications  {

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    override fun initialize() {
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MESSAGES) == null) {
            notificationManagerCompat.createNotificationChannel(
                NotificationChannelCompat.Builder(CHANNEL_NEW_MESSAGES,
                    NotificationManagerCompat.IMPORTANCE_DEFAULT
                )
                    .setName("context.getString(R.string.channel_new_messages)")
                    .setDescription("context.getString(R.string.channel_new_messages_description)")
                    .build()
            )
        }
    }
/*
    Plan:
    Step 1. Support deep links to open a movie screen by movie ID.
    Step 2. <compare and find new movie with highest rating>
    Step 3. <fire a notification with deep link>
    Step 4*. Add a button to the movie screen to schedule a watch.
    Step 5*. Ask the user for Calendar access permission and declare related permission in manifest.
    Step 6*. Ask the user for preferred date & time (via dialogs, for example) and schedule a watch in the primary calendar on the device.
*/

    @WorkerThread
    override fun showNotification(movie: Movie) {
        val contentUri = "an.maguste.android.navier/${movie.id}".toUri()

        val builder = NotificationCompat.Builder(context, CHANNEL_NEW_MESSAGES)
            .setContentTitle(/*chat.contact.name*/movie.title)
            .setContentText(/*chat.messages.last().text*/movie.overview)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            // Direct Reply
           /* .addReplyAction(context,contentUri)
            .setStyle(
                NotificationCompat.MessagingStyle(person)
                    .run {
                        for (message in chat.messages) {
                            val m = NotificationCompat.MessagingStyle.Message(
                                message.text,
                                message.timestamp,
                                if (message.isIncoming) person else null
                            ).apply {
                                if (message.photoUri != null) {
                                    setData(message.photoMimeType, message.photoUri)
                                }
                            }
                            if (!message.isNew) {
                                addHistoricMessage(m)
                            } else {
                                addMessage(m)
                            }
                        }
                        this
                    }
                    .setGroupConversation(false)
            )
            .setWhen(chat.messages.last().timestamp)*/

        notificationManagerCompat.notify(CHAT_TAG, movie.id, builder.build())
    }


    companion object {
        /**
         * The notification channel for messages. This is used for showing Bubbles.
         */
        private const val CHANNEL_NEW_MESSAGES = "new_messages"

        private const val REQUEST_CONTENT = 1

        private const val CHAT_TAG = "chat"
    }
}
