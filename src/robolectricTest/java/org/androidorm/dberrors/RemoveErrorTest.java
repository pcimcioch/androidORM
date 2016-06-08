package org.androidorm.dberrors;

import org.androidorm.TestBase;
import org.androidorm.exceptions.DatabaseSqlException;
import org.androidorm.simple.testclasses.GeneratedIdTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RemoveErrorTest extends TestBase {

    @Test
    public void testRemoveAfeterRemoved() throws Exception {
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

        // remove
        em.remove(obj);
    }

    @Test
    public void testRemoveFromRemovedTable() throws Exception {
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

        // remove
        try {
            em.remove(obj);
        } catch (DatabaseSqlException ex) {
            assertTrue(ex.getMessage().contains("no such table"));
        }
    }

    @Test
    public void testRemoveFromIncorrectTable() throws Exception {
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
        db.execSQL("create table \"GeneratedIdTestClass\"(FIELDNO integer primary key, FIELD2 integer, FIELD3 integer);");

        // remove
        try {
            em.remove(obj);
        } catch (DatabaseSqlException ex) {
            assertTrue(ex.getMessage().contains("no such column: FIELD1"));
        }
    }
}
