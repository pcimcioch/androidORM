package org.androidorm.dberrors;

import org.androidorm.TestBase;
import org.androidorm.exceptions.DatabaseSqlException;
import org.androidorm.simple.testclasses.GeneratedIdTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class InsertErrorTest extends TestBase {

    @Test
    public void testInsertToRemovedTable() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // delete table
        db.execSQL("drop table GeneratedIdTestClass");

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(0, "Foo", 12L);
        try {
            em.persist(obj);
            fail();
        } catch (DatabaseSqlException ex) {
            assertTrue(ex.getMessage().contains("no such table"));
        }
    }

    @Test
    public void testInsertIncorrectColumns() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // alter table
        db.execSQL("drop table GeneratedIdTestClass");
        db.execSQL("create table \"GeneratedIdTestClass\"(FIELD1 integer primary key, FIELDNO integer, FIELD3 integer);");

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(0, "Foo", 12L);
        try {
            em.persist(obj);
            fail();
        } catch (DatabaseSqlException ex) {
            assertTrue(ex.getMessage().contains("has no column named FIELD2"));
        }
    }
}
