package org.androidorm.simple;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.simple.testclasses.GeneratedIdTestClass;
import org.androidorm.simple.testclasses.NotGeneratedIdTestClass;
import org.androidorm.simple.testclasses.VariousTypesTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FindSimpleTest extends TestBase {

    @Test
    public void testFindAfterPersisted() throws Exception {
        // prepare
        init(VariousTypesTestClass.class);

        // persist
        VariousTypesTestClass obj = new VariousTypesTestClass(1, 2L, (short) 3, (byte) 4, "five", true, 7.0f, 8.0);
        em.persist(obj);

        // check
        obj.assertEq(1, 2L, (short) 3, (byte) 4, "five", true, 7.0f, 8.0);
        assertEq(getAll(VariousTypesTestClass.class, db), obj);

        // find
        VariousTypesTestClass obj2 = em.find(VariousTypesTestClass.class, 1);

        // check
        assertSame(obj, obj2);
    }

    @Test
    public void testSimpleFind() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Field2");
        obj.insert(db);

        // find
        NotGeneratedIdTestClass found = em.find(NotGeneratedIdTestClass.class, 1);

        // check
        assertEquals(obj, found);
    }

    @Test
    public void testPrivateConstructor() throws AndroidOrmException {
        // prepare
        init(GeneratedIdTestClass.class);

        // create object
        GeneratedIdTestClass obj = new GeneratedIdTestClass(1, "Hello", 2L);
        obj.insert(db);

        // find
        GeneratedIdTestClass found = em.find(GeneratedIdTestClass.class, 1);

        // check
        assertEquals(obj, found);
    }

    @Test
    public void testEntityIdNotFound() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Field2");
        obj.insert(db);

        // find
        NotGeneratedIdTestClass found = em.find(NotGeneratedIdTestClass.class, 2);
        assertNull(found);
    }

    @Test
    public void testEntityClassNotFound() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Field2");
        obj.insert(db);

        // find
        try {
            em.find(PersistSimpleTest.class, 1);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("is not registered"));
        }
    }

    @Test
    public void testAfterRemove() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Hello");
        em.persist(obj);

        // find
        NotGeneratedIdTestClass found = em.find(NotGeneratedIdTestClass.class, 1);
        assertEquals(obj, found);

        // remove
        em.remove(obj);

        // check
        assertNull(em.find(NotGeneratedIdTestClass.class, 1));
    }

    @Test
    public void testAfterUpdate() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Hello");
        em.persist(obj);

        // find
        NotGeneratedIdTestClass found = em.find(NotGeneratedIdTestClass.class, 1);
        assertEquals(obj, found);

        // update
        obj.setField1(2);
        em.update(obj);

        // find
        found = em.find(NotGeneratedIdTestClass.class, 2);
        assertNull(em.find(NotGeneratedIdTestClass.class, 1));
        assertSame(obj, found);
    }

    @Test
    public void testNullValues() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, null);
        obj.insert(db);

        // find
        NotGeneratedIdTestClass found = em.find(NotGeneratedIdTestClass.class, 1);
        assertEquals(obj, found);
    }
}
