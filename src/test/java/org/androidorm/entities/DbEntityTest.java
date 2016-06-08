package org.androidorm.entities;

import org.androidorm.annotations.AnnotationApplier;
import org.androidorm.annotations.Id;
import org.androidorm.annotations.Table;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.MappingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DbEntityTest {

    @Test
    public void testConfigureTableTest() throws AndroidOrmException {
        DbEntity entity = new DbEntity(DbEntityTestClass1.class);
        AnnotationApplier.applyEntity(entity);
        entity.configureNames();

        assertEquals("DBENTITYTESTCLASS1", entity.getTableName());
    }

    @Test
    public void testConfigureTableAnnotationTest() throws AndroidOrmException {
        DbEntity entity = new DbEntity(DbEntityTestClass2.class);
        AnnotationApplier.applyEntity(entity);
        AnnotationApplier.applyEntity(entity);
        entity.configureNames();

        assertEquals("TABLE", entity.getTableName());
    }

    @Test
    public void testConfigureTableAnnotationEmptyTest() throws AndroidOrmException {
        DbEntity entity = new DbEntity(DbEntityTestClass3.class);
        AnnotationApplier.applyEntity(entity);
        entity.configureNames();

        assertEquals("DBENTITYTESTCLASS3", entity.getTableName());
    }

    @Test(expected = MappingException.class)
    public void testConfigureTableAnonymousTest() throws AndroidOrmException {
        DbEntity entity = new DbEntity(new Object() {
            public int field1;
        }.getClass());

        AnnotationApplier.applyEntity(entity);
        entity.configureNames();
    }

    private static class DbEntityTestClass1 {

        @Id
        public int field1;
    }

    @Table(name = "table")
    private static class DbEntityTestClass2 {

        @Id
        public int field1;
    }

    @Table
    private static class DbEntityTestClass3 {

        @Id
        public int field1;
    }

    private static class DbEntityTestClass4 {

        @Id
        public int field1;

        public int field2;
    }

    private static class DbEntityTestClass5 {
        // empty
    }
}