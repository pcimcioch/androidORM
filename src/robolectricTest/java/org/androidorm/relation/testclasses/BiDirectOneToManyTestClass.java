package org.androidorm.relation.testclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.androidorm.RoboTestBase;
import org.androidorm.TestBase;
import org.androidorm.annotations.GeneratedValue;
import org.androidorm.annotations.Id;
import org.androidorm.annotations.OneToMany;
import org.androidorm.annotations.Table;

import java.util.Set;

@Table(name = "BiDirectOneToManyTestClass")
public class BiDirectOneToManyTestClass extends RoboTestBase {

    @Id
    @GeneratedValue
    public int field1;

    @OneToMany
    public Set<BiDirectManyToOneTestClass> field2;

    static {
        TestBase.registerTestClass(BiDirectOneToManyTestClass.class, getSqlSchema());
    }

    public BiDirectOneToManyTestClass() {
    }

    public BiDirectOneToManyTestClass(int field1) {
        this.field1 = field1;
    }

    @Override
    public void assertEq(Cursor cursor) {
        assertInt(field1, cursor, "FIELD1");
    }

    @Override
    public void insert(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("field1", field1);

        db.insert("BiDirectOneToManyTestClass", null, values);
    }

    public static String getSqlSchema() {
        return "create table \"BiDirectOneToManyTestClass\"(FIELD1 integer primary key autoincrement);";
    }
}
