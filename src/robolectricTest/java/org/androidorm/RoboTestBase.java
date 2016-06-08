package org.androidorm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class RoboTestBase {
	protected void assertInt(Integer arg, Cursor c, String column) {
		if (!checkNull(arg, c, column)) {
			assertEquals((int) arg, c.getInt(c.getColumnIndex(column)));
		}
	}

	protected void assertLong(Long arg, Cursor c, String column) {
		if (!checkNull(arg, c, column)) {
			assertEquals((long) arg, c.getLong(c.getColumnIndex(column)));
		}
	}

	protected void assertShort(Short arg, Cursor c, String column) {
		if (!checkNull(arg, c, column)) {
			assertEquals((short) arg, c.getShort(c.getColumnIndex(column)));
		}
	}

	protected void assertByte(Byte arg, Cursor c, String column) {
		if (!checkNull(arg, c, column)) {
			assertEquals((byte) arg, (byte) c.getShort(c.getColumnIndex(column)));
		}
	}

	protected void assertString(String arg, Cursor c, String column) {
		if (!checkNull(arg, c, column)) {
			assertEquals(arg, c.getString(c.getColumnIndex(column)));
		}
	}

	protected void assertFloat(Float arg, Cursor c, String column) {
		if (!checkNull(arg, c, column)) {
			assertEquals(arg, c.getFloat(c.getColumnIndex(column)), 0.001);
		}
	}

	protected void assertDouble(Double arg, Cursor c, String column) {
		if (!checkNull(arg, c, column)) {
			assertEquals(arg, c.getDouble(c.getColumnIndex(column)), 0.001);
		}
	}

	private boolean checkNull(Object arg, Cursor c, String column) {
		if (arg == null) {
			assertTrue(c.isNull(c.getColumnIndex(column)));
			return true;
		}
		return false;
	}

	public abstract void assertEq(Cursor cursor);

	public abstract void insert(SQLiteDatabase db);
}