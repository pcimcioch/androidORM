package org.androidorm.dberrors;

import org.androidorm.TestBase;
import org.androidorm.dberrors.testclasses.EnumTestClass;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.DatabaseSqlException;
import org.androidorm.exceptions.DatabaseStateException;
import org.androidorm.simple.testclasses.GeneratedIdTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FindErrorTest extends TestBase {

    @Test
    public void testFindInRemovedTable() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // delete table
        db.execSQL("drop table GeneratedIdTestClass");

        // persist
        try {
            em.find(GeneratedIdTestClass.class, 12);
            fail();
        } catch (DatabaseSqlException ex) {
            assertTrue(ex.getMessage().contains("no such table"));
        }
    }

    @Test
    public void testFindIncorrectColumns() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // alter table
        db.execSQL("drop table GeneratedIdTestClass");
        db.execSQL("create table \"GeneratedIdTestClass\"(FIELD1 integer primary key, FIELDNO integer, FIELD3 integer);");

        // persist
        try {
            em.find(GeneratedIdTestClass.class, 12);
            fail();
        } catch (DatabaseSqlException ex) {
            assertTrue(ex.getMessage().contains("no such column: FIELD2"));
        }
    }

    @Test
    public void testEnumIncorrectOrdinal() throws AndroidOrmException {
        // prepare
        init(EnumTestClass.class);

        // alter table
        db.execSQL("insert into EnumTestClass(ID, HORIZONT, VERTICAL) values(1, 2, \"NORTH\")");

        // find
        try {
            em.find(EnumTestClass.class, 1L);
            fail();
        } catch (DatabaseStateException ex) {
            assertTrue(ex.getMessage().contains("Enum with ordinal 2 not found"));
        }
    }

    @Test
    public void testEnumIncorrectString() throws AndroidOrmException {
        // prepare
        init(EnumTestClass.class);

        // alter table
        db.execSQL("insert into EnumTestClass(ID, HORIZONT, VERTICAL) values(1, 1, \"MISSINGNO\")");

        // find
        try {
            em.find(EnumTestClass.class, 1L);
            fail();
        } catch (DatabaseStateException ex) {
            assertTrue(ex.getMessage().contains("Enum with string MISSINGNO not found"));
        }
    }
}
