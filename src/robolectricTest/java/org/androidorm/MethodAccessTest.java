package org.androidorm;

import org.androidorm.testclasses.WithTransientTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class MethodAccessTest extends TestBase {

    @Test
    public void testPersistTest() throws Exception {
        // prepare
        init(WithTransientTestClass.class);

        // persist
        WithTransientTestClass obj = new WithTransientTestClass(0, 20);
        em.persist(obj);

        // check
        obj.assertEq(1, 20);
        assertEq(getAll(WithTransientTestClass.class, db), obj);
    }

    @Test
    public void testFindTest() throws Exception {
        // prepare
        init(WithTransientTestClass.class);

        // persist
        WithTransientTestClass obj = new WithTransientTestClass(1, 20);
        obj.insert(db);

        // find
        WithTransientTestClass found = em.find(WithTransientTestClass.class, 1);

        // check
        assertEquals(obj, found);
    }
}
