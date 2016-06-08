package org.androidorm.simple;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.simple.testclasses.NotGeneratedIdTestClass;
import org.androidorm.simple.testclasses.VariousTypesTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FindAllSimpleTest extends TestBase {

    @Test
    public void testSimpleFind() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj1 = new NotGeneratedIdTestClass(1, "Field1");
        obj1.insert(db);
        NotGeneratedIdTestClass obj2 = new NotGeneratedIdTestClass(2, "Field2");
        obj2.insert(db);
        NotGeneratedIdTestClass obj3 = new NotGeneratedIdTestClass(3, "Field3");
        obj3.insert(db);

        // find
        List<NotGeneratedIdTestClass> found = em.findAll(NotGeneratedIdTestClass.class);
        assertContains(found, obj1, obj2, obj3);
    }

    @Test
    public void testEmptyResult() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // don't create any objects
        // find
        List<NotGeneratedIdTestClass> found = em.findAll(NotGeneratedIdTestClass.class);
        assertTrue(found.isEmpty());
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
            em.findAll(PersistSimpleTest.class);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("is not registered"));
        }
    }

    @Test
    public void testEntityAlreadyInUnit() throws AndroidOrmException {
        // prepare
        init(VariousTypesTestClass.class);

        // persist
        VariousTypesTestClass obj1 = new VariousTypesTestClass(1, 2L, (short) 3, (byte) 4, "five", true, 7.0f, 8.0);
        em.persist(obj1);
        VariousTypesTestClass obj2 = new VariousTypesTestClass(10, 20L, (short) 30, (byte) 40, "fifty", false, 70.0f, 80.0);
        obj2.insert(db);

        // find
        List<VariousTypesTestClass> found = em.findAll(VariousTypesTestClass.class);

        // check
        assertEquals(2, found.size());
        assertSame(obj1, found.get(0));
        assertEquals(obj2, found.get(1));
        assertNotSame(obj2, found.get(1));
    }
}
