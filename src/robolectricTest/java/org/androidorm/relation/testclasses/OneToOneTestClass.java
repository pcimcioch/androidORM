package org.androidorm.relation.testclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.androidorm.RoboTestBase;
import org.androidorm.TestBase;
import org.androidorm.annotations.Id;
import org.androidorm.annotations.OneToOne;
import org.androidorm.annotations.Table;
import org.androidorm.testclasses.SimpleTestClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@Table(name = "OneToOneTestClass")
public class OneToOneTestClass extends RoboTestBase {

    @Id
    public String field1;

    public String field2;

    @OneToOne
    public SimpleTestClass field3;

    static {
        TestBase.registerTestClass(OneToOneTestClass.class, getSqlSchema());
    }

    public OneToOneTestClass() {
    }

    public OneToOneTestClass(String field1, String field2, SimpleTestClass field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    @Override
    public void assertEq(Cursor cursor) {
        assertString(field1, cursor, "FIELD1");
        assertString(field2, cursor, "FIELD2");
        assertInt(field3 != null ? field3.field1 : null, cursor, "FIELD3_ID");
    }

    public void assertEq(String f1, String f2, SimpleTestClass f3) {
        assertEquals(this.field1, f1);
        assertEquals(this.field2, f2);
        assertSame(this.field3, f3);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((field1 == null) ? 0 : field1.hashCode());
        result = prime * result + ((field2 == null) ? 0 : field2.hashCode());
        result = prime * result + ((field3 == null) ? 0 : field3.hashCode());
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
        OneToOneTestClass other = (OneToOneTestClass) obj;
        if (field1 == null) {
            if (other.field1 != null) {
                return false;
            }
        } else if (!field1.equals(other.field1)) {
            return false;
        }
        if (field2 == null) {
            if (other.field2 != null) {
                return false;
            }
        } else if (!field2.equals(other.field2)) {
            return false;
        }
        if (field3 == null) {
            if (other.field3 != null) {
                return false;
            }
        } else if (!field3.equals(other.field3)) {
            return false;
        }
        return true;
    }

    @Override
    public void insert(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("field1", field1);
        values.put("field2", field2);
        values.put("field3_id", field3 == null ? null : field3.field1);

        db.insert("OneToOneTestClass", null, values);
    }

    public static String getSqlSchema() {
        return "create table \"OneToOneTestClass\"(FIELD1 text primary key, FIELD2 text, FIELD3_ID integer);";
    }
}
