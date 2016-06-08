package org.androidorm.testclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.androidorm.RoboTestBase;
import org.androidorm.TestBase;
import org.androidorm.annotations.Column;
import org.androidorm.annotations.GeneratedValue;
import org.androidorm.annotations.Id;
import org.androidorm.annotations.Table;
import org.androidorm.annotations.Transient;

import static org.junit.Assert.assertEquals;

@Table(name = "WithTransientTestClass")
public class WithTransientTestClass extends RoboTestBase {

    @Id
    @GeneratedValue
    public int field1;

    @Transient
    public int field2;

    static {
        TestBase.registerTestClass(WithTransientTestClass.class, getSqlSchema());
    }

    public WithTransientTestClass() {
    }

    public WithTransientTestClass(int field1, int field2) {
        this.field1 = field1;
        setField2(field2);
    }

    public void setField1(int field1) {
        this.field1 = field1;
    }

    @Column
    public int getField2() {
        return field2 + 10;
    }

    public void setField2(int field2) {
        this.field2 = field2 - 10;
    }

    @Override
    public void assertEq(Cursor cursor) {
        assertInt(field1, cursor, "FIELD1");
        assertInt(getField2(), cursor, "FIELD2");
    }

    public void assertEq(int f1, int f2) {
        assertEquals(this.field1, f1);
        assertEquals(getField2(), f2);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + field1;
        result = prime * result + field2;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        WithTransientTestClass other = (WithTransientTestClass) obj;
        if (field1 != other.field1) {
            return false;
        }
        if (field2 != other.field2) {
            return false;
        }
        return true;
    }

    @Override
    public void insert(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("field1", field1);
        values.put("field2", getField2());

        db.insert("WithTransientTestClass", null, values);
    }

    public static String getSqlSchema() {
        return "create table \"WithTransientTestClass\"(FIELD1 integer primary key autoincrement, FIELD2 integer);";
    }
}
