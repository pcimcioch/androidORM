package org.androidorm.annotations.column;

import org.androidorm.accessors.DbPropertyAccessor;
import org.androidorm.accessors.DbSimpleFieldAccessor;
import org.androidorm.annotations.Column;
import org.androidorm.annotations.Id;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.mutator.FieldMutator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IdApllierTest {

    @Test
    public void testIdAnnotation() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        IdApplier applier = new IdApplier();

        DbPropertyAccessor fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(IdApllierTestClass.class.getDeclaredField("field1")));
        applier.apply(fieldMock);
        assertFalse(fieldMock.isId());

        fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(IdApllierTestClass.class.getDeclaredField("field2")));
        applier.apply(fieldMock);
        assertTrue(fieldMock.isId());
        assertTrue(fieldMock.isUnique());
        assertFalse(fieldMock.isNullable());
    }

    @Test
    public void testIdColumnConflict() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        IdApplier applier = new IdApplier();

        DbPropertyAccessor fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(IdApllierTestClass.class.getDeclaredField("field3")));
        applier.apply(fieldMock);
        assertTrue(fieldMock.isId());
        assertTrue(fieldMock.isUnique());
        assertFalse(fieldMock.isNullable());

        fieldMock = new DbSimpleFieldAccessor();
        fieldMock.setMutator(new FieldMutator(IdApllierTestClass.class.getDeclaredField("field4")));
        applier.apply(fieldMock);
        assertTrue(fieldMock.isId());
        assertTrue(fieldMock.isUnique());
        assertFalse(fieldMock.isNullable());
    }

    private static class IdApllierTestClass {

        private String field1;

        @Id
        private int field2;

        @Id
        @Column
        private String field3;

        @Column
        @Id
        private String field4;
    }
}
