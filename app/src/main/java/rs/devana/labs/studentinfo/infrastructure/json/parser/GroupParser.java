package rs.devana.labs.studentinfo.infrastructure.json.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.group.Group;

public class GroupParser {
    @Inject
    public GroupParser(){
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
        return groups;
    }
}
