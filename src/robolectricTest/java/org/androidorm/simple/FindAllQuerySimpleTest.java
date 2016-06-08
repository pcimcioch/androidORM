package org.androidorm.simple;

import org.androidorm.TestBase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.DatabaseSqlException;
import org.androidorm.simple.testclasses.GeneratedIdTestClass;
import org.androidorm.simple.testclasses.NotGeneratedIdTestClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FindAllQuerySimpleTest extends TestBase {

    @Test
    public void testSimpleFind() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj1 = new NotGeneratedIdTestClass(1, "Field1");
        obj1.insert(db);
        NotGeneratedIdTestClass obj2 = new NotGeneratedIdTestClass(2, "Field2");
        obj2.insert(db);
        NotGeneratedIdTestClass obj3 = new NotGeneratedIdTestClass(3, "Field3");
        obj3.insert(db);
        NotGeneratedIdTestClass obj4 = new NotGeneratedIdTestClass(4, "Foo");
        obj4.insert(db);
        NotGeneratedIdTestClass obj5 = new NotGeneratedIdTestClass(5, "Bar");
        obj5.insert(db);

        // find
        assertQuery(NotGeneratedIdTestClass.class, "where field1 > 2", obj3, obj4, obj5);
        assertQuery(NotGeneratedIdTestClass.class, "where field1 between 2 and 5", obj2, obj3, obj4, obj5);
        assertQuery(NotGeneratedIdTestClass.class, "where field2 like \'Field%\'", obj1, obj2, obj3);
        assertQuery(NotGeneratedIdTestClass.class, "where field2 = \'Foo\'", obj4);
        assertQuery(NotGeneratedIdTestClass.class, "where field2 = \"Bar\"", obj5);
    }

    @Test
    public void testNoResults() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj1 = new NotGeneratedIdTestClass(1, "Field1");
        obj1.insert(db);
        NotGeneratedIdTestClass obj2 = new NotGeneratedIdTestClass(2, "Field2");
        obj2.insert(db);
        NotGeneratedIdTestClass obj3 = new NotGeneratedIdTestClass(3, "Field3");
        obj3.insert(db);
        NotGeneratedIdTestClass obj4 = new NotGeneratedIdTestClass(4, "Foo");
        obj4.insert(db);
        NotGeneratedIdTestClass obj5 = new NotGeneratedIdTestClass(5, "Bar");
        obj5.insert(db);

        // find
        assertQuery(NotGeneratedIdTestClass.class, "where field1 > 12");
        assertQuery(NotGeneratedIdTestClass.class, "where field2 = \'FooBar\'");
    }

    @Test
    public void testWrongQuery() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj1 = new NotGeneratedIdTestClass(1, "Field1");
        obj1.insert(db);

        // find
        try {
            em.findAll(NotGeneratedIdTestClass.class, "where field1 = FooBar");
            fail();
        } catch (DatabaseSqlException ex) {
            // expected
        }

        try {
            em.findAll(NotGeneratedIdTestClass.class, "where fieldErr > 6");
            fail();
        } catch (DatabaseSqlException ex) {
            // expected
        }
    }

    @Test
    public void testAsExpression() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj1 = new NotGeneratedIdTestClass(1, "Field1");
        obj1.insert(db);
        NotGeneratedIdTestClass obj2 = new NotGeneratedIdTestClass(2, "Field2");
        obj2.insert(db);
        NotGeneratedIdTestClass obj3 = new NotGeneratedIdTestClass(3, "Field3");
        obj3.insert(db);
        NotGeneratedIdTestClass obj4 = new NotGeneratedIdTestClass(4, "Foo");
        obj4.insert(db);
        NotGeneratedIdTestClass obj5 = new NotGeneratedIdTestClass(5, "Bar");
        obj5.insert(db);

        // find
        assertQuery(NotGeneratedIdTestClass.class, "as MyTable where MyTable.field1>=4", obj4, obj5);
    }

    @Test
    public void testJoin() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class, GeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj1 = new NotGeneratedIdTestClass(1, "Field1");
        obj1.insert(db);
        NotGeneratedIdTestClass obj2 = new NotGeneratedIdTestClass(2, "Field2");
        obj2.insert(db);
        NotGeneratedIdTestClass obj3 = new NotGeneratedIdTestClass(3, "Field3");
        obj3.insert(db);

        GeneratedIdTestClass parentObj1 = new GeneratedIdTestClass(1, "Parent1", 1L);
        parentObj1.insert(db);
        GeneratedIdTestClass parentObj2 = new GeneratedIdTestClass(2, "Parent2", 2L);
        parentObj2.insert(db);
        GeneratedIdTestClass parentObj3 = new GeneratedIdTestClass(3, "Parent3", 2L);
        parentObj3.insert(db);

        // find
        assertQuery(GeneratedIdTestClass.class,
                "join NOTGENERATEDIDTESTCLASS on NotGeneratedIdTestClass.field1=GeneratedIdTestClass.field3 where NotGeneratedIdTestClass.field2=\'Field2\'", parentObj2,
                parentObj3);
        assertQuery(GeneratedIdTestClass.class,
                " join NOTGENERATEDIDTESTCLASS on NotGeneratedIdTestClass.field1=GeneratedIdTestClass.field3 where NotGeneratedIdTestClass.field1=1", parentObj1);
        assertQuery(NotGeneratedIdTestClass.class,
                "left join GENERATEDIDTESTCLASS on NotGeneratedIdTestClass.field1=GeneratedIdTestClass.field3 where GeneratedIdTestClass.field1 is null", obj3);
    }

    @Test
    public void testJoinAs() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class, GeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj1 = new NotGeneratedIdTestClass(1, "Field1");
        obj1.insert(db);
        NotGeneratedIdTestClass obj2 = new NotGeneratedIdTestClass(2, "Field2");
        obj2.insert(db);
        NotGeneratedIdTestClass obj3 = new NotGeneratedIdTestClass(3, "Field3");
        obj3.insert(db);

        GeneratedIdTestClass parentObj1 = new GeneratedIdTestClass(1, "Parent1", 1L);
        parentObj1.insert(db);
        GeneratedIdTestClass parentObj2 = new GeneratedIdTestClass(2, "Parent2", 2L);
        parentObj2.insert(db);
        GeneratedIdTestClass parentObj3 = new GeneratedIdTestClass(3, "Parent3", 2L);
        parentObj3.insert(db);

        // find
        assertQuery(GeneratedIdTestClass.class, " as T4 join NotGeneratedIdTestClass as T2 on T2.field1=T4.field3 where T2.field2=\'Field2\'", parentObj2, parentObj3);
        assertQuery(GeneratedIdTestClass.class, "as T4 join NotGeneratedIdTestClass as T2 on T2.field1=T4.field3 where T2.field1=1", parentObj1);
        assertQuery(GeneratedIdTestClass.class, "AS T4 join NotGeneratedIdTestClass as T2 on T2.field1=T4.field3 where T2.field1=1", parentObj1);
        assertQuery(GeneratedIdTestClass.class, "As T4 join NotGeneratedIdTestClass as T2 on T2.field1=T4.field3 where T2.field1=1", parentObj1);
    }

    @Test
    public void testNestedQuery() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class, GeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj1 = new NotGeneratedIdTestClass(1, "NotAField");
        obj1.insert(db);
        NotGeneratedIdTestClass obj2 = new NotGeneratedIdTestClass(2, "Field2");
        obj2.insert(db);
        NotGeneratedIdTestClass obj3 = new NotGeneratedIdTestClass(3, "Field3");
        obj3.insert(db);

        GeneratedIdTestClass parentObj1 = new GeneratedIdTestClass(1, "Parent1", 1L);
        parentObj1.insert(db);
        GeneratedIdTestClass parentObj2 = new GeneratedIdTestClass(2, "Parent2", 2L);
        parentObj2.insert(db);
        GeneratedIdTestClass parentObj3 = new GeneratedIdTestClass(3, "Parent3", 2L);
        parentObj3.insert(db);

        // find
        assertQuery(GeneratedIdTestClass.class,
                "as T4 join NotGeneratedIdTestClass as T2 on T2.field1=T4.field3 where T2.field2 in " + "(select field2 from NotGeneratedIdTestClass where field2 like \'Field%\')",
                parentObj2, parentObj3);
    }

    @Test
    public void testArguments() throws AndroidOrmException {
        // prepare
        init(NotGeneratedIdTestClass.class);

        // create object
        NotGeneratedIdTestClass obj1 = new NotGeneratedIdTestClass(1, "Field1");
        obj1.insert(db);
        NotGeneratedIdTestClass obj2 = new NotGeneratedIdTestClass(2, "Field2");
        obj2.insert(db);
        NotGeneratedIdTestClass obj3 = new NotGeneratedIdTestClass(3, "Field3");
        obj3.insert(db);
        NotGeneratedIdTestClass obj4 = new NotGeneratedIdTestClass(4, "Foo");
        obj4.insert(db);
        NotGeneratedIdTestClass obj5 = new NotGeneratedIdTestClass(5, "Bar");
        obj5.insert(db);

        // find
        assertQueryArgs(NotGeneratedIdTestClass.class, "where field1 > ?", new Object[]{2}, obj3, obj4, obj5);
        assertQueryArgs(NotGeneratedIdTestClass.class, "where field1 between ? and ?", new Object[]{2, 5}, obj2, obj3, obj4, obj5);
        assertQueryArgs(NotGeneratedIdTestClass.class, "where field2 like ?", new Object[]{"Field%"}, obj1, obj2, obj3);
        assertQueryArgs(NotGeneratedIdTestClass.class, "where field2 = ?", new Object[]{"Foo"}, obj4);
        assertQueryArgs(NotGeneratedIdTestClass.class, "where field2 = ?", new Object[]{"FooBar"});
    }
}
