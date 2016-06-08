package org.androidorm.relation.testclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.androidorm.RoboTestBase;
import org.androidorm.TestBase;
import org.androidorm.annotations.GeneratedValue;
import org.androidorm.annotations.Id;
import org.androidorm.annotations.ManyToOne;
import org.androidorm.annotations.Table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@Table(name = "ManyToOneTestClass")
public class ManyToOneTestClass extends RoboTestBase {

    @Id
    @GeneratedValue
    public int field1;

    @ManyToOne
    public OneToOneTestClass field2;

    static {
        TestBase.registerTestClass(ManyToOneTestClass.class, getSqlSchema());
    }

    public ManyToOneTestClass() {
    }

    public ManyToOneTestClass(int field1, OneToOneTestClass field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    @Override
    public void assertEq(Cursor cursor) {
        assertInt(field1, cursor, "FIELD1");
        assertString(field2 != null ? field2.field1 : null, cursor, "FIELD2_ID");
    }

    public void assertEq(int f1, OneToOneTestClass f2) {
        assertEquals(this.field1, f1);
        assertSame(this.field2, f2);
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
        ManyToOneTestClass other = (ManyToOneTestClass) obj;
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
        values.put("field2_id", field2 == null ? null : field2.field1);

        db.insert("ManyToOneTestClass", null, values);
    }

    public static String getSqlSchema() {
        return "create table \"ManyToOneTestClass\"(FIELD1 integer primary key autoincrement, FIELD2_ID text);";
    }
}
