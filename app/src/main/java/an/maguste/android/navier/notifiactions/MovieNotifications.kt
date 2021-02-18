package an.maguste.android.navier.notifiactions

import an.maguste.android.navier.MainActivity
import an.maguste.android.navier.R
import an.maguste.android.navier.data.Movie

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.core.app.*
import androidx.core.net.toUri

interface Notifications {
    fun initialize()
    fun showNotification(movie: Movie)
    fun dismissNotification(movieId: Long)
}

class MovieNotifications(private val context: Context) : Notifications {

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    override fun initialize() {
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MOVIES) == null) {
            notificationManagerCompat.createNotificationChannel(
                NotificationChannelCompat.Builder(
                    CHANNEL_NEW_MOVIES,
                    NotificationManagerCompat.IMPORTANCE_DEFAULT
                )
                    .setName(context.getString(R.string.channel_new_movies))
                    .setDescription(context.getString(R.string.channel_new_movies_description))
                    .build()
            )
        }
    }

    @WorkerThread
    override fun showNotification(movie: Movie) {
        val contentUri = "an.maguste.android.navier/${movie.id}".toUri()

        val builder = NotificationCompat.Builder(context, CHANNEL_NEW_MOVIES)
            .setContentTitle(context.getString(R.string.notification_movie_recommendation_title))
            .setContentText(context.getString(R.string.notification_movie_recommendation_desc, movie.title, movie.ratings.toString()))
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

        notificationManagerCompat.notify(TAG, movie.id, builder.build())
    }

    @MainThread
    override fun dismissNotification(movieId: Long) {
        notificationManagerCompat.cancel(TAG, movieId.toInt())
    }

    companion object {
        private const val CHANNEL_NEW_MOVIES = "new_movie"
        private const val REQUEST_CONTENT = 1
        private const val TAG = "movie"
    }
}
