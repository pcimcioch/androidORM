package org.androidorm.accessors.testclasses.onetomany;

import org.androidorm.annotations.Id;
import org.androidorm.annotations.OneToMany;

import java.util.Set;

public class TestClassOneToManyNoInverseSet1 {

    @Id
    private int id;

    @OneToMany
    private Set<TestClassOneToManyChildInverse1> children;
}
