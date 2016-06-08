package org.androidorm.simple;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.EntityStateException;
import org.androidorm.simple.testclasses.NotGeneratedIdTestClass;
import org.androidorm.simple.testclasses.VariousTypesTestClass;
import org.androidorm.testclasses.SimpleTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class UpdateSimpleTest extends TestBase {

    @Test
    public void testSimpleTypes() throws Exception {
        // prepare
        init(VariousTypesTestClass.class);

        // persist
        VariousTypesTestClass obj = new VariousTypesTestClass(1, 2L, (short) 3, (byte) 4, "five", true, 7.0f, 8.0);
        em.persist(obj);

        // check
        obj.assertEq(1, 2L, (short) 3, (byte) 4, "five", true, 7.0f, 8.0);
        assertEq(getAll(VariousTypesTestClass.class, db), obj);

        // update
        obj.setFields(10, 20L, (short) 30, (byte) 40, "fifty", false, 70.0f, 80.0);
        em.update(obj);

        // check
        assertEq(getAll(VariousTypesTestClass.class, db), obj);
    }

    @Test
    public void testUpdateNotExisting() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // update
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Hello");
        try {
            em.update(obj);
            fail();
        } catch (EntityStateException ex) {
            assertTrue(ex.getMessage().contains("detached"));
        }
    }

    @Test
    public void testUpdateIncorrectId() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // persist
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Hello");
        em.persist(obj);

        // check
        obj.assertEq(1, "Hello");
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);

        // update
        obj.setField1(0);
        try {
            em.update(obj);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("is incorrect id value"));
        }

        // check
        obj.setField1(1);
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);
    }

    @Test
    public void testUpdateToNull() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // persist
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Hello");
        em.persist(obj);

        // check
        obj.assertEq(1, "Hello");
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);

        // update
        obj.setField2(null);
        em.update(obj);

        // check
        obj.assertEq(1, null);
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);
    }

    @Test
    public void testUpdateAutoIncrementId() throws AndroidOrmException {
        // prepare
        init(SimpleTestClass.class);

        // persist
        SimpleTestClass obj = new SimpleTestClass(0, "Hello");
        em.persist(obj);

        // check
        obj.assertEq(1, "Hello");
        assertEq(getAll(SimpleTestClass.class, db), obj);

        // update
        obj.setField1(10);
        try {
            em.update(obj);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("autoincremented"));
        }

        // check
        obj.assertEq(10, "Hello");
        obj.setField1(1);
        assertEq(getAll(SimpleTestClass.class, db), obj);
    }

    @Test
    public void testUpdateIdAndPersist() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // persist
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Hello");
        em.persist(obj);

        // check
        obj.assertEq(1, "Hello");
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);

        // update
        obj.setField1(10);
        em.update(obj);

        // check
        obj.assertEq(10, "Hello");
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);

        // perist 1
        NotGeneratedIdTestClass obj2 = new NotGeneratedIdTestClass(1, "Hello2");
        em.persist(obj2);

        // check
        obj2.assertEq(1, "Hello2");
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj2, obj);
    }

    @Test
    public void testUpdateRemoved() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // persist
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Hello");
        em.persist(obj);

        // check
        obj.assertEq(1, "Hello");
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);

        // update
        obj.setField2("Hello2");
        em.update(obj);

        // check
        obj.assertEq(1, "Hello2");
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);

        // remove
        em.remove(obj);

        // try update removed
        obj.setField2("Hello3");
        try {
            em.update(obj);
            fail();
        } catch (EntityStateException ex) {
            assertTrue(ex.getMessage().contains("detached"));
        }
    }
}
