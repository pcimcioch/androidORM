package org.androidorm.accessors;

import org.androidorm.annotations.AnnotationApplier;
import org.androidorm.annotations.EnumType;
import org.androidorm.annotations.Enumerated;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.ReflectionException;
import org.androidorm.mutator.FieldMutator;
import org.androidorm.types.SimpleType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class DbEnumFieldAccessorTest {

    @Test
    public void testGetTypeTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        DbEnumFieldAccessor accessor = new DbEnumFieldAccessor();

        // TestEnum1
        accessor.setMutator(new FieldMutator(TestEnumClass.class.getField("field1")));
        AnnotationApplier.applyField(accessor);
        assertFalse(SimpleType.isSimple(accessor.getRealType()));
        assertEquals(SimpleType.INTEGER, accessor.getType());
        assertEquals(TestEnum1.class, accessor.getRealType());

        DbEnumFieldAccessor accessor2 = new DbEnumFieldAccessor();

        // TestEnum2
        accessor2.setMutator(new FieldMutator(TestEnumClass.class.getField("field2")));
        AnnotationApplier.applyField(accessor2);
        assertFalse(SimpleType.isSimple(accessor2.getRealType()));
        assertEquals(SimpleType.STRING, accessor2.getType());
        assertEquals(TestEnum2.class, accessor2.getRealType());
    }

    @Test
    public void testExtractValue_simpleTest() throws AndroidOrmException, NoSuchFieldException, SecurityException {
        TestEnumClass obj = new TestEnumClass();
        obj.field1 = TestEnum1.VALUE2;
        obj.field2 = TestEnum2.ENUM2;

        // TestEnum1
        DbPropertyAccessor accessor1 = new DbEnumFieldAccessor();
        accessor1.setMutator(new FieldMutator(TestEnumClass.class.getField("field1")));
        AnnotationApplier.applyField(accessor1);
        assertEquals(1, accessor1.extractValue(obj));
        assertEquals(Integer.class, accessor1.extractValue(obj).getClass());

        // TestEnum2
        DbPropertyAccessor accessor2 = new DbEnumFieldAccessor();
        accessor2.setMutator(new FieldMutator(TestEnumClass.class.getField("field2")));
        AnnotationApplier.applyField(accessor2);
        assertEquals("ENUM2", accessor2.extractValue(obj));
        assertEquals(String.class, accessor2.extractValue(obj).getClass());
    }

    @Test
    public void testExtractValue_nullTest() throws AndroidOrmException, NoSuchFieldException, SecurityException {
        TestEnumClass obj = new TestEnumClass();
        obj.field1 = null;
        obj.field2 = null;

        // TestEnum1
        DbPropertyAccessor accessor1 = new DbEnumFieldAccessor();
        accessor1.setMutator(new FieldMutator(TestEnumClass.class.getField("field1")));
        assertNull(accessor1.extractValue(obj));

        // TestEnum2
        DbPropertyAccessor accessor2 = new DbEnumFieldAccessor();
        accessor2.setMutator(new FieldMutator(TestEnumClass.class.getField("field2")));
        assertNull(accessor2.extractValue(obj));
    }

    @Test
    public void testSetValue_simpleTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestEnumClass obj = new TestEnumClass();

        // TestEnum1
        DbPropertyAccessor accessor1 = new DbEnumFieldAccessor();
        accessor1.setMutator(new FieldMutator(TestEnumClass.class.getField("field1")));
        AnnotationApplier.applyField(accessor1);
        accessor1.setValue(obj, 2);
        assertEquals(TestEnum1.VALUE3, obj.field1);
        assertEquals(TestEnum1.class, obj.field1.getClass());

        // TestEnum2
        DbPropertyAccessor accessor2 = new DbEnumFieldAccessor();
        accessor2.setMutator(new FieldMutator(TestEnumClass.class.getField("field2")));
        AnnotationApplier.applyField(accessor2);
        accessor2.setValue(obj, "ENUM1");
        assertEquals(TestEnum2.ENUM1, obj.field2);
    }

    @Test
    public void testSetValue_nullTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestEnumClass obj = new TestEnumClass();
        obj.field1 = TestEnum1.VALUE1;
        obj.field2 = TestEnum2.ENUM1;

        // TestEnum1
        DbPropertyAccessor accessor1 = new DbEnumFieldAccessor();
        accessor1.setMutator(new FieldMutator(TestEnumClass.class.getField("field1")));
        accessor1.setValue(obj, null);
        assertNull(obj.field1);

        // TestEnum2
        DbPropertyAccessor accessor2 = new DbEnumFieldAccessor();
        accessor2.setMutator(new FieldMutator(TestEnumClass.class.getField("field2")));
        accessor2.setValue(obj, null);
        assertNull(obj.field2);
    }

    @Test
    public void testSetValue_realTypeTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestEnumClass obj = new TestEnumClass();
        obj.field1 = TestEnum1.VALUE2;
        obj.field2 = TestEnum2.ENUM3;

        // TestEnum1
        DbPropertyAccessor accessor1 = new DbEnumFieldAccessor();
        accessor1.setMutator(new FieldMutator(TestEnumClass.class.getField("field1")));
        try {
            accessor1.setValue(obj, TestEnum1.VALUE1);
            fail();
        } catch (ReflectionException ex) {
            // expected
        }

        // TestEnum2
        DbPropertyAccessor accessor2 = new DbEnumFieldAccessor();
        accessor2.setMutator(new FieldMutator(TestEnumClass.class.getField("field2")));
        try {
            accessor2.setValue(obj, TestEnum2.ENUM1);
            fail();
        } catch (ReflectionException ex) {
            // expected
        }
    }

    @Test
    public void testSetValue_outOfRangeTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestEnumClass obj = new TestEnumClass();
        obj.field1 = TestEnum1.VALUE2;
        obj.field2 = TestEnum2.ENUM3;

        // TestEnum1
        DbPropertyAccessor accessor1 = new DbEnumFieldAccessor();
        accessor1.setMutator(new FieldMutator(TestEnumClass.class.getField("field1")));
        try {
            accessor1.setValue(obj, 6);
            fail();
        } catch (AndroidOrmException ex) {
            // expected
        }

        // TestEnum2
        DbPropertyAccessor accessor2 = new DbEnumFieldAccessor();
        accessor2.setMutator(new FieldMutator(TestEnumClass.class.getField("field2")));
        try {
            accessor2.setValue(obj, "INCORRECT");
            fail();
        } catch (AndroidOrmException ex) {
            // expected
        }
    }

    private enum TestEnum1 {
        VALUE1,
        VALUE2,
        VALUE3
    }

    private enum TestEnum2 {
        ENUM1,
        ENUM2,
        ENUM3
    }

    private static class TestEnumClass {

        @Enumerated(EnumType.ORDINAL)
        public TestEnum1 field1;

        @Enumerated(EnumType.STRING)
        public TestEnum2 field2;
    }
}