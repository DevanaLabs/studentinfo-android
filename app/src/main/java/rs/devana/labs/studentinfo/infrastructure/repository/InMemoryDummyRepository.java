package rs.devana.labs.studentinfo.infrastructure.repository;

import java.util.ArrayList;
import java.util.List;

import rs.devana.labs.studentinfo.domain.dummy.Dummy;
import rs.devana.labs.studentinfo.domain.dummy.DummyRepository;

public class InMemoryDummyRepository implements DummyRepository {

    @Override
    public List<Dummy> getAll() {
        List<Dummy> dummies = new ArrayList<>();
        dummies.add(new Dummy("Dummy #1"));
        dummies.add(new Dummy("Dummy #2"));
        dummies.add(new Dummy("Dummy #3"));
        dummies.add(new Dummy("Dummy #4"));
        dummies.add(new Dummy("Dummy #5"));
        dummies.add(new Dummy("Dummy #6"));
        dummies.add(new Dummy("Dummy #7"));
        dummies.add(new Dummy("Dummy #8"));
        dummies.add(new Dummy("Dummy #9"));
        dummies.add(new Dummy("Dummy #10"));
        return dummies;
    }
}
