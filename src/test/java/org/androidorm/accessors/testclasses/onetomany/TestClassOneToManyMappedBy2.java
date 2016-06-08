package org.androidorm.accessors.testclasses.onetomany;

import org.androidorm.annotations.Id;
import org.androidorm.annotations.OneToMany;

import java.util.Set;

public class TestClassOneToManyMappedBy2 {

    @Id
    private int id;

    @OneToMany
    private Set<TestClassOneToManyChildMappedBy2> children;
}
