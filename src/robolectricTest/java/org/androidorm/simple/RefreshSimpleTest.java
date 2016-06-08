package org.androidorm.simple;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.EntityStateException;
import org.androidorm.simple.testclasses.GeneratedIdTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RefreshSimpleTest extends TestBase {

    @Test
    public void testSimpleRefresh() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(0, "Foo", 12L);
        em.persist(obj);

        // check
        obj.assertEq(1, "Foo", 12L);
        assertEq(getAll(GeneratedIdTestClass.class, db), obj);

        // modify
        obj.field2 = "Bar";
        obj.field3 = 50L;
        obj.assertEq(1, "Bar", 50L);

        // refresh
        em.refresh(obj);

        // check
        obj.assertEq(1, "Foo", 12L);
        assertTrue(em.contains(obj));
    }

    @Test
    public void testRefreshRemoved() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(0, "Foo", 12L);
        em.persist(obj);

        // check
        obj.assertEq(1, "Foo", 12L);
        assertEq(getAll(GeneratedIdTestClass.class, db), obj);

        // remove
        em.remove(obj);

        // refresh
        try {
            em.refresh(obj);
            fail();
        } catch (EntityStateException ex) {
            assertTrue(ex.getMessage().contains("detached"));
        }
    }

    @Test
    public void testRefreshId() throws Exception {
        // prepare
        init(GeneratedIdTestClass.class);

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(0, "Foo", 12L);
        GeneratedIdTestClass obj2 = new GeneratedIdTestClass(0, "Foo2", 13L);
        em.persist(obj);
        em.persist(obj2);

        // check
        obj.assertEq(1, "Foo", 12L);
        obj2.assertEq(2, "Foo2", 13L);
        assertEq(getAll(GeneratedIdTestClass.class, db), obj, obj2);

        // modify
        obj.field1 = 2;
        obj.assertEq(2, "Foo", 12L);

        // refresh
        em.refresh(obj);

        // check
        obj.assertEq(1, "Foo", 12L);
        obj2.assertEq(2, "Foo2", 13L);
        assertTrue(em.contains(obj));
        assertTrue(em.contains(obj2));
    }

    @Test
    public void testRefreshDetached() throws AndroidOrmException {
        // prepare
        init(GeneratedIdTestClass.class);

        // persist
        GeneratedIdTestClass obj = new GeneratedIdTestClass(0, "Foo", 12L);

        // refresh
        try {
            em.refresh(obj);
            fail();
        } catch (EntityStateException ex) {
            assertTrue(ex.getMessage().contains("detached"));
        }
    }
}
