package org.androidorm.annotations.entity;

import org.androidorm.annotations.Table;
import org.androidorm.annotations.UniqueConstraint;
import org.androidorm.entities.DbEntity;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.MappingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TableApplierTest {

    @Test
    public void testNoTableAnnotation() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TableApplier applier = new TableApplier();
        DbEntity entityMock = new DbEntity(TableApplierTestClass1.class);

        applier.apply(entityMock);
        assertEquals(null, entityMock.getTableName());
    }

    @Test
    public void testTableAnnotation() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TableApplier applier = new TableApplier();
        DbEntity entityMock = new DbEntity(TableApplierTestClass2.class);

        applier.apply(entityMock);
        assertEquals("TABLE", entityMock.getTableName());
    }

    @Test
    public void testEmptyTableAnnotation() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TableApplier applier = new TableApplier();
        DbEntity entityMock = new DbEntity(TableApplierTestClass3.class);

        applier.apply(entityMock);
        assertEquals(null, entityMock.getTableName());
    }

    @Test
    public void testIncorrectTableName() throws NoSuchFieldException, SecurityException, AndroidOrmException {
        TableApplier applier = new TableApplier();
        DbEntity entityMock = new DbEntity(TableApplierTestClass4.class);

        try {
            applier.apply(entityMock);
            fail();
        } catch (MappingException ex) {
            assertTrue(ex.getMessage().contains("match"));
        }
    }

    @Test
    public void testUniqueConstraints_oneConstraintOneColumn() throws MappingException {
        TableApplier applier = new TableApplier();
        DbEntity entityMock = spy(new DbEntity(TableApplierUniqueConstraintsTestClass1.class));
        doNothing().when(entityMock).setUniqueConstraints(any(String[][].class));

        applier.apply(entityMock);
        verify(entityMock, times(1)).setUniqueConstraints(eq(new String[][]{{"f1"}}));
    }

    @Test
    public void testUniqueConstraints_oneConstraintMultipleColumns() throws MappingException {
        TableApplier applier = new TableApplier();
        DbEntity entityMock = spy(new DbEntity(TableApplierUniqueConstraintsTestClass2.class));
        doNothing().when(entityMock).setUniqueConstraints(any(String[][].class));

        applier.apply(entityMock);
        verify(entityMock, times(1)).setUniqueConstraints(eq(new String[][]{{"f1", "f2"}}));
    }

    @Test
    public void testUniqueConstraints_multipleConstrainsOneColumns() throws MappingException {
        TableApplier applier = new TableApplier();
        DbEntity entityMock = spy(new DbEntity(TableApplierUniqueConstraintsTestClass3.class));
        doNothing().when(entityMock).setUniqueConstraints(any(String[][].class));

        applier.apply(entityMock);
        verify(entityMock, times(1)).setUniqueConstraints(eq(new String[][]{{"f1"}, {"f2"}}));
    }

    @Test
    public void testUniqueConstraints_multipleConstraintsMultipleColumns() throws MappingException {
        TableApplier applier = new TableApplier();
        DbEntity entityMock = spy(new DbEntity(TableApplierUniqueConstraintsTestClass4.class));
        doNothing().when(entityMock).setUniqueConstraints(any(String[][].class));

        applier.apply(entityMock);
        verify(entityMock, times(1)).setUniqueConstraints(eq(new String[][]{{"f1", "f2"}, {"f3", "f4"}}));
    }

    @Test
    public void testUniqueConstraints_noConstraints() throws MappingException {
        TableApplier applier = new TableApplier();
        DbEntity entityMock = spy(new DbEntity(TableApplierUniqueConstraintsTestClass5.class));
        doNothing().when(entityMock).setUniqueConstraints(any(String[][].class));

        applier.apply(entityMock);
        verify(entityMock, never()).setUniqueConstraints(any(String[][].class));
    }

    @Test
    public void testUniqueConstraints_emptyConstraints() throws MappingException {
        TableApplier applier = new TableApplier();
        DbEntity entityMock = spy(new DbEntity(TableApplierUniqueConstraintsTestClass6.class));
        doNothing().when(entityMock).setUniqueConstraints(any(String[][].class));

        applier.apply(entityMock);
        verify(entityMock, times(1)).setUniqueConstraints(eq(new String[][]{{}}));
    }

    private static class TableApplierTestClass1 {
        // empty
    }

    @Table(name = "table")
    private static class TableApplierTestClass2 {
        // empty
    }

    @Table
    private static class TableApplierTestClass3 {
        // empty
    }

    @Table(name = "incorr#ct")
    private static class TableApplierTestClass4 {
        // empty
    }

    @Table(name = "Table", uniqueConstraints = {@UniqueConstraint(columnNames = "f1")})
    private static class TableApplierUniqueConstraintsTestClass1 {
        // empty
    }

    @Table(name = "Table", uniqueConstraints = {@UniqueConstraint(columnNames = {"f1", "f2"})})
    private static class TableApplierUniqueConstraintsTestClass2 {
        // empty
    }

    @Table(name = "Table", uniqueConstraints = {@UniqueConstraint(columnNames = "f1"), @UniqueConstraint(columnNames = "f2")})
    private static class TableApplierUniqueConstraintsTestClass3 {
        // empty
    }

    @Table(name = "Table", uniqueConstraints = {@UniqueConstraint(columnNames = {"f1", "f2"}), @UniqueConstraint(columnNames = {"f3", "f4"})})
    private static class TableApplierUniqueConstraintsTestClass4 {
        // empty
    }

    @Table(name = "Table", uniqueConstraints = {})
    private static class TableApplierUniqueConstraintsTestClass5 {
        // empty
    }

    @Table(name = "Table", uniqueConstraints = {@UniqueConstraint(columnNames = {})})
    private static class TableApplierUniqueConstraintsTestClass6 {
        // empty
    }
}