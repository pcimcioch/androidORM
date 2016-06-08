package org.androidorm.accessors.testclasses.onetomany;

import org.androidorm.annotations.Id;
import org.androidorm.annotations.OneToMany;

import java.util.List;

public class TestClassOneToManyList {

    @Id
    private int id;

    @OneToMany
    private List<TestClassOneToManyChild> children;
}
