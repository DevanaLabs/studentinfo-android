package rs.devana.labs.studentinfo.infrastructure.repository;

import org.json.JSONArray;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.models.group.Group;
import rs.devana.labs.studentinfo.domain.models.group.GroupRepositoryInterface;
import rs.devana.labs.studentinfo.infrastructure.json.parser.GroupParser;

public class GroupRepository implements GroupRepositoryInterface {
    ApiDataFetch apiDataFetch;
    GroupParser groupParser;

    @Inject
    public GroupRepository(ApiDataFetch apiDataFetch, GroupParser groupParser){
        this.apiDataFetch = apiDataFetch;
        this.groupParser = groupParser;
    }

    @Override
    public List<Group> getGroups() {
        JSONArray jsonGroups = apiDataFetch.getAllGroups();

        return groupParser.parse(jsonGroups);
    }
}
