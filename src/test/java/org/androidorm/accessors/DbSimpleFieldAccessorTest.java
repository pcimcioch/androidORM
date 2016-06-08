package org.androidorm.accessors;

import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.AssertException;
import org.androidorm.exceptions.ReflectionException;
import org.androidorm.mutator.FieldMutator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class DbSimpleFieldAccessorTest {

    @Test
    public void testExtractValue_simpleTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestClass1 object = new TestClass1();

        // int
        DbPropertyAccessor a1 = new DbSimpleFieldAccessor();
        a1.setMutator(new FieldMutator(TestClass1.class.getField("intField")));
        object.intField = 12;
        assertEquals(object.intField, a1.extractValue(object));

        // Integer
        DbPropertyAccessor a2 = new DbSimpleFieldAccessor();
        a2.setMutator(new FieldMutator(TestClass1.class.getField("integerField")));
        object.integerField = 5234;
        assertEquals(object.integerField, a2.extractValue(object));

        // Long
        DbPropertyAccessor a3 = new DbSimpleFieldAccessor();
        a3.setMutator(new FieldMutator(TestClass1.class.getField("longWrapField")));
        object.longWrapField = 5000000000L;
        assertEquals(object.longWrapField, a3.extractValue(object));

        // long
        DbPropertyAccessor a4 = new DbSimpleFieldAccessor();
        a4.setMutator(new FieldMutator(TestClass1.class.getField("longField")));
        object.longField = 6000000000L;
        assertEquals(object.longField, a4.extractValue(object));

        // String
        DbPropertyAccessor a5 = new DbSimpleFieldAccessor();
        a5.setMutator(new FieldMutator(TestClass1.class.getField("stringField")));
        object.stringField = "Hello world!";
        assertEquals(object.stringField, a5.extractValue(object));

        // Byte
        DbPropertyAccessor a6 = new DbSimpleFieldAccessor();
        a6.setMutator(new FieldMutator(TestClass1.class.getField("byteWrapField")));
        object.byteWrapField = 120;
        assertEquals(object.byteWrapField, a6.extractValue(object));

        // byte
        DbPropertyAccessor a7 = new DbSimpleFieldAccessor();
        a7.setMutator(new FieldMutator(TestClass1.class.getField("byteField")));
        object.byteField = 125;
        assertEquals(object.byteField, a7.extractValue(object));

        // short
        DbPropertyAccessor a8 = new DbSimpleFieldAccessor();
        a8.setMutator(new FieldMutator(TestClass1.class.getField("shortField")));
        object.shortField = 1125;
        assertEquals(object.shortField, a8.extractValue(object));

        // Short
        DbPropertyAccessor a9 = new DbSimpleFieldAccessor();
        a9.setMutator(new FieldMutator(TestClass1.class.getField("shortWrapField")));
        object.shortWrapField = 11255;
        assertEquals(object.shortWrapField, a9.extractValue(object));
    }

    @Test
    public void testExtractValue_nullTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestClass1 object = new TestClass1();

        // Integer
        DbPropertyAccessor a2 = new DbSimpleFieldAccessor();
        a2.setMutator(new FieldMutator(TestClass1.class.getField("integerField")));
        object.integerField = null;
        assertNull(a2.extractValue(object));

        // Long
        DbPropertyAccessor a3 = new DbSimpleFieldAccessor();
        a3.setMutator(new FieldMutator(TestClass1.class.getField("longWrapField")));
        object.longWrapField = null;
        assertNull(a3.extractValue(object));

        // String
        DbPropertyAccessor a5 = new DbSimpleFieldAccessor();
        a5.setMutator(new FieldMutator(TestClass1.class.getField("stringField")));
        object.stringField = null;
        assertNull(a5.extractValue(object));

        // Byte
        DbPropertyAccessor a6 = new DbSimpleFieldAccessor();
        a6.setMutator(new FieldMutator(TestClass1.class.getField("byteWrapField")));
        object.byteWrapField = null;
        assertNull(a6.extractValue(object));

        // Short
        DbPropertyAccessor a9 = new DbSimpleFieldAccessor();
        a9.setMutator(new FieldMutator(TestClass1.class.getField("shortWrapField")));
        object.shortWrapField = null;
        assertNull(a9.extractValue(object));
    }

    @Test
    public void testExtractValue_accessTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestClass2 object = new TestClass2(1, 2, 3, 4);

        // public
        DbPropertyAccessor a1 = new DbSimpleFieldAccessor();
        a1.setMutator(new FieldMutator(TestClass2.class.getField("publicField")));
        assertEquals(1, a1.extractValue(object));

        // protected
        DbPropertyAccessor a2 = new DbSimpleFieldAccessor();
        a2.setMutator(new FieldMutator(TestClass2.class.getDeclaredField("protectedField")));
        assertEquals(2, a2.extractValue(object));

        // package
        DbPropertyAccessor a3 = new DbSimpleFieldAccessor();
        a3.setMutator(new FieldMutator(TestClass2.class.getDeclaredField("packageField")));
        assertEquals(3, a3.extractValue(object));

        // private
        DbPropertyAccessor a4 = new DbSimpleFieldAccessor();
        a4.setMutator(new FieldMutator(TestClass2.class.getDeclaredField("privateField")));
        assertEquals(4, a4.extractValue(object));
    }

    @Test
    public void testSetValue_simpleTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestClass1 object = new TestClass1();

        // int
        DbPropertyAccessor a1 = new DbSimpleFieldAccessor();
        a1.setMutator(new FieldMutator(TestClass1.class.getField("intField")));
        a1.setValue(object, 1);
        assertEquals(1, object.intField);

        // Integer
        DbPropertyAccessor a2 = new DbSimpleFieldAccessor();
        a2.setMutator(new FieldMutator(TestClass1.class.getField("integerField")));
        a2.setValue(object, 5234);
        assertEquals((Integer) 5234, object.integerField);

        // Long
        DbPropertyAccessor a3 = new DbSimpleFieldAccessor();
        a3.setMutator(new FieldMutator(TestClass1.class.getField("longWrapField")));
        a3.setValue(object, 5000000000L);
        assertEquals((Long) 5000000000L, object.longWrapField);

        // long
        DbPropertyAccessor a4 = new DbSimpleFieldAccessor();
        a4.setMutator(new FieldMutator(TestClass1.class.getField("longField")));
        a4.setValue(object, 6000000000L);
        assertEquals(6000000000L, object.longField);

        // String
        DbPropertyAccessor a5 = new DbSimpleFieldAccessor();
        a5.setMutator(new FieldMutator(TestClass1.class.getField("stringField")));
        a5.setValue(object, "Hello world!");
        assertEquals("Hello world!", object.stringField);

        // Byte
        DbPropertyAccessor a6 = new DbSimpleFieldAccessor();
        a6.setMutator(new FieldMutator(TestClass1.class.getField("byteWrapField")));
        byte b = 120;
        a6.setValue(object, b);
        assertEquals((Byte) b, object.byteWrapField);

        // byte
        DbPropertyAccessor a7 = new DbSimpleFieldAccessor();
        a7.setMutator(new FieldMutator(TestClass1.class.getField("byteField")));
        b = 125;
        a7.setValue(object, b);
        assertEquals(b, object.byteField);

        // short
        DbPropertyAccessor a8 = new DbSimpleFieldAccessor();
        a8.setMutator(new FieldMutator(TestClass1.class.getField("shortField")));
        short s = 1125;
        a8.setValue(object, s);
        assertEquals(s, object.shortField);

        // Short
        DbPropertyAccessor a9 = new DbSimpleFieldAccessor();
        a9.setMutator(new FieldMutator(TestClass1.class.getField("shortWrapField")));
        s = 11255;
        a9.setValue(object, s);
        assertEquals((Short) s, object.shortWrapField);
    }

    @Test
    public void testSetValue_nullTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestClass1 object = new TestClass1();

        // int
        DbPropertyAccessor a1 = new DbSimpleFieldAccessor();
        a1.setMutator(new FieldMutator(TestClass1.class.getField("intField")));
        try {
            a1.setValue(object, null);
            fail();
        } catch (ReflectionException ex) {
            // expected
        }

        // Integer
        DbPropertyAccessor a2 = new DbSimpleFieldAccessor();
        a2.setMutator(new FieldMutator(TestClass1.class.getField("integerField")));
        a2.setValue(object, null);
        assertNull(object.integerField);

        // Long
        DbPropertyAccessor a3 = new DbSimpleFieldAccessor();
        a3.setMutator(new FieldMutator(TestClass1.class.getField("longWrapField")));
        a3.setValue(object, null);
        assertNull(object.longWrapField);

        // long
        DbPropertyAccessor a4 = new DbSimpleFieldAccessor();
        a4.setMutator(new FieldMutator(TestClass1.class.getField("longField")));
        try {
            a4.setValue(object, null);
            fail();
        } catch (ReflectionException ex) {
            // expected
        }

        // String
        DbPropertyAccessor a5 = new DbSimpleFieldAccessor();
        a5.setMutator(new FieldMutator(TestClass1.class.getField("stringField")));
        a5.setValue(object, null);
        assertNull(object.stringField);

        // Byte
        DbPropertyAccessor a6 = new DbSimpleFieldAccessor();
        a6.setMutator(new FieldMutator(TestClass1.class.getField("byteWrapField")));
        a6.setValue(object, null);
        assertEquals(null, object.byteWrapField);

        // byte
        DbPropertyAccessor a7 = new DbSimpleFieldAccessor();
        a7.setMutator(new FieldMutator(TestClass1.class.getField("byteField")));
        try {
            a7.setValue(object, null);
            fail();
        } catch (ReflectionException ex) {
            // expected
        }

        // short
        DbPropertyAccessor a8 = new DbSimpleFieldAccessor();
        a8.setMutator(new FieldMutator(TestClass1.class.getField("shortField")));
        try {
            a8.setValue(object, null);
            fail();
        } catch (ReflectionException ex) {
            // expected
        }

        // Short
        DbPropertyAccessor a9 = new DbSimpleFieldAccessor();
        a9.setMutator(new FieldMutator(TestClass1.class.getField("shortWrapField")));
        a9.setValue(object, null);
        assertNull(object.shortWrapField);
    }

    @Test
    public void testSetValue_accessTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TestClass2 object = new TestClass2(1, 2, 3, 4);

        // public
        DbPropertyAccessor a1 = new DbSimpleFieldAccessor();
        a1.setMutator(new FieldMutator(TestClass2.class.getField("publicField")));
        a1.setValue(object, 10);
        assertEquals(10, object.publicField);

        // protected
        DbPropertyAccessor a2 = new DbSimpleFieldAccessor();
        a2.setMutator(new FieldMutator(TestClass2.class.getDeclaredField("protectedField")));
        a2.setValue(object, 20);
        assertEquals(20, object.protectedField);

        // public
        DbPropertyAccessor a3 = new DbSimpleFieldAccessor();
        a3.setMutator(new FieldMutator(TestClass2.class.getDeclaredField("packageField")));
        a3.setValue(object, 30);
        assertEquals(30, object.packageField);

        // public
        DbPropertyAccessor a4 = new DbSimpleFieldAccessor();
        a4.setMutator(new FieldMutator(TestClass2.class.getDeclaredField("privateField")));
        a4.setValue(object, 40);
        assertEquals(40, object.getPrivateField());
    }

    @Test(expected = AssertException.class)
    public void testTypeNotSupported() throws SecurityException, AndroidOrmException, NoSuchFieldException {
        DbPropertyAccessor a1 = new DbSimpleFieldAccessor();
        a1.setMutator(new FieldMutator(TestClass3.class.getField("notSimpleField")));
    }

    private static class TestClass1 {

        public int intField;

        public Integer integerField;

        public Long longWrapField;

        public long longField;

        public String stringField;

        public Byte byteWrapField;

        public byte byteField;

        public short shortField;

        public Short shortWrapField;
        // TODO boolean, date, byte[]
    }

    private static class TestClass2 {

        public int publicField;

        protected int protectedField;

        int packageField;

        private int privateField;

        public TestClass2(int publicField, int protectedField, int packageField, int privateField) {
            this.publicField = publicField;
            this.protectedField = protectedField;
            this.packageField = packageField;
            this.privateField = privateField;
        }

        public int getPrivateField() {
            return privateField;
        }
    }

    private static class TestClass3 {

        public TestClass1 notSimpleField;
    }
}






