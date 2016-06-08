package org.androidorm.accessors.testclasses.onetomany;

import org.androidorm.annotations.Id;
import org.androidorm.annotations.OneToMany;

import java.util.Set;

public class TestClassOneToManyRaw {

    @Id
    private int id;

    @SuppressWarnings("rawtypes")
    @OneToMany
    private Set children;
}
