package rs.devana.labs.studentinfo.infrastructure.services.groups;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import org.json.JSONArray;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;

public class FetchGroupsService extends IntentService {
    @Inject
    ApiDataFetch apiDataFetch;

    @Inject
    SharedPreferences sharedPreferences;

    public FetchGroupsService() {
        super("FetchGroupService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.getApplicationComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        JSONArray jsonGroups = apiDataFetch.getAllGroups();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("allGroups", jsonGroups.toString());
        editor.apply();
    }
}
