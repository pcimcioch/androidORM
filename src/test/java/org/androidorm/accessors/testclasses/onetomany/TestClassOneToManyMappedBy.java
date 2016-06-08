package org.androidorm.accessors.testclasses.onetomany;

import org.androidorm.annotations.Id;
import org.androidorm.annotations.OneToMany;

import java.util.Set;

public class TestClassOneToManyMappedBy {

    @Id
    private int id;

    @OneToMany(mappedBy = "parent2")
    private Set<TestClassOneToManyChildMappedBy> children;
}
