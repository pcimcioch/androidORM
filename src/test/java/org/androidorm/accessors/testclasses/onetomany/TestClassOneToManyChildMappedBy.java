package org.androidorm.accessors.testclasses.onetomany;

import org.androidorm.annotations.Id;
import org.androidorm.annotations.ManyToOne;

public class TestClassOneToManyChildMappedBy {

    @Id
    public int id;

    public String name;

    @ManyToOne
    public TestClassOneToManyMappedBy parent;

    @ManyToOne
    private TestClassOneToManyMappedBy parent2;
}
