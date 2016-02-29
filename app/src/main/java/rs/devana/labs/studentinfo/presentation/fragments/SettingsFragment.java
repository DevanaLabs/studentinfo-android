package rs.devana.labs.studentinfo.presentation.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.util.Log;

import org.json.JSONArray;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;

public class SettingsFragment extends PreferenceFragment {

    @Inject
    ApiDataFetch apiDataFetch;
    @Inject
    SharedPreferences sharedPreferences;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_fragment);
        Injector.INSTANCE.getApplicationComponent().inject(this);

        ListPreference listPreference = (ListPreference) findPreference("groups");
        setListPreferenceData(listPreference);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setListPreferenceData(ListPreference listPreference) {
        final String accessToken = sharedPreferences.getString("accessToken", "");
//        final JSONArray[] jsonGroups = new JSONArray[1];
//        if (!accessToken.equals("")) {
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    jsonGroups[0] = apiDataFetch.getAllGroups(accessToken);
//                }
//            });
//            t.start();
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Log.i("GRUPE", jsonGroups[0].toString());
//        }
        CharSequence[] entries = new CharSequence[1];
        entries[0] = "Grupa nije izabrana";
        CharSequence[] entryValues = new CharSequence[1];
        entryValues[0] = "0";
//        while (i < jsonGroups[0].length()){
//
//        }
        listPreference.setEntries(entries);
        listPreference.setDefaultValue("0");
        listPreference.setEntryValues(entryValues);
    }
}
