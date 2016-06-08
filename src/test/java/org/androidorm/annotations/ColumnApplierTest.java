package org.androidorm.annotations;

import org.androidorm.accessors.DbAccessor;
import org.androidorm.accessors.DbPropertyAccessor;
import org.androidorm.accessors.DbSimpleFieldAccessor;
import org.androidorm.annotations.column.ColumnApplier;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.MappingException;
import org.androidorm.mutator.FieldMutator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ColumnApplierTest {

    @Test
    public void testColumAnnotation() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        ColumnApplier applier = new ColumnApplier();

        DbPropertyAccessor fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(ColumnApplierTestClass.class.getDeclaredField("field1")));
        applier.apply(fieldMock);
        assertDbField(fieldMock, null, true, false, 255);

        fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(ColumnApplierTestClass.class.getDeclaredField("field2")));
        applier.apply(fieldMock);
        assertDbField(fieldMock, "NAME2", true, false, 255);

        fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(ColumnApplierTestClass.class.getDeclaredField("field3")));
        applier.apply(fieldMock);
        assertDbField(fieldMock, null, false, false, 255);

        fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(ColumnApplierTestClass.class.getDeclaredField("field4")));
        applier.apply(fieldMock);
        assertDbField(fieldMock, null, true, true, 255);

        fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(ColumnApplierTestClass.class.getDeclaredField("field5")));
        applier.apply(fieldMock);
        assertDbField(fieldMock, null, true, false, 10);

        fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(ColumnApplierTestClass.class.getDeclaredField("field6")));
        applier.apply(fieldMock);
        assertDbField(fieldMock, "NAME6", false, true, 7);
    }

    @Test
    public void testIncorrectLengthTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        ColumnApplier applier = new ColumnApplier();

        DbPropertyAccessor fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(ColumnApplierTestClass.class.getDeclaredField("field7")));
        try {
            applier.apply(fieldMock);
            fail();
        } catch (MappingException ex) {
            // ok
        }
        assertEquals(255, fieldMock.getLength());

        fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(ColumnApplierTestClass.class.getDeclaredField("field8")));
        try {
            applier.apply(fieldMock);
            fail();
        } catch (MappingException ex) {
            // ok
        }
        assertEquals(255, fieldMock.getLength());

        fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(ColumnApplierTestClass.class.getDeclaredField("field9")));
        applier.apply(fieldMock);
        assertEquals(1, fieldMock.getLength());

        fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(ColumnApplierTestClass.class.getDeclaredField("field10")));
        try {
            applier.apply(fieldMock);
            fail();
        } catch (MappingException ex) {
            // ok
        }
        assertEquals(255, fieldMock.getLength());

        fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(ColumnApplierTestClass.class.getDeclaredField("field11")));
        applier.apply(fieldMock);
        assertEquals(65535, fieldMock.getLength());
    }

    private void assertDbField(DbAccessor field, String name, boolean nullable, boolean unique, int length) {
        if (name != null) {
            assertEquals(name, field.getName());
        }
        assertEquals(nullable, field.isNullable());
        assertEquals(unique, field.isUnique());
        assertEquals(length, field.getLength());
    }

    private static class ColumnApplierTestClass {

        @SuppressWarnings("unused")
        private String field1;

        @Column(name = "name2")
        public int field2;

        @Column(nullable = false)
        private double field3;

        @Column(unique = true)
        protected Integer field4;

        @Column(length = 10)
        String field5;

        @Column(name = "name6", unique = true, nullable = false, length = 7)
        public Double field6;

        @Column(length = -1)
        private String field7;

        @Column(length = 0)
        public String field8;

        @Column(length = 1)
        private int field9;

        @Column(length = 65536)
        Integer field10;

        @Column(length = 65535)
        protected Long field11;
    }
}
