package org.androidorm.relation;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.relation.testclasses.ManyToOneTestClass;
import org.androidorm.relation.testclasses.OneToOneTestClass;
import org.androidorm.testclasses.SimpleTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FindAllQueryRelationTest extends TestBase {

    @Test
    public void testSimpleFind() throws AndroidOrmException {
        // prepare
        init(SimpleTestClass.class, OneToOneTestClass.class, ManyToOneTestClass.class);

        // create object
        SimpleTestClass obj1 = new SimpleTestClass(1, "Field1");
        obj1.insert(db);
        SimpleTestClass obj2 = new SimpleTestClass(2, "Field2");
        obj2.insert(db);
        SimpleTestClass obj3 = new SimpleTestClass(3, "Field3");
        obj3.insert(db);

        OneToOneTestClass par1 = new OneToOneTestClass("1", "Hello1", obj1);
        par1.insert(db);
        OneToOneTestClass par2 = new OneToOneTestClass("2", "Hello2", obj1);
        par2.insert(db);
        OneToOneTestClass par3 = new OneToOneTestClass("3", "Hello3", obj2);
        par3.insert(db);

        ManyToOneTestClass grand1 = new ManyToOneTestClass(1, par1);
        grand1.insert(db);
        ManyToOneTestClass grand2 = new ManyToOneTestClass(2, par3);
        grand2.insert(db);

        // sanity check
        assertEq(getAll(SimpleTestClass.class, db), obj1, obj2, obj3);
        assertEq(getAll(OneToOneTestClass.class, db), par1, par2, par3);
        assertEq(getAll(ManyToOneTestClass.class, db), grand1, grand2);

        // find
        assertQuery(ManyToOneTestClass.class, "where field1 = 1", grand1);
    }
}
