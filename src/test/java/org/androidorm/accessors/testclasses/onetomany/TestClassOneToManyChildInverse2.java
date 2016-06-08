package org.androidorm.accessors.testclasses.onetomany;

import org.androidorm.annotations.Id;
import org.androidorm.annotations.ManyToOne;

public class TestClassOneToManyChildInverse2 {

    @Id
    public int id;

    public String name;

    @ManyToOne
    public TestClassOneToManyNoInverseSet2 parent;
}
