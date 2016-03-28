package rs.devana.labs.studentinfoapp.infrastructure.services.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.notification.event.EventNotification;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.EventNotificationParser;
import rs.devana.labs.studentinfoapp.presentation.main.NavigationDrawerActivity;

public class GcmListeningService extends GcmListenerService {

    private static final String TAG = GcmListeningService.class.getSimpleName();

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    EventNotificationParser eventNotificationParser;

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.getApplicationComponent().inject(this);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        try {
            EventNotification eventNotification = eventNotificationParser.parse(new JSONObject(message));
            String description = eventNotification.getDescription();
            String expiresAt = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(eventNotification.getExpiresAt().getTime());
            if(sharedPreferences.getBoolean("pushNotifications", true)){
                Log.i(TAG, "Push notification sent.");
                sendNotification(expiresAt+"-"+description, "notifications");
            }
            else {
                Log.i(TAG, "Push notifications are disabled and the message is not sent.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendNotification(String message, String fragment) {
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        intent.putExtra("fragment", fragment);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("StudentInfo")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (sharedPreferences.getBoolean("vibrationEnabled", true)){
            notificationBuilder.setVibrate(new long[]{1000, 1000, 1000});
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
