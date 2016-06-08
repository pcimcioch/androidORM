package org.androidorm.accessors.testclasses.simple;

import org.androidorm.annotations.Id;
import org.androidorm.annotations.Transient;

public class TestClassSimpleTransient {

    public transient int field1;

    public transient int field2;

    @Transient
    public int field3;

    @Transient
    public int field4;

    @Id
    public int field5;

    public int field6;
}
