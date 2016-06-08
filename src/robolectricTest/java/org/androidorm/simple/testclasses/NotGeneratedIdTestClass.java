package org.androidorm.simple.testclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.androidorm.RoboTestBase;
import org.androidorm.TestBase;
import org.androidorm.annotations.Id;
import org.androidorm.annotations.Table;

import static org.junit.Assert.assertEquals;

@Table(name = "NotGeneratedIdTestClass")
public class NotGeneratedIdTestClass extends RoboTestBase {

    @Id
    public int field1;

    public String field2;

    static {
        TestBase.registerTestClass(NotGeneratedIdTestClass.class, getSqlSchema());
    }

    public NotGeneratedIdTestClass() {
    }

    public NotGeneratedIdTestClass(int field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public void setField1(int field1) {
        this.field1 = field1;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    @Override
    public void assertEq(Cursor cursor) {
        assertInt(field1, cursor, "FIELD1");
        assertString(field2, cursor, "FIELD2");
    }

    public void assertEq(int f1, String f2) {
        assertEquals(this.field1, f1);
        assertEquals(this.field2, f2);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + field1;
        result = prime * result + ((field2 == null) ? 0 : field2.hashCode());
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
        NotGeneratedIdTestClass other = (NotGeneratedIdTestClass) obj;
        if (field1 != other.field1) {
            return false;
        }
        if (field2 == null) {
            if (other.field2 != null) {
                return false;
            }
        } else if (!field2.equals(other.field2)) {
            return false;
        }
        return true;
    }

    @Override
    public void insert(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("field1", field1);
        values.put("field2", field2);

        db.insert("NotGeneratedIdTestClass", null, values);
    }

    public static String getSqlSchema() {
        return "create table \"NotGeneratedIdTestClass\"(FIELD1 integer primary key, FIELD2 text);";
    }
}
