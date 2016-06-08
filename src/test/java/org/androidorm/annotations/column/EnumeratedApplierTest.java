package org.androidorm.annotations.column;

import org.androidorm.accessors.DbEnumFieldAccessor;
import org.androidorm.accessors.DbPropertyAccessor;
import org.androidorm.accessors.DbSimpleFieldAccessor;
import org.androidorm.annotations.EnumType;
import org.androidorm.annotations.Enumerated;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.mutator.FieldMutator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumeratedApplierTest {

    @Test
    public void testDefaultEnumTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        EnumeratedApplier applier = new EnumeratedApplier();

        DbEnumFieldAccessor fieldMock = new DbEnumFieldAccessor();
        fieldMock.setMutator(new FieldMutator(EnumeratedApplierTestClass.class.getDeclaredField("field1")));
        applier.apply(fieldMock);

        assertEquals(EnumType.ORDINAL, fieldMock.getEnumRepresentationType());
    }

    @Test
    public void testEmptyAnnotationTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        EnumeratedApplier applier = new EnumeratedApplier();

        DbEnumFieldAccessor fieldMock = new DbEnumFieldAccessor();
        fieldMock.setMutator(new FieldMutator(EnumeratedApplierTestClass.class.getDeclaredField("field2")));
        applier.apply(fieldMock);

        assertEquals(EnumType.ORDINAL, fieldMock.getEnumRepresentationType());
    }

    @Test
    public void testOrdinalTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        EnumeratedApplier applier = new EnumeratedApplier();

        DbEnumFieldAccessor fieldMock = new DbEnumFieldAccessor();
        fieldMock.setMutator(new FieldMutator(EnumeratedApplierTestClass.class.getDeclaredField("field3")));
        applier.apply(fieldMock);

        assertEquals(EnumType.ORDINAL, fieldMock.getEnumRepresentationType());
    }

    @Test
    public void testStringTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        EnumeratedApplier applier = new EnumeratedApplier();

        DbEnumFieldAccessor fieldMock = new DbEnumFieldAccessor();
        fieldMock.setMutator(new FieldMutator(EnumeratedApplierTestClass.class.getDeclaredField("field4")));
        applier.apply(fieldMock);

        assertEquals(EnumType.STRING, fieldMock.getEnumRepresentationType());
    }

    @Test
    public void testWrongTypeTest() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        EnumeratedApplier applier = new EnumeratedApplier();

        DbPropertyAccessor fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(EnumeratedApplierTestClass.class.getDeclaredField("field5")));
        applier.apply(fieldMock);
        // no exception
    }

    private enum EnumClass1 {
        VALUE1,
        VALUE2,
        VALUE3
    }

    private static class EnumeratedApplierTestClass {

        EnumClass1 field1;

        @Enumerated
        EnumClass1 field2;

        @Enumerated(EnumType.ORDINAL)
        EnumClass1 field3;

        @Enumerated(EnumType.STRING)
        EnumClass1 field4;

        @Enumerated
        Integer field5;
    }
}



