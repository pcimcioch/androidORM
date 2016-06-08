package org.androidorm.dberrors.testclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.androidorm.RoboTestBase;
import org.androidorm.TestBase;
import org.androidorm.annotations.EnumType;
import org.androidorm.annotations.Enumerated;
import org.androidorm.annotations.GeneratedValue;
import org.androidorm.annotations.Id;
import org.androidorm.annotations.Table;

import static org.junit.Assert.assertEquals;

@Table(name = "EnumTestClass")
public class EnumTestClass extends RoboTestBase {

    @Id
    @GeneratedValue
    public long id;

    @Enumerated(EnumType.ORDINAL)
    public HorizontalType horizont;

    @Enumerated(EnumType.STRING)
    public VerticalType vertical;

    public enum HorizontalType {
        EAST,
        WEST
    }

    public enum VerticalType {
        NORTH,
        SOUTH
    }

    static {
        TestBase.registerTestClass(EnumTestClass.class, getSqlSchema());
    }

    public EnumTestClass() {

    }

    public EnumTestClass(long id, HorizontalType horizontal, VerticalType vertical) {
        this.id = id;
        this.vertical = vertical;
        this.horizont = horizontal;
    }

    @Override
    public void assertEq(Cursor cursor) {
        assertLong(id, cursor, "ID");
        assertInt(horizont.ordinal(), cursor, "HORIZONT");
        assertString(vertical.name(), cursor, "VERTICAL");
    }

    public void assertEq(long otherId, HorizontalType otherHorizontal, VerticalType otherVertical) {
        assertEquals(this.id, otherId);
        assertEquals(this.vertical, otherVertical);
        assertEquals(this.horizont, otherHorizontal);
    }

    @Override
    public void insert(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("ID", id);
        values.put("HORIZONT", horizont.ordinal());
        values.put("VERTICAL", vertical.name());

        db.insert("EnumTestClass", null, values);
    }

    public static String getSqlSchema() {
        return "create table \"EnumTestClass\"(ID integer primary key, HORIZONT integer, VERTICAL text);";
    }
}
