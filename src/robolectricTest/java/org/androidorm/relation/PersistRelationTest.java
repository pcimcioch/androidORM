package org.androidorm.relation;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.EntityStateException;
import org.androidorm.relation.testclasses.ManyToOneTestClass;
import org.androidorm.relation.testclasses.OneToOneTestClass;
import org.androidorm.testclasses.SimpleTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class PersistRelationTest extends TestBase {

    @Test
    public void testCorrectSimplePersist() throws AndroidOrmException {
        // prepare
        init(OneToOneTestClass.class, SimpleTestClass.class);

        // persist
        SimpleTestClass obj = new SimpleTestClass(0, "Hello");
        OneToOneTestClass parentObj = new OneToOneTestClass("id", "Hello2", obj);
        em.persist(obj);
        em.persist(parentObj);

        // check
        obj.assertEq(1, "Hello");
        parentObj.assertEq("id", "Hello2", obj);
        assertEq(getAll(SimpleTestClass.class, db), obj);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);
    }

    @Test
    public void testNullRelationPersist() throws AndroidOrmException {
        // prepare
        init(SimpleTestClass.class, OneToOneTestClass.class);

        // persist
        OneToOneTestClass parentObj = new OneToOneTestClass("id", "Hello2", null);
        em.persist(parentObj);

        // check
        parentObj.assertEq("id", "Hello2", null);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);
    }

    @Test
    public void testChildNotPersistedTest() throws AndroidOrmException {
        // prepare
        init(SimpleTestClass.class, OneToOneTestClass.class);

        // persist
        SimpleTestClass obj = new SimpleTestClass(0, "Hello");
        OneToOneTestClass parentObj = new OneToOneTestClass("id", "Hello2", obj);

        try {
            em.persist(parentObj);
            fail();
        } catch (EntityStateException ex) {
            // ok
        }

        // check
        obj.assertEq(0, "Hello");
        parentObj.assertEq("id", "Hello2", obj);
        assertEq(getAll(SimpleTestClass.class, db));
        assertEq(getAll(OneToOneTestClass.class, db));
    }

    @Test
    public void testThreeLevelTest() throws AndroidOrmException {
        // prepare
        init(OneToOneTestClass.class, ManyToOneTestClass.class, SimpleTestClass.class);

        // persist
        SimpleTestClass obj = new SimpleTestClass(0, "Hello");
        OneToOneTestClass parentObj = new OneToOneTestClass("id", "Hello2", obj);
        ManyToOneTestClass grandParentObj = new ManyToOneTestClass(0, parentObj);
        em.persist(obj);
        em.persist(parentObj);
        em.persist(grandParentObj);

        // check
        obj.assertEq(1, "Hello");
        parentObj.assertEq("id", "Hello2", obj);
        grandParentObj.assertEq(1, parentObj);
        assertEq(getAll(SimpleTestClass.class, db), obj);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);
        assertEq(getAll(ManyToOneTestClass.class, db), grandParentObj);
    }

    @Test
    public void testChildFindNotPersisted() throws AndroidOrmException {
        // prepare
        init(OneToOneTestClass.class, SimpleTestClass.class);

        // insert
        SimpleTestClass obj = new SimpleTestClass(1, "Hello");
        obj.insert(db);
        assertFalse(em.contains(obj));

        // find
        SimpleTestClass dbObj = em.find(SimpleTestClass.class, 1);
        assertNotNull(dbObj);

        // persist
        OneToOneTestClass parentObj = new OneToOneTestClass("id", "Hello2", dbObj);
        em.persist(parentObj);

        // check
        obj.assertEq(1, "Hello");
        parentObj.assertEq("id", "Hello2", dbObj);
        assertEq(getAll(SimpleTestClass.class, db), dbObj);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);
    }
}
