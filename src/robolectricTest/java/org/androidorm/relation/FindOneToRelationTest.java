package org.androidorm.relation;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.relation.testclasses.BiDirectManyToOneTestClass;
import org.androidorm.relation.testclasses.BiDirectOneToManyTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FindOneToRelationTest extends TestBase {

    @Test
    public void testSimpleFind() throws AndroidOrmException {
        // prepare
        init(BiDirectOneToManyTestClass.class, BiDirectManyToOneTestClass.class);

        // create object
        BiDirectOneToManyTestClass obj1 = new BiDirectOneToManyTestClass(1);
        obj1.insert(db);
        BiDirectOneToManyTestClass obj2 = new BiDirectOneToManyTestClass(2);
        obj2.insert(db);
        BiDirectOneToManyTestClass obj3 = new BiDirectOneToManyTestClass(3);
        obj3.insert(db);

        BiDirectManyToOneTestClass par1 = new BiDirectManyToOneTestClass(1, obj1);
        par1.insert(db);
        BiDirectManyToOneTestClass par2 = new BiDirectManyToOneTestClass(2, obj1);
        par2.insert(db);
        BiDirectManyToOneTestClass par3 = new BiDirectManyToOneTestClass(3, obj2);
        par3.insert(db);

        // sanity check
        assertEq(getAll(BiDirectOneToManyTestClass.class, db), obj1, obj2, obj3);
        assertEq(getAll(BiDirectManyToOneTestClass.class, db), par1, par2, par3);

        // find
        BiDirectOneToManyTestClass dbObj3 = em.find(BiDirectOneToManyTestClass.class, 3);
        assertTrue(dbObj3.field2.isEmpty());
        BiDirectOneToManyTestClass dbObj2 = em.find(BiDirectOneToManyTestClass.class, 2);
        assertEquals(1, dbObj2.field2.size());

        BiDirectManyToOneTestClass dbPar2 = em.find(BiDirectManyToOneTestClass.class, 2);
        assertEquals(1, dbPar2.field2.field1);
        assertEquals(2, dbPar2.field2.field2.size());

        BiDirectManyToOneTestClass dbPar1 = em.find(BiDirectManyToOneTestClass.class, 1);
        assertEquals(1, dbPar1.field2.field1);
        assertSame(dbPar1.field2, dbPar2.field2);
    }
}
