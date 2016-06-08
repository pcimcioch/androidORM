package org.androidorm.accessors.testclasses.method;

import org.androidorm.annotations.Column;
import org.androidorm.annotations.Id;

@SuppressWarnings("unused")
public class TestClassMethod {

    @Id
    public int field1;

    private int field2;

    @Column
    public String getField3() {
        return "13";
    }

    public void setField3(String val) {
    }

    public String getField4() {
        return "14";
    }

    public void setField4(String val) {
    }

    private int getField50() {
        return 15;
    }

    @Column(name = "field5")
    private void setField50(int val) {
    }
}