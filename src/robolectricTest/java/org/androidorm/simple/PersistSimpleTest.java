package org.androidorm.simple;

import android.database.Cursor;
import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.DatabaseSqlException;
import org.androidorm.exceptions.EntityStateException;
import org.androidorm.simple.testclasses.GeneratedIdTestClass;
import org.androidorm.simple.testclasses.NotGeneratedIdTestClass;
import org.androidorm.simple.testclasses.VariousTypesTestClass;
import org.androidorm.testclasses.SimpleTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class PersistSimpleTest extends TestBase {

    @Test
    public void testSimpleTypesTest() throws Exception {
        // prepare
        init(VariousTypesTestClass.class);

        // persist
        VariousTypesTestClass obj = new VariousTypesTestClass(1, 2L, (short) 3, (byte) 4, "five", true, 7.0f, 8.0);
        em.persist(obj);

        // check
        obj.assertEq(1, 2L, (short) 3, (byte) 4, "five", true, 7.0f, 8.0);
        assertEq(getAll(VariousTypesTestClass.class, db), obj);
    }

    @Test
    public void testBasicPersistTest() throws Exception {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // persist
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(10, "Hello");
        em.persist(obj);

        // check
        obj.assertEq(10, "Hello");
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);
    }

    @Test
    public void testSetAutoIdTest() throws AndroidOrmException {
        // prepare
        init(SimpleTestClass.class);

        // persist
        SimpleTestClass obj = new SimpleTestClass(0, "Hello2");
        em.persist(obj);

        // check
        obj.assertEq(1, "Hello2");

        assertEq(getAll(SimpleTestClass.class, db), obj);
    }

    @Test
    public void testPersistAttachedTest() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // persist
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(10, "Hello");
        em.persist(obj);

        // check
        obj.assertEq(10, "Hello");
        Cursor cursor = getAll(NotGeneratedIdTestClass.class, db);
        assertEquals(1, cursor.getCount());
        cursor.close();

        try {
            em.persist(obj);
            fail();
        } catch (EntityStateException ex) {
            assertTrue(ex.getMessage().contains("attached"));
        }
    }

    @Test
    public void testPersistSameIdTest() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // persist
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(10, "Hello");
        em.persist(obj);

        // check
        obj.assertEq(10, "Hello");
        Cursor cursor = getAll(NotGeneratedIdTestClass.class, db);
        assertEquals(1, cursor.getCount());
        cursor.close();

        try {
            em.persist(new NotGeneratedIdTestClass(10, "Hello2"));
            fail();
        } catch (DatabaseSqlException ex) {
            // expected
        }
    }

    @Test
    public void testPersistNullsTest() throws AndroidOrmException {
        // prepare
        init(GeneratedIdTestClass.class);

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(null, "Hello", null);
        em.persist(obj);

        // check
        obj.assertEq(1, "Hello", null);
        assertEq(getAll(GeneratedIdTestClass.class, db), obj);
    }

    @Test
    public void testPersistOnlyNullsTest() throws AndroidOrmException {
        // prepare
        init(GeneratedIdTestClass.class);

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(null, null, null);
        em.persist(obj);

        // check
        obj.assertEq(1, null, null);
        assertEq(getAll(GeneratedIdTestClass.class, db), obj);
    }
}
