package org.androidorm.accessors.testclasses.simple;

import org.androidorm.accessors.DbAccessor;
import org.androidorm.annotations.Id;

import java.util.Date;

public class TestClassSimpleTypes {

    @Id
    public String field1;

    public int field2;

    public Integer field3;

    public long field4;

    public Long field5;

    public Double field6;

    public double field7;

    public Float field8;

    public float field9;

    public Date field10;

    public TestClassSimpleTransient field11;

    public DbAccessor field12;
}
