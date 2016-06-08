package org.androidorm.dberrors;

import org.androidorm.TestBase;
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
public class UpdateErrorTest extends TestBase {

    @Test
    public void testUpdateAfeterRemoved() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(0, "Foo", 12L);
        em.persist(obj);

        // check
        obj.assertEq(1, "Foo", 12L);
        assertEq(getAll(GeneratedIdTestClass.class, db), obj);

        // remove
        db.delete("GeneratedIdTestClass", "field1 = ?", new String[]{"1"});

        // update
        obj.field2 = "Bar";
        try {
            em.update(obj);
            fail();
        } catch (DatabaseStateException ex) {
            assertTrue(ex.getMessage().contains("can't find entity"));
        }
    }

    @Test
    public void testUpdateToRemovedTable() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(0, "Foo", 12L);
        em.persist(obj);

        // check
        obj.assertEq(1, "Foo", 12L);
        assertEq(getAll(GeneratedIdTestClass.class, db), obj);

        // delete table
        db.execSQL("drop table GeneratedIdTestClass");

        // update
        obj.field2 = "Bar";
        try {
            em.update(obj);
            fail();
        } catch (DatabaseSqlException ex) {
            assertTrue(ex.getMessage().contains("no such table"));
        }
    }

    @Test
    public void testUpdateToIncorrectTable() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(0, "Foo", 12L);
        em.persist(obj);

        // check
        obj.assertEq(1, "Foo", 12L);
        assertEq(getAll(GeneratedIdTestClass.class, db), obj);

        // alter table
        db.execSQL("drop table GeneratedIdTestClass");
        db.execSQL("create table \"GeneratedIdTestClass\"(FIELD1 integer primary key, FIELDNO integer, FIELD3 integer);");

        // update
        obj.field2 = "Bar";
        try {
            em.update(obj);
            fail();
        } catch (DatabaseSqlException ex) {
            assertTrue(ex.getMessage().contains("no such column: FIELD2"));
        }
    }
}
