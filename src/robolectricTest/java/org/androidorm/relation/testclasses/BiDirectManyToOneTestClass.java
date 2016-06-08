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

@Table(name = "BiDirectManyToOneTestClass")
public class BiDirectManyToOneTestClass extends RoboTestBase {

    @Id
    @GeneratedValue
    public int field1;

    @ManyToOne
    public BiDirectOneToManyTestClass field2;

    static {
        TestBase.registerTestClass(BiDirectManyToOneTestClass.class, getSqlSchema());
    }

    public BiDirectManyToOneTestClass() {
    }

    public BiDirectManyToOneTestClass(int field1, BiDirectOneToManyTestClass field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    @Override
    public void assertEq(Cursor cursor) {
        assertInt(field1, cursor, "FIELD1");
        assertInt(field2 != null ? field2.field1 : null, cursor, "FIELD2_ID");
    }

    @Override
    public void insert(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("field1", field1);
        values.put("field2_id", field2 == null ? null : field2.field1);

        db.insert("BiDirectManyToOneTestClass", null, values);
    }

    public static String getSqlSchema() {
        return "create table \"BiDirectManyToOneTestClass\"(FIELD1 integer primary key autoincrement, FIELD2_ID integer);";
    }
}
