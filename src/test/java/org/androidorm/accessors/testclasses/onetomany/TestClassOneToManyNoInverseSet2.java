package org.androidorm.accessors.testclasses.onetomany;

import org.androidorm.annotations.Id;
import org.androidorm.annotations.OneToMany;

import java.util.Set;

public class TestClassOneToManyNoInverseSet2 {

    @Id
    private int id;

    @OneToMany(mappedBy = "noparent")
    private Set<TestClassOneToManyChildInverse2> children;
}
