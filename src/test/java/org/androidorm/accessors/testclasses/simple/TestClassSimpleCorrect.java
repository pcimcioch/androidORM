package org.androidorm.accessors.testclasses.simple;

import org.androidorm.accessors.DbAccessor;
import org.androidorm.annotations.Column;
import org.androidorm.annotations.Id;

@SuppressWarnings("unused")
public class TestClassSimpleCorrect {

    @Id
    @Column(name = "id")
    public int field1;

    private String field2;

    @Column(length = 12)
    protected String field3;

    @Column(unique = true)
    private double field4;

    protected DbAccessor field5;

    @Column(nullable = false)
    private Float field6;

    public transient int field7;
}
