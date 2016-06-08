package org.androidorm.accessors.testclasses.simple;

import org.androidorm.annotations.Id;

@SuppressWarnings("unused")
public class TestClassSimpleVisibility {

    @Id
    public int field1;

    private int field2;

    protected int field3;

    int field4;
}
