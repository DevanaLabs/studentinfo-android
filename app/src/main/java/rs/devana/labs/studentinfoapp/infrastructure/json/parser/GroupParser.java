package rs.devana.labs.studentinfoapp.infrastructure.json.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.group.Group;

public class GroupParser {
    @Inject
    public GroupParser(){
    }

    public Group parse(JSONObject jsonGroup) {
        try {
            return new Group(jsonGroup.getInt("id"), jsonGroup.getInt("year"), jsonGroup.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Group> parse(JSONArray jsonGroups){
        List<Group> groups = new ArrayList<>();

        int i = 0;
        while (i < jsonGroups.length()) {
            try {
                JSONObject jsonGroup = jsonGroups.getJSONObject(i);

                Group group = new Group(jsonGroup.getInt("id"), jsonGroup.getInt("year"), jsonGroup.getString("name"));

                groups.add(group);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                i++;
            }
        }
        Collections.sort(groups, new Comparator<Group>() {
            @Override
            public int compare(Group lhs, Group rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        });
        return groups;
    }
}
