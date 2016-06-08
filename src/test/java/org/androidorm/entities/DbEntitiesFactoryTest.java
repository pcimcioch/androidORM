package org.androidorm.entities;

import org.androidorm.annotations.Column;
import org.androidorm.annotations.Id;
import org.androidorm.annotations.Table;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.MappingException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DbEntitiesFactoryTest {

    @Test(expected = MappingException.class)
    public void testDuplicatedTableTest() throws AndroidOrmException {
        DbEntitiesFactory.buildEntities(DatabaseManagerTestClass1.class, DatabaseManagerTestClass2.class);
    }

    @Test
    public void testCreateTest() throws AndroidOrmException {
        Map<Class<?>, DbEntity> entities = DbEntitiesFactory.buildEntities(DatabaseManagerTestClass3.class, DatabaseManagerTestClass4.class);
        assertEquals(2, entities.size());

        DbEntity ent1 = entities.get(DatabaseManagerTestClass3.class);
        assertEquals("DATABASEMANAGERTESTCLASS3", ent1.getTableName());
        assertEquals(2, ent1.getSimpleAccessors().size());

        DbEntity ent2 = entities.get(DatabaseManagerTestClass4.class);
        assertEquals("DATABASEMANAGERTESTCLASS4", ent2.getTableName());
        assertEquals(3, ent2.getSimpleAccessors().size());
    }

    @Test(expected = MappingException.class)
    public void testDuplicateAttributesTest() throws AndroidOrmException {
        DbEntitiesFactory.buildEntities(DatabaseManagerTestClass7.class);
    }

    @Test(expected = MappingException.class)
    public void testNoDefaultConstructorTest() throws AndroidOrmException {
        DbEntitiesFactory.buildEntities(DatabaseManagerTestClass8.class);
    }

    private static class DatabaseManagerTestClass1 {

        @Id
        public int field1;

        public int field2;
    }

    @Table(name = "DatabaseManagerTestClass1")
    private static class DatabaseManagerTestClass2 {

        @Id
        public int field1;

        public int field2;
    }

    private static class DatabaseManagerTestClass3 {

        @Id
        public int field1;

        public int field2;
    }

    private static class DatabaseManagerTestClass4 {

        @Id
        public int field1;

        public int field2;

        public int field4;
    }

    private static class DatabaseManagerTestClass5 {

        @Id
        public int field1;

        public int field2;
    }

    private static class DatabaseManagerTestClass7 {

        @Id
        public int field1;

        @Column(name = "name")
        public int field2;

        public int field3;

        @Column(name = "name")
        public int field4;
    }

    private static class DatabaseManagerTestClass8 {

        @Id
        public int field1;

        public int field2;

        public DatabaseManagerTestClass8(int field1, int field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }
}
