package rs.devana.labs.studentinfo.presentation.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.models.group.Group;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfo.infrastructure.json.parser.GroupParser;

public class SettingsFragment extends PreferenceFragment{

    @Inject
    ApiDataFetch apiDataFetch;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    GroupParser groupParser;

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
    public void onDetach()   {
        super.onDetach();
    }

    private void setListPreferenceData(ListPreference listPreference) {
        String jsonGroups = sharedPreferences.getString("allGroups", "");
        try {
            List<Group> groups = groupParser.parse(new JSONArray(jsonGroups));

            CharSequence[] entries = new CharSequence[groups.size()];
            CharSequence[] entryValues = new CharSequence[groups.size()];

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
}
