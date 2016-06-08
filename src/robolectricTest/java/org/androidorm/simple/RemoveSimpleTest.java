package org.androidorm.simple;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.EntityStateException;
import org.androidorm.simple.testclasses.NotGeneratedIdTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RemoveSimpleTest extends TestBase {

    @Test
    public void testBasicRemoveTest() throws Exception {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // persist
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Hello");
        NotGeneratedIdTestClass obj2 = new NotGeneratedIdTestClass(2, "Hello2");
        em.persist(obj);
        em.persist(obj2);

        // check
        obj.assertEq(1, "Hello");
        obj2.assertEq(2, "Hello2");
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj, obj2);

        // remove
        em.remove(obj);

        // check
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj2);
    }

    @Test
    public void testRemoveNoExisting() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // remove
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Hello");
        try {
            em.remove(obj);
            fail();
        } catch (EntityStateException ex) {
            assertTrue(ex.getMessage().contains("detached"));
        }
    }
}
