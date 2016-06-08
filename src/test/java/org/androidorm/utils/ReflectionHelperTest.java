package org.androidorm.utils;

import org.junit.Test;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ReflectionHelperTest {

    @Test
    public void testGetAllFieldsTest() {
        // interface's fields should not be considered
        assertEquals(13, ReflectionHelper.getAllFields(TestClass.class).size());
    }

    @Test
    public void testIsAccesibleTest() throws NoSuchFieldException, SecurityException {
        assertTrue(ReflectionHelper.isAccesible(TestClass3.class.getDeclaredField("field1")));
        assertTrue(ReflectionHelper.isAccesible(TestClass3.class.getDeclaredField("field2")));
        assertTrue(ReflectionHelper.isAccesible(TestClass3.class.getDeclaredField("field3")));
        assertTrue(ReflectionHelper.isAccesible(TestClass3.class.getDeclaredField("field4")));
        assertFalse(ReflectionHelper.isAccesible(TestClass3.class.getDeclaredField("field5")));
        assertFalse(ReflectionHelper.isAccesible(TestClass3.class.getDeclaredField("field6")));
        assertFalse(ReflectionHelper.isAccesible(TestClass3.class.getDeclaredField("field7")));
        assertFalse(ReflectionHelper.isAccesible(TestClass3.class.getDeclaredField("field8")));
    }

    @Test
    public void testIsEmptyTest() {
        // only for simple types
        assertTrue(ReflectionHelper.isEmpty(null));
        assertTrue(ReflectionHelper.isEmpty(""));
        assertFalse(ReflectionHelper.isEmpty("text"));
        assertFalse(ReflectionHelper.isEmpty("Hello world"));
        assertTrue(ReflectionHelper.isEmpty((byte) 0));
        assertFalse(ReflectionHelper.isEmpty((byte) 12));
        assertFalse(ReflectionHelper.isEmpty(125L));
        assertTrue(ReflectionHelper.isEmpty(0L));
    }

    @Test
    public void testCopyTwoDimArray_null() {
        assertNull(ReflectionHelper.copyTwoDimArray(null));
    }

    @Test
    public void testCopyTwoDimArray_empty() {
        assertArraysEquals(new String[][]{}, ReflectionHelper.copyTwoDimArray(new String[][]{}));
        assertArraysEquals(new String[][]{{}, {}}, ReflectionHelper.copyTwoDimArray(new String[][]{{}, {}}));
        assertArraysEquals(new String[][]{{}, {"1", "6"}}, ReflectionHelper.copyTwoDimArray(new String[][]{{}, {"1", "6"}}));
    }

    @Test
    public void testCopyTwoDimArray_assertTheSame() {
        assertArraysEquals(new String[][]{{"1"}, {"2", "3"}}, ReflectionHelper.copyTwoDimArray(new String[][]{{"1"}, {"2", "3"}}));
        assertArraysEquals(new String[][]{{"2", "3"}}, ReflectionHelper.copyTwoDimArray(new String[][]{{"2", "3"}}));
    }

    @Test
    public void testCopyTwoDimArray_isNotTheSameArray() {
        String[][] orig = new String[][]{{"1"}, {"2", "3"}};
        String[][] copy = ReflectionHelper.copyTwoDimArray(orig);

        assertArraysEquals(orig, copy);
        copy[0][0] = "P";
        assertArraysNotEqual(orig, copy);
    }

    private void assertArraysNotEqual(String[][] orig, String[][] copy) {
        try {
            assertArraysEquals(orig, copy);
            fail();
        } catch (AssertionError ex) {
            // ok
        }
    }

    private void assertArraysEquals(String[][] array1, String[][] array2) {
        assertEquals(array1.length, array2.length);
        for (int i = 0; i < array1.length; ++i) {
            assertArraysEquals(array1[i], array2[i]);
        }
    }

    private void assertArraysEquals(String[] array1, String[] array2) {
        assertEquals(array1.length, array2.length);
        for (int i = 0; i < array1.length; ++i) {
            assertEquals(array1[i], array2[i]);
        }
    }

    private static abstract class TestClass1 {

        protected short field10;
    }

    private static class TestClass3 extends TestClass1 {

        public int field1;

        @SuppressWarnings("unused")
        private Integer field2;

        protected List<Long> field3;

        Serializable field4;

        final public ReflectionHelperTest field5 = null;

        static int field6;

        transient int field7;

        @SuppressWarnings("unused")
        private static final long field8 = 10;
    }

    private interface TestClass2 {

        int field9 = 0;
    }

    private interface TestClass4 {

        byte field11 = 4;
    }

    private static class TestClass extends TestClass3 implements TestClass2, TestClass4 {

        public Boolean fiel12;

        @SuppressWarnings("unused")
        private char field13;

        byte[] field14;

        @SuppressWarnings("rawtypes")
        protected volatile Map field15;
    }
}