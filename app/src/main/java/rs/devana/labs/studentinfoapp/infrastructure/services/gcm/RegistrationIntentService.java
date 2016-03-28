package rs.devana.labs.studentinfoapp.infrastructure.services.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.api.ApiPushNotifications;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;

public class RegistrationIntentService extends IntentService {

    private final static String TAG = RegistrationIntentService.class.getSimpleName();

    @Inject
    public ApiPushNotifications apiPushNotifications;

    @Inject
    public SharedPreferences sharedPreferences;

    private static final String AUTHORIZED_ENTITY = "494149022047";

    public RegistrationIntentService() {
        super("RegIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.getApplicationComponent().inject(this);
        Log.i(TAG, " service started.");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            int userId = sharedPreferences.getInt("userId", 0);
            if (userId != 0) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String deviceToken = instanceID.getToken(AUTHORIZED_ENTITY,
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("deviceToken", deviceToken);
                editor.apply();

                apiPushNotifications.postDeviceToken(deviceToken);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
