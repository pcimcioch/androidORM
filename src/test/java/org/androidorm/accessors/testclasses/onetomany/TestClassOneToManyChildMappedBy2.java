package org.androidorm.accessors.testclasses.onetomany;

import org.androidorm.annotations.Id;
import org.androidorm.annotations.ManyToOne;

public class TestClassOneToManyChildMappedBy2 {

    @Id
    public int id;

    public String name;

    @ManyToOne
    public TestClassOneToManyMappedBy2 parent;

    @ManyToOne
    private TestClassOneToManyMappedBy2 parent2;
}
