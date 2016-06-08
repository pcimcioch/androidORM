package org.androidorm.accessors.testclasses.onetomany;

import org.androidorm.annotations.Id;
import org.androidorm.annotations.ManyToOne;

public class TestClassOneToManyChild {

    @Id
    public int id;

    public String name;

    @ManyToOne
    public TestClassOneToManyList parent;

    @ManyToOne
    private TestClassOneToManySet parent2;
}
