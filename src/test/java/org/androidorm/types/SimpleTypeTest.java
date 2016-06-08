package org.androidorm.types;

import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SimpleTypeTest {

    @Test
    public void testIsSimpleTest() throws NoSuchFieldException, SecurityException {
        for (Field field : TestSimpleClass.class.getFields()) {
            assertTrue("Type " + field.getType() + " should be recognized as simple", SimpleType.isSimple(field.getType()));
        }

        for (Field field : TestNotSimpleClass.class.getFields()) {
            assertFalse("Type " + field.getType() + " should not be recognized as simple", SimpleType.isSimple(field.getType()));
        }
    }

    @Test
    public void testConvertTest() {
        assertEquals(DbType.INTEGER, SimpleType.convert(Integer.class).getDbType());
        assertEquals(DbType.INTEGER, SimpleType.convert(int.class).getDbType());
        assertEquals(DbType.INTEGER, SimpleType.convert(Long.class).getDbType());
        assertEquals(DbType.INTEGER, SimpleType.convert(long.class).getDbType());
        assertEquals(DbType.INTEGER, SimpleType.convert(Short.class).getDbType());
        assertEquals(DbType.INTEGER, SimpleType.convert(short.class).getDbType());
        assertEquals(DbType.INTEGER, SimpleType.convert(Byte.class).getDbType());
        assertEquals(DbType.INTEGER, SimpleType.convert(byte.class).getDbType());

        assertEquals(DbType.INTEGER, SimpleType.convert(Boolean.class).getDbType());
        assertEquals(DbType.INTEGER, SimpleType.convert(boolean.class).getDbType());
        assertEquals(DbType.TEXT, SimpleType.convert(String.class).getDbType());
        assertEquals(DbType.TEXT, SimpleType.convert(Character.class).getDbType());
        assertEquals(DbType.TEXT, SimpleType.convert(char.class).getDbType());
        assertEquals(DbType.TEXT, SimpleType.convert(Byte[].class).getDbType());
        assertEquals(DbType.TEXT, SimpleType.convert(byte[].class).getDbType());

        assertEquals(DbType.REAL, SimpleType.convert(Double.class).getDbType());
        assertEquals(DbType.REAL, SimpleType.convert(double.class).getDbType());
        assertEquals(DbType.REAL, SimpleType.convert(float.class).getDbType());
        assertEquals(DbType.REAL, SimpleType.convert(Float.class).getDbType());
    }

    @Test
    public void testCastFromLong_correctTest() {
        long longValue = 44L;

        assertEquals(Long.class, SimpleType.LONG.castFromLong(longValue).getClass());
        assertEquals(Short.class, SimpleType.SHORT.castFromLong(longValue).getClass());
        assertEquals(Integer.class, SimpleType.INTEGER.castFromLong(longValue).getClass());
        assertEquals(Byte.class, SimpleType.BYTE.castFromLong(longValue).getClass());
    }

    @Test
    public void testCastFromLong_outOfRangeTest() {
        long longValue = 6000000000L;

        try {
            SimpleType.SHORT.castFromLong(longValue);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("range"));
        }

        try {
            SimpleType.INTEGER.castFromLong(longValue);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("range"));
        }

        try {
            SimpleType.BYTE.castFromLong(longValue);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("range"));
        }
    }

    @Test
    public void testCastFromLong_NotNumberTest() {
        long longValue = 10L;

        try {
            SimpleType.STRING.castFromLong(longValue);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("casted"));
        }

        try {
            SimpleType.DOUBLE.castFromLong(longValue);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("casted"));
        }

        try {
            SimpleType.FLOAT.castFromLong(longValue);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("casted"));
        }

        try {
            SimpleType.BOOLEAN.castFromLong(longValue);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("casted"));
        }
    }

    private static class TestSimpleClass {

        public int field1;

        public Integer field2;

        public long field3;

        public Long field4;

        public Short field5;

        public short field6;

        public Byte field7;

        public byte field8;

        public byte[] field9;

        public Byte[] field10;

        public Boolean field11;

        public boolean field12;

        public String field13;

        public float field16;

        public Float field17;

        public Double field18;

        public double field19;
        // TODO character support, enum support
    }

    private static class TestNotSimpleClass {

        public BigDecimal field1;

        public TestSimpleClass field2;

        public List<Integer> field3;

        public Serializable field4;

        public Date field14;

        public java.sql.Date field15;
    }
}
