package org.androidorm.mutator;

import org.androidorm.exceptions.MappingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MethodMutatorTest {

    @Test
    public void testSimpleSetterGetter() throws NoSuchMethodException, SecurityException, MappingException {
        MethodMutator mutator = new MethodMutator(MethodMutatorTestClass1.class.getMethod("getField1"));

        assertEquals("Field1", mutator.getName());
        assertEquals(int.class, mutator.getType());
        assertEquals(MethodMutatorTestClass1.class.getMethod("getField1"), mutator.getGetter());
        assertEquals(MethodMutatorTestClass1.class.getMethod("setField1", int.class), mutator.getSetter());

        MethodMutator mutator2 = new MethodMutator(MethodMutatorTestClass1.class.getMethod("setField1", int.class));

        assertEquals("Field1", mutator2.getName());
        assertEquals(int.class, mutator2.getType());
        assertEquals(MethodMutatorTestClass1.class.getMethod("getField1"), mutator2.getGetter());
        assertEquals(MethodMutatorTestClass1.class.getMethod("setField1", int.class), mutator2.getSetter());
    }

    @Test
    public void testIsGetterSetter() throws NoSuchMethodException, SecurityException, MappingException {
        // boolean
        MethodMutator mutator = new MethodMutator(MethodMutatorTestClass1.class.getMethod("isField2"));

        assertEquals("Field2", mutator.getName());
        assertEquals(boolean.class, mutator.getType());
        assertEquals(MethodMutatorTestClass1.class.getMethod("isField2"), mutator.getGetter());
        assertEquals(MethodMutatorTestClass1.class.getMethod("setField2", boolean.class), mutator.getSetter());

        MethodMutator mutator2 = new MethodMutator(MethodMutatorTestClass1.class.getMethod("setField2", boolean.class));

        assertEquals("Field2", mutator2.getName());
        assertEquals(boolean.class, mutator2.getType());
        assertEquals(MethodMutatorTestClass1.class.getMethod("isField2"), mutator2.getGetter());
        assertEquals(MethodMutatorTestClass1.class.getMethod("setField2", boolean.class), mutator2.getSetter());

        // Boolean
        MethodMutator mutator3 = new MethodMutator(MethodMutatorTestClass1.class.getMethod("isField3"));

        assertEquals("Field3", mutator3.getName());
        assertEquals(Boolean.class, mutator3.getType());
        assertEquals(MethodMutatorTestClass1.class.getMethod("isField3"), mutator3.getGetter());
        assertEquals(MethodMutatorTestClass1.class.getMethod("setField3", Boolean.class), mutator3.getSetter());

        MethodMutator mutator4 = new MethodMutator(MethodMutatorTestClass1.class.getMethod("setField3", Boolean.class));

        assertEquals("Field3", mutator4.getName());
        assertEquals(Boolean.class, mutator4.getType());
        assertEquals(MethodMutatorTestClass1.class.getMethod("isField3"), mutator4.getGetter());
        assertEquals(MethodMutatorTestClass1.class.getMethod("setField3", Boolean.class), mutator4.getSetter());
    }

    @Test
    public void testIsNotBoolean() throws NoSuchMethodException, SecurityException, MappingException {
        try {
            new MethodMutator(MethodMutatorTestClass1.class.getMethod("setField4", long.class));
            fail();
        } catch (MappingException ex) {
            // expected
        }

        try {
            new MethodMutator(MethodMutatorTestClass1.class.getMethod("isField4"));
            fail();
        } catch (MappingException ex) {
            // expected
        }
    }

    @Test(expected = MappingException.class)
    public void testGetterNoSetter() throws NoSuchMethodException, SecurityException, MappingException {
        new MethodMutator(MethodMutatorTestClass1.class.getMethod("getField5"));
    }

    @Test(expected = MappingException.class)
    public void testGetterIncorrectTypeSetter() throws NoSuchMethodException, SecurityException, MappingException {
        new MethodMutator(MethodMutatorTestClass1.class.getMethod("getField6"));
    }

    @Test(expected = MappingException.class)
    public void testGetterIncorrectNoArgumentsSetter() throws NoSuchMethodException, SecurityException, MappingException {
        new MethodMutator(MethodMutatorTestClass1.class.getMethod("getField7"));
    }

    @Test(expected = MappingException.class)
    public void testSetterNoGetter() throws NoSuchMethodException, SecurityException, MappingException {
        new MethodMutator(MethodMutatorTestClass1.class.getMethod("setField8", int.class));
    }

    @Test(expected = MappingException.class)
    public void testSetterIncorrectTypeGetter() throws NoSuchMethodException, SecurityException, MappingException {
        new MethodMutator(MethodMutatorTestClass1.class.getMethod("setField9", int.class));
    }

    @Test(expected = MappingException.class)
    public void testSetterIncorrectNoArgumentsGetter() throws NoSuchMethodException, SecurityException, MappingException {
        new MethodMutator(MethodMutatorTestClass1.class.getMethod("setField10", int.class));
    }

    @Test(expected = MappingException.class)
    public void testNoGettrNoSetter() throws NoSuchMethodException, SecurityException, MappingException {
        new MethodMutator(MethodMutatorTestClass1.class.getMethod("agetField11"));
    }

    @Test
    public void testAccessLevels() throws NoSuchMethodException, SecurityException, MappingException {
        // potected
        MethodMutator mutator = new MethodMutator(MethodMutatorTestClass2.class.getDeclaredMethod("getField1"));

        assertEquals("Field1", mutator.getName());
        assertEquals(int.class, mutator.getType());
        assertEquals(MethodMutatorTestClass2.class.getDeclaredMethod("getField1"), mutator.getGetter());
        assertEquals(MethodMutatorTestClass2.class.getDeclaredMethod("setField1", int.class), mutator.getSetter());

        // private
        MethodMutator mutator2 = new MethodMutator(MethodMutatorTestClass2.class.getDeclaredMethod("getField2"));

        assertEquals("Field2", mutator2.getName());
        assertEquals(String.class, mutator2.getType());
        assertEquals(MethodMutatorTestClass2.class.getDeclaredMethod("getField2"), mutator2.getGetter());
        assertEquals(MethodMutatorTestClass2.class.getDeclaredMethod("setField2", String.class), mutator2.getSetter());

        // package
        MethodMutator mutator3 = new MethodMutator(MethodMutatorTestClass2.class.getDeclaredMethod("setField3", float.class));

        assertEquals("Field3", mutator3.getName());
        assertEquals(float.class, mutator3.getType());
        assertEquals(MethodMutatorTestClass2.class.getDeclaredMethod("getField3"), mutator3.getGetter());
        assertEquals(MethodMutatorTestClass2.class.getDeclaredMethod("setField3", float.class), mutator3.getSetter());
    }

    private static class MethodMutatorTestClass1 {

        public int getField1() {
            return 1;
        }

        public void setField1(int value) {
        }

        public void setField2(boolean vale) {
        }

        public boolean isField2() {
            return false;
        }

        public void setField3(Boolean value) {
        }

        public Boolean isField3() {
            return true;
        }

        public long isField4() {
            return 1L;
        }

        public void setField4(long value) {
        }

        public short getField5() {
            return 5;
        }

        public void asetField5(short value) {
        }

        public short getField6() {
            return 6;
        }

        public void setField6(byte value) {
        }

        public int getField7() {
            return 6;
        }

        public void setField7() {
        }

        public void setField8(int value) {
        }

        public int agetField8() {
            return 8;
        }

        public void setField9(int value) {
        }

        public String getField9() {
            return "8";
        }

        public void setField10(int value) {
        }

        public int getField10(int value) {
            return 10;
        }

        public int agetField11() {
            return 11;
        }
    }

    private static class MethodMutatorTestClass2 {

        protected int getField1() {
            return 1;
        }

        protected void setField1(int value) {
        }

        @SuppressWarnings("unused")
        private String getField2() {
            return "2";
        }

        @SuppressWarnings("unused")
        private void setField2(String value) {
        }

        float getField3() {
            return 1;
        }

        void setField3(float value) {
        }
    }
}