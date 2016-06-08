package org.androidorm.accessors.testclasses.method;

import org.androidorm.annotations.Column;

@SuppressWarnings("unused")
public class TestClassMethodInheritance extends TestClassMethod {

    private int field6;

    protected int getField7() {
        return 17;
    }

    @Column
    protected void setField7(int v) {
    }

    public int getField8() {
        return 18;
    }

    public void setField8() {
    }

    @Override
    public String getField3() {
        return "17";
    }
}
