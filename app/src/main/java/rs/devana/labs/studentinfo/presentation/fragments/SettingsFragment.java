package rs.devana.labs.studentinfo.presentation.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.models.group.Group;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfo.infrastructure.event_bus_events.GroupChangedEvent;
import rs.devana.labs.studentinfo.infrastructure.event_bus_events.GroupsFetchedEvent;
import rs.devana.labs.studentinfo.infrastructure.event_bus_events.ScheduleFetchedEvent;
import rs.devana.labs.studentinfo.infrastructure.json.parser.GroupParser;

public class SettingsFragment extends PreferenceFragmentCompat{

    @Inject
    ApiDataFetch apiDataFetch;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    GroupParser groupParser;
    @Inject
    EventBus eventBus;

    ListPreference listPreference;
    private CharSequence[] entries;
    private CharSequence[] entryValues;
    ProgressDialog groupFetchDialog, scheduleFetchDialog;

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
        EventBus.getDefault().register(this);

        listPreference = (ListPreference) findPreference("groups");
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, final Object newValue) {

                new ScheduleFetchTask().execute((String)newValue);
                scheduleFetchDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.pleaseWait), getResources().getString(R.string.fetchingSchedule), true);
                CharSequence group = findEntryForValue((CharSequence) newValue);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("groupName", group.toString());
                editor.apply();
                listPreference.setSummary(group);
                eventBus.post(new GroupChangedEvent(group.toString()));

                return true;
            }
        });
        if (sharedPreferences.getString("allGroups", "").isEmpty()) {
            groupFetchDialog = ProgressDialog.show(getActivity(),  getResources().getString(R.string.pleaseWait), getResources().getString(R.string.fetchingGroups), true);
        } else {
            setListPreferenceData(listPreference);
        }
        listPreference.setSummary(listPreference.getEntry());
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

    @Subscribe
    public void onGroupsFetchedEvent(GroupsFetchedEvent groupsFetchedEvent){
        groupFetchDialog.dismiss();
        setListPreferenceData(listPreference);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private class ScheduleFetchTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            return apiDataFetch.getLecturesForGroup(Integer.parseInt(params[0]));
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lectures", jsonArray.toString());
            editor.apply();
            scheduleFetchDialog.dismiss();
            eventBus.post(new ScheduleFetchedEvent(jsonArray.toString()));
        }
    }
}
