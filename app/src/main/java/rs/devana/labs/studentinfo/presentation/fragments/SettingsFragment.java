package rs.devana.labs.studentinfo.presentation.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.models.group.Group;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfo.infrastructure.json.parser.GroupParser;

public class SettingsFragment extends PreferenceFragmentCompat{

    @Inject
    ApiDataFetch apiDataFetch;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    GroupParser groupParser;

    ListPreference listPreference;
    private CharSequence[] entries;
    private CharSequence[] entryValues;

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

        listPreference = (ListPreference) findPreference("groups");
        setListPreferenceData(listPreference);
        listPreference.setSummary(listPreference.getEntry());
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, final Object newValue) {

                Thread getLecturesForGroup = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray jsonLectures= apiDataFetch.getLecturesForGroup(Integer.parseInt(String.valueOf(newValue)));

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("lectures", jsonLectures.toString());
                        editor.apply();
                    }
                });
                getLecturesForGroup.start();

                listPreference.setSummary(findEntryForValue((CharSequence) newValue));
                return true;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach()   {
        super.onDetach();
    }

    private void setListPreferenceData(ListPreference listPreference) {
        String jsonGroups = sharedPreferences.getString("allGroups", "");
        try {
            List<Group> groups = groupParser.parse(new JSONArray(jsonGroups));

            entries = new CharSequence[groups.size()];
            entryValues = new CharSequence[groups.size()];

            for (int i = 0; i < groups.size(); i++) {
                entries[i] = groups.get(i).name;
                entryValues[i] = String.valueOf(groups.get(i).id);
            }

            listPreference.setEntries(entries);
            listPreference.setEntryValues(entryValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private CharSequence findEntryForValue(CharSequence value){
        int i = 0;
        for (CharSequence entryValue : entryValues) {
            if (entryValue == value) {
                return entries[i];
            }
            i++;
        }
        return "";
    }
}
