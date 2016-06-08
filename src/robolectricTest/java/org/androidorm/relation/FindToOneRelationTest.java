package org.androidorm.relation;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.DatabaseStateException;
import org.androidorm.relation.testclasses.ManyToOneTestClass;
import org.androidorm.relation.testclasses.OneToOneTestClass;
import org.androidorm.testclasses.SimpleTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FindToOneRelationTest extends TestBase {

    @Test
    public void testFindNew() throws Exception {
        // prepare
        init(SimpleTestClass.class, OneToOneTestClass.class);

        // insert
        SimpleTestClass obj1 = new SimpleTestClass(1, "Hello1");
        obj1.insert(db);
        SimpleTestClass obj2 = new SimpleTestClass(2, "Hello2");
        obj2.insert(db);
        SimpleTestClass obj3 = new SimpleTestClass(3, "Hello3");
        obj3.insert(db);

        OneToOneTestClass par1 = new OneToOneTestClass("1", "H1", obj1);
        par1.insert(db);
        OneToOneTestClass par2 = new OneToOneTestClass("2", "H2", obj2);
        par2.insert(db);
        OneToOneTestClass par3 = new OneToOneTestClass("3", "H3", null);
        par3.insert(db);

        // sanity check
        assertEq(getAll(SimpleTestClass.class, db), obj1, obj2, obj3);
        assertEq(getAll(OneToOneTestClass.class, db), par1, par2, par3);

        // find
        OneToOneTestClass dbPar1 = em.find(OneToOneTestClass.class, "1");
        assertNotNull(em.find(SimpleTestClass.class, 2));
        OneToOneTestClass dbPar2 = em.find(OneToOneTestClass.class, "2");
        OneToOneTestClass dbPar3 = em.find(OneToOneTestClass.class, "3");

        // check
        assertTrue(em.contains(dbPar1));
        assertTrue(em.contains(dbPar1.field3));
        assertEquals(par1, dbPar1);
        assertEquals(obj1.field1, dbPar1.field3.field1);
        assertEquals(par2, dbPar2);
        assertEquals(obj2.field1, dbPar2.field3.field1);
        assertEquals(par3, dbPar3);
        assertNull(dbPar3.field3);
    }

    @Test
    public void testFindPersisted() throws AndroidOrmException {
        // prepare
        init(SimpleTestClass.class, OneToOneTestClass.class);

        // insert
        SimpleTestClass obj1 = new SimpleTestClass(0, "Hello1");
        em.persist(obj1);
        OneToOneTestClass par1 = new OneToOneTestClass("1", "H1", obj1);
        par1.insert(db);

        // sanity check
        assertEq(getAll(SimpleTestClass.class, db), obj1);
        assertEq(getAll(OneToOneTestClass.class, db), par1);

        // find
        OneToOneTestClass dbPar1 = em.find(OneToOneTestClass.class, "1");

        // check
        assertTrue(em.contains(dbPar1));
        assertTrue(em.contains(dbPar1.field3));
        assertEquals(par1, dbPar1);
        assertSame(obj1, dbPar1.field3);
        assertEquals(obj1.field1, dbPar1.field3.field1);
    }

    @Test
    public void testFindRelationNotInDatabase() throws AndroidOrmException {
        // prepare
        init(SimpleTestClass.class, OneToOneTestClass.class);

        // insert
        SimpleTestClass obj1 = new SimpleTestClass(19, "Hello1");
        OneToOneTestClass par1 = new OneToOneTestClass("1", "H1", obj1);
        par1.insert(db);

        // sanity check
        assertEq(getAll(SimpleTestClass.class, db));
        assertEq(getAll(OneToOneTestClass.class, db), par1);

        // find
        try {
            em.find(OneToOneTestClass.class, "1");
            fail();
        } catch (DatabaseStateException ex) {
            assertTrue(ex.getMessage().contains("Can't load related instance"));
        }
    }

    @Test
    public void testMultipleLevelsRelations() throws AndroidOrmException {
        // prepare
        init(SimpleTestClass.class, OneToOneTestClass.class, ManyToOneTestClass.class);

        // insert
        SimpleTestClass obj1 = new SimpleTestClass(1, "Hello1");
        obj1.insert(db);
        OneToOneTestClass obj2 = new OneToOneTestClass("1", "Hello2", obj1);
        obj2.insert(db);
        ManyToOneTestClass obj3 = new ManyToOneTestClass(1, obj2);
        obj3.insert(db);

        // sanity check
        assertEq(getAll(SimpleTestClass.class, db), obj1);
        assertEq(getAll(OneToOneTestClass.class, db), obj2);
        assertEq(getAll(ManyToOneTestClass.class, db), obj3);

        // find
        ManyToOneTestClass db1 = em.find(ManyToOneTestClass.class, 1);

        assertEquals(obj3, db1);
        assertEquals(obj2, db1.field2);
        assertEquals(obj1, db1.field2.field3);
    }
}
