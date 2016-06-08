package org.androidorm.accessors.testclasses.simple;

import org.androidorm.annotations.Id;

@SuppressWarnings("unused")
public class TestClassSimpleUnaccessible {

    @Id
    public int field1;

    public int field2;

    static int field3;

    final int field4 = 0;

    private int field5;
}
