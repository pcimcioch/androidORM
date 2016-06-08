package org.androidorm.transaction;

import org.androidorm.TestBase;
import org.androidorm.simple.testclasses.NotGeneratedIdTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class UpdateTransactionTest extends TestBase {

    @Test
    public void testCommitIdUpdate() throws Exception {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // persist
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Hello");
        em.persist(obj);

        // check
        obj.assertEq(1, "Hello");
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);

        // update
        obj.setField1(2);
        em.getTransaction().open();
        assertSame(obj, em.find(NotGeneratedIdTestClass.class, 1));
        assertNull(em.find(NotGeneratedIdTestClass.class, 2));
        em.update(obj);
        assertNull(em.find(NotGeneratedIdTestClass.class, 1));
        assertSame(obj, em.find(NotGeneratedIdTestClass.class, 2));
        em.getTransaction().commit();

        // check
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);
        assertNull(em.find(NotGeneratedIdTestClass.class, 1));
        assertSame(obj, em.find(NotGeneratedIdTestClass.class, 2));
    }

    @Test
    public void testRollbackIdUpdate() throws Exception {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // persist
        NotGeneratedIdTestClass obj = new NotGeneratedIdTestClass(1, "Hello");
        em.persist(obj);

        // check
        obj.assertEq(1, "Hello");
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);

        // update
        obj.setField1(2);
        em.getTransaction().open();
        assertSame(obj, em.find(NotGeneratedIdTestClass.class, 1));
        assertNull(em.find(NotGeneratedIdTestClass.class, 2));
        em.update(obj);
        assertNull(em.find(NotGeneratedIdTestClass.class, 1));
        assertSame(obj, em.find(NotGeneratedIdTestClass.class, 2));
        em.getTransaction().rollback();

        // check
        assertNull(em.find(NotGeneratedIdTestClass.class, 2));
        assertSame(obj, em.find(NotGeneratedIdTestClass.class, 1));

        obj.setField1(1);
        assertEq(getAll(NotGeneratedIdTestClass.class, db), obj);
    }
}
