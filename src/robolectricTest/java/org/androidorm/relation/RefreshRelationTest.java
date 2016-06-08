package org.androidorm.relation;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.DatabaseStateException;
import org.androidorm.relation.testclasses.OneToOneTestClass;
import org.androidorm.testclasses.SimpleTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RefreshRelationTest extends TestBase {

    @Test
    public void testRefreshRelationObject() throws AndroidOrmException {
        // prepare
        init(OneToOneTestClass.class, SimpleTestClass.class);

        // persist
        SimpleTestClass obj = new SimpleTestClass(0, "HelloWorld");
        SimpleTestClass obj2 = new SimpleTestClass(0, "HelloWorld2");
        OneToOneTestClass parentObj = new OneToOneTestClass("id", "Hello2", obj);
        em.persist(obj);
        em.persist(obj2);
        em.persist(parentObj);

        // check
        obj.assertEq(1, "HelloWorld");
        obj2.assertEq(2, "HelloWorld2");
        parentObj.assertEq("id", "Hello2", obj);
        assertEq(getAll(SimpleTestClass.class, db), obj, obj2);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);

        // refresh
        parentObj.field3 = obj2;
        parentObj.assertEq("id", "Hello2", obj2);

        em.refresh(parentObj);

        // check
        obj.assertEq(1, "HelloWorld");
        obj2.assertEq(2, "HelloWorld2");
        parentObj.assertEq("id", "Hello2", obj);
        assertEq(getAll(SimpleTestClass.class, db), obj, obj2);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);
    }

    @Test
    public void testReloadDetachedRelationObject() throws AndroidOrmException {
        // prepare
        init(OneToOneTestClass.class, SimpleTestClass.class);

        // persist
        SimpleTestClass obj = new SimpleTestClass(0, "HelloWorld");
        SimpleTestClass obj2 = new SimpleTestClass(0, "HelloWorld2");
        OneToOneTestClass parentObj = new OneToOneTestClass("id", "Hello2", obj);
        em.persist(obj);
        em.persist(obj2);
        em.persist(parentObj);

        // check
        obj.assertEq(1, "HelloWorld");
        obj2.assertEq(2, "HelloWorld2");
        parentObj.assertEq("id", "Hello2", obj);
        assertEq(getAll(SimpleTestClass.class, db), obj, obj2);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);

        // refresh
        parentObj.field3 = obj2;
        parentObj.assertEq("id", "Hello2", obj2);
        em.detach(obj);

        em.refresh(parentObj);

        // check
        obj.assertEq(1, "HelloWorld");
        obj2.assertEq(2, "HelloWorld2");
        assertEq(getAll(SimpleTestClass.class, db), obj, obj2);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);
        parentObj.assertEq("id", "Hello2", em.find(SimpleTestClass.class, 1));
    }

    @Test
    public void testRefreshRelationObjectRemoved() throws AndroidOrmException {
        // prepare
        init(OneToOneTestClass.class, SimpleTestClass.class);

        // persist
        SimpleTestClass obj = new SimpleTestClass(0, "HelloWorld");
        SimpleTestClass obj2 = new SimpleTestClass(0, "HelloWorld2");
        OneToOneTestClass parentObj = new OneToOneTestClass("id", "Hello2", obj);
        em.persist(obj);
        em.persist(obj2);
        em.persist(parentObj);

        // check
        obj.assertEq(1, "HelloWorld");
        obj2.assertEq(2, "HelloWorld2");
        parentObj.assertEq("id", "Hello2", obj);
        assertEq(getAll(SimpleTestClass.class, db), obj, obj2);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);

        // refresh
        parentObj.field3 = obj2;
        parentObj.assertEq("id", "Hello2", obj2);
        em.remove(obj);

        try {
            em.refresh(parentObj);
            fail();
        } catch (DatabaseStateException ex) {
            assertTrue(ex.getMessage().contains("Can't load related instance"));
        }
    }
}
