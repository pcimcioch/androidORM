package org.androidorm.accessors;

import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.ReflectionException;
import org.androidorm.mutator.FieldMutator;
import org.androidorm.types.SimpleType;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class DbDateFieldAccessorTest {

    @Test
    public void testGetTypeTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        DbDateFieldAccessor accessor = new DbDateFieldAccessor();

        // java.util.Data
        accessor.setMutator(new FieldMutator(TestDateClass.class.getField("field1")));
        assertFalse(SimpleType.isSimple(accessor.getRealType()));
        assertEquals(SimpleType.LONG, accessor.getType());
        assertEquals(Date.class, accessor.getRealType());

        DbDateFieldAccessor accessor2 = new DbDateFieldAccessor();

        // java.sql.Data
        accessor2.setMutator(new FieldMutator(TestDateClass.class.getField("field2")));
        assertFalse(SimpleType.isSimple(accessor2.getRealType()));
        assertEquals(SimpleType.LONG, accessor2.getType());
        assertEquals(java.sql.Date.class, accessor2.getRealType());
    }

    @Test
    public void testExtractValue_simpleTest() throws AndroidOrmException, NoSuchFieldException, SecurityException {
        TestDateClass obj = new TestDateClass();
        obj.field1 = new Date(1000L);
        obj.field2 = new java.sql.Date(2000L);

        // java.util.Data
        DbPropertyAccessor accessor1 = new DbDateFieldAccessor();
        accessor1.setMutator(new FieldMutator(TestDateClass.class.getField("field1")));
        assertEquals(1000L, accessor1.extractValue(obj));
        assertEquals(Long.class, accessor1.extractValue(obj).getClass());

        // java.sql.Data
        DbPropertyAccessor accessor2 = new DbDateFieldAccessor();
        accessor2.setMutator(new FieldMutator(TestDateClass.class.getField("field2")));
        assertEquals(2000L, accessor2.extractValue(obj));
        assertEquals(Long.class, accessor2.extractValue(obj).getClass());
    }

    @Test
    public void testExtractValue_nullTest() throws AndroidOrmException, NoSuchFieldException, SecurityException {
        TestDateClass obj = new TestDateClass();
        obj.field1 = null;
        obj.field2 = null;

        // java.util.Data
        DbPropertyAccessor accessor1 = new DbDateFieldAccessor();
        accessor1.setMutator(new FieldMutator(TestDateClass.class.getField("field1")));
        assertNull(accessor1.extractValue(obj));

        // java.sql.Data
        DbPropertyAccessor accessor2 = new DbDateFieldAccessor();
        accessor2.setMutator(new FieldMutator(TestDateClass.class.getField("field2")));
        assertNull(accessor2.extractValue(obj));
    }

    @Test
    public void testSetValue_simpleTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestDateClass obj = new TestDateClass();

        // java.util.Data
        DbPropertyAccessor accessor1 = new DbDateFieldAccessor();
        accessor1.setMutator(new FieldMutator(TestDateClass.class.getField("field1")));
        accessor1.setValue(obj, 3000L);
        assertEquals(3000L, obj.field1.getTime());
        assertEquals(Date.class, obj.field1.getClass());

        // java.sql.Data
        DbPropertyAccessor accessor2 = new DbDateFieldAccessor();
        accessor2.setMutator(new FieldMutator(TestDateClass.class.getField("field2")));
        accessor2.setValue(obj, 4000L);
        assertEquals(4000L, obj.field2.getTime());
        assertEquals(java.sql.Date.class, obj.field2.getClass());
    }

    @Test
    public void testSetValue_nullTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestDateClass obj = new TestDateClass();
        obj.field1 = new Date(1000L);
        obj.field2 = new java.sql.Date(2000L);

        // java.util.Data
        DbPropertyAccessor accessor1 = new DbDateFieldAccessor();
        accessor1.setMutator(new FieldMutator(TestDateClass.class.getField("field1")));
        accessor1.setValue(obj, null);
        assertNull(obj.field1);

        // java.sql.Data
        DbPropertyAccessor accessor2 = new DbDateFieldAccessor();
        accessor2.setMutator(new FieldMutator(TestDateClass.class.getField("field2")));
        accessor2.setValue(obj, null);
        assertNull(obj.field2);
    }

    @Test
    public void testSetValue_realTypeTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestDateClass obj = new TestDateClass();
        obj.field1 = new Date(1000L);
        obj.field2 = new java.sql.Date(2000L);

        // java.util.Data
        DbPropertyAccessor accessor1 = new DbDateFieldAccessor();
        accessor1.setMutator(new FieldMutator(TestDateClass.class.getField("field1")));
        try {
            accessor1.setValue(obj, new Date(5000L));
            fail();
        } catch (ReflectionException ex) {
            // expected
        }

        // java.sql.Data
        DbPropertyAccessor accessor2 = new DbDateFieldAccessor();
        accessor2.setMutator(new FieldMutator(TestDateClass.class.getField("field2")));
        try {
            accessor2.setValue(obj, new java.sql.Date(5000L));
            fail();
        } catch (ReflectionException ex) {
            // expected
        }
    }

    private static class TestDateClass {

        public Date field1;

        public java.sql.Date field2;
    }
}


