package org.androidorm.transaction;

import org.androidorm.TestBase;
import org.androidorm.simple.testclasses.GeneratedIdTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RemoveTransactionTest extends TestBase {

    @Test
    public void testCommitRemoveTest() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(0, "Hello", 1L);
        GeneratedIdTestClass obj2 = new GeneratedIdTestClass(0, "Hello2", 2L);
        em.persist(obj);
        em.persist(obj2);

        // check
        obj.assertEq(1, "Hello", 1L);
        obj2.assertEq(2, "Hello2", 2L);
        assertEq(getAll(GeneratedIdTestClass.class, db), obj, obj2);
        assertTrue(em.contains(obj));
        assertTrue(em.contains(obj2));
        assertEquals(2, em.findAll(GeneratedIdTestClass.class).size());

        // remove
        em.getTransaction().open();
        assertEquals(2, em.findAll(GeneratedIdTestClass.class).size());
        em.remove(obj);
        assertEquals(1, em.findAll(GeneratedIdTestClass.class).size());
        em.remove(obj2);
        assertTrue(em.findAll(GeneratedIdTestClass.class).isEmpty());
        assertFalse(em.contains(obj));
        assertFalse(em.contains(obj2));
        em.getTransaction().commit();

        // check
        assertEq(getAll(GeneratedIdTestClass.class, db));
        assertFalse(em.contains(obj));
        assertFalse(em.contains(obj2));
    }

    @Test
    public void testRollbackRemoveTest() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(0, "Hello", 1L);
        GeneratedIdTestClass obj2 = new GeneratedIdTestClass(0, "Hello2", 2L);
        em.persist(obj);
        em.persist(obj2);

        // check
        obj.assertEq(1, "Hello", 1L);
        obj2.assertEq(2, "Hello2", 2L);
        assertEq(getAll(GeneratedIdTestClass.class, db), obj, obj2);
        assertTrue(em.contains(obj));
        assertTrue(em.contains(obj2));
        assertEquals(2, em.findAll(GeneratedIdTestClass.class).size());

        // remove
        em.getTransaction().open();
        assertEquals(2, em.findAll(GeneratedIdTestClass.class).size());
        em.remove(obj);
        assertEquals(1, em.findAll(GeneratedIdTestClass.class).size());
        em.remove(obj2);
        assertTrue(em.findAll(GeneratedIdTestClass.class).isEmpty());
        assertFalse(em.contains(obj));
        assertFalse(em.contains(obj2));
        em.getTransaction().rollback();

        // check
        assertEq(getAll(GeneratedIdTestClass.class, db), obj, obj2);
        assertTrue(em.contains(obj));
        assertTrue(em.contains(obj2));
        assertEquals(2, em.findAll(GeneratedIdTestClass.class).size());
    }
}
