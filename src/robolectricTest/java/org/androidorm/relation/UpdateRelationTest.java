package org.androidorm.relation;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.EntityStateException;
import org.androidorm.relation.testclasses.OneToOneTestClass;
import org.androidorm.testclasses.SimpleTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class UpdateRelationTest extends TestBase {

    @Test
    public void testUpdateRelationObjectToAnother() throws AndroidOrmException {
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

        // update
        parentObj.field3 = obj2;
        em.update(parentObj);

        // check
        obj.assertEq(1, "HelloWorld");
        obj2.assertEq(2, "HelloWorld2");
        parentObj.assertEq("id", "Hello2", obj2);
        assertEq(getAll(SimpleTestClass.class, db), obj, obj2);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);
    }

    @Test
    public void testUpdateRelationObjectToNull() throws AndroidOrmException {
        // prepare
        init(OneToOneTestClass.class, SimpleTestClass.class);

        // persist
        SimpleTestClass obj = new SimpleTestClass(0, "HelloWorld");
        OneToOneTestClass parentObj = new OneToOneTestClass("id", "Hello2", obj);
        em.persist(obj);
        em.persist(parentObj);

        // check
        obj.assertEq(1, "HelloWorld");
        parentObj.assertEq("id", "Hello2", obj);
        assertEq(getAll(SimpleTestClass.class, db), obj);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);

        // update
        parentObj.field3 = null;
        em.update(parentObj);

        // check
        obj.assertEq(1, "HelloWorld");
        parentObj.assertEq("id", "Hello2", null);
        assertEq(getAll(SimpleTestClass.class, db), obj);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);
    }

    @Test
    public void testUpdateRelationNullToObject() throws AndroidOrmException {
        // prepare
        init(OneToOneTestClass.class, SimpleTestClass.class);

        // persist
        SimpleTestClass obj = new SimpleTestClass(0, "HelloWorld");
        OneToOneTestClass parentObj = new OneToOneTestClass("id", "Hello2", null);
        em.persist(obj);
        em.persist(parentObj);

        // check
        obj.assertEq(1, "HelloWorld");
        parentObj.assertEq("id", "Hello2", null);
        assertEq(getAll(SimpleTestClass.class, db), obj);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);

        // update
        parentObj.field3 = obj;
        em.update(parentObj);

        // check
        obj.assertEq(1, "HelloWorld");
        parentObj.assertEq("id", "Hello2", obj);
        assertEq(getAll(SimpleTestClass.class, db), obj);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);
    }

    @Test
    public void testUpdateRelationObjectToNotPersisted() throws AndroidOrmException {
        // prepare
        init(OneToOneTestClass.class, SimpleTestClass.class);

        // persist
        SimpleTestClass obj = new SimpleTestClass(0, "HelloWorld");
        OneToOneTestClass parentObj = new OneToOneTestClass("id", "Hello2", obj);
        em.persist(obj);
        em.persist(parentObj);

        // check
        obj.assertEq(1, "HelloWorld");
        parentObj.assertEq("id", "Hello2", obj);
        assertEq(getAll(SimpleTestClass.class, db), obj);
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);

        // update
        SimpleTestClass notPersisted = new SimpleTestClass(100, "Not Persisted");
        parentObj.field3 = notPersisted;
        try {
            em.update(parentObj);
            fail();
        } catch (EntityStateException ex) {
            // ok
        }

        // check
        obj.assertEq(1, "HelloWorld");
        parentObj.assertEq("id", "Hello2", notPersisted);
        assertEq(getAll(SimpleTestClass.class, db), obj);
        parentObj.field3 = obj;
        assertEq(getAll(OneToOneTestClass.class, db), parentObj);
    }
}
