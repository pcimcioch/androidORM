package org.androidorm.simple.testclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.androidorm.RoboTestBase;
import org.androidorm.TestBase;
import org.androidorm.annotations.Id;
import org.androidorm.annotations.Table;

import static org.junit.Assert.assertEquals;

@Table(name = "VariousTypesTestClass")
public class VariousTypesTestClass extends RoboTestBase {

    @Id
    public int field1;

    public long field2;

    public short field3;

    public byte field4;

    public String field5;

    public boolean field6;

    public float field7;

    public double field8;

    static {
        TestBase.registerTestClass(VariousTypesTestClass.class, getSqlSchema());
    }

    public VariousTypesTestClass() {
    }

    public VariousTypesTestClass(int field1, long field2, short field3, byte field4, String field5, boolean field6, float field7, double field8) {
        setFields(field1, field2, field3, field4, field5, field6, field7, field8);
    }

    public void setFields(int field1, long field2, short field3, byte field4, String field5, boolean field6, float field7, double field8) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
        this.field5 = field5;
        this.field6 = field6;
        this.field7 = field7;
        this.field8 = field8;
    }

    @Override
    public void insert(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("field1", field1);
        values.put("field2", field2);
        values.put("field3", field3);
        values.put("field4", field4);
        values.put("field5", field5);
        values.put("field6", field6);
        values.put("field7", field7);
        values.put("field8", field8);

        db.insert("VariousTypesTestClass", null, values);
    }

    @Override
    public void assertEq(Cursor cursor) {
        assertInt(field1, cursor, "FIELD1");
        assertLong(field2, cursor, "FIELD2");
        assertShort(field3, cursor, "FIELD3");
        assertShort((short) field4, cursor, "FIELD4");
        assertString(field5, cursor, "FIELD5");
        assertInt(field6 ? 1 : 0, cursor, "FIELD6");
        assertFloat(field7, cursor, "FIELD7");
        assertDouble(field8, cursor, "FIELD8");
    }

    public void assertEq(int f1, long f2, short f3, byte f4, String f5, boolean f6, float f7, double f8) {
        assertEquals(this.field1, f1);
        assertEquals(this.field2, f2);
        assertEquals(this.field3, f3);
        assertEquals(this.field4, f4);
        assertEquals(this.field5, f5);
        assertEquals(this.field6, f6);
        assertEquals(this.field7, f7, 0.001);
        assertEquals(this.field8, f8, 0.001);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + field1;
        result = prime * result + (int) (field2 ^ (field2 >>> 32));
        result = prime * result + field3;
        result = prime * result + field4;
        result = prime * result + ((field5 == null) ? 0 : field5.hashCode());
        result = prime * result + (field6 ? 1231 : 1237);
        result = prime * result + Float.floatToIntBits(field7);
        long temp;
        temp = Double.doubleToLongBits(field8);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        VariousTypesTestClass other = (VariousTypesTestClass) obj;
        if (field1 != other.field1) {
            return false;
        }
        if (field2 != other.field2) {
            return false;
        }
        if (field3 != other.field3) {
            return false;
        }
        if (field4 != other.field4) {
            return false;
        }
        if (field5 == null) {
            if (other.field5 != null) {
                return false;
            }
        } else if (!field5.equals(other.field5)) {
            return false;
        }
        if (field6 != other.field6) {
            return false;
        }
        if (Float.floatToIntBits(field7) != Float.floatToIntBits(other.field7)) {
            return false;
        }
        if (Double.doubleToLongBits(field8) != Double.doubleToLongBits(other.field8)) {
            return false;
        }
        return true;
    }

    public static String getSqlSchema() {
        return "create table \"VariousTypesTestClass\"(FIELD1 integer primary key, FIELD2 integer, FIELD3 integer, FIELD4 integer, FIELD5 text, FIELD6 integer, FIELD7 real, "
                + "FIELD8 real);";
    }
}
