package org.androidorm.schema;

import org.androidorm.accessors.DbAccessor;
import org.androidorm.entities.DbEntity;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.types.SimpleType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class TableGeneratorTest {

    @Test
    public void testGetTableDescriptor_typesTest() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(5);
        fields.add(createDbField(false, false, "FIELD1", true, false, Integer.class));
        fields.add(createDbField(false, false, "FIELD2", true, false, String.class));
        fields.add(createDbField(false, false, "FIELD3", true, false, float.class));
        fields.add(createDbField(false, false, "FIELD4", true, false, String.class));
        fields.add(createDbField(false, false, "FIELD5", true, false, long.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER, \"FIELD2\" TEXT, \"FIELD3\" REAL, \"FIELD4\" TEXT, \"FIELD5\" INTEGER);", descriptor);
    }

    @Test
    public void testGetTableDescriptor_oneColumnTest() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(1);
        fields.add(createDbField(false, false, "FIELD1", true, false, long.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER);", descriptor);
    }

    @Test
    public void testGetTableDescriptor_idTest() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(2);
        fields.add(createDbField(true, false, "FIELD1", true, false, Integer.class));
        fields.add(createDbField(false, false, "FIELD2", true, false, int.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER PRIMARY KEY, \"FIELD2\" INTEGER);", descriptor);
    }

    @Test
    public void testGetTableDescriptor_autoTest() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(2);
        fields.add(createDbField(true, true, "FIELD1", true, false, long.class));
        fields.add(createDbField(false, false, "FIELD2", true, false, Byte.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER PRIMARY KEY AUTOINCREMENT, \"FIELD2\" INTEGER);", descriptor);
    }

    @Test
    public void testGetTableDescriptor_nullableTest() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(2);
        fields.add(createDbField(false, false, "FIELD1", false, false, short.class));
        fields.add(createDbField(false, false, "FIELD2", true, false, int.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER NOT NULL, \"FIELD2\" INTEGER);", descriptor);
    }

    @Test
    public void testGetTableDescriptor_uniqueTest() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(2);
        fields.add(createDbField(false, false, "FIELD1", true, false, Short.class));
        fields.add(createDbField(false, false, "FIELD2", true, true, byte.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER, \"FIELD2\" INTEGER UNIQUE);", descriptor);
    }

    @Test
    public void testGetTableDescriptor_simpleTableTest() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(4);
        fields.add(createDbField(true, false, "FIELD1", true, false, Integer.class));
        fields.add(createDbField(false, false, "FIELD2", true, true, String.class));
        fields.add(createDbField(false, false, "FIELD3", false, false, Double.class));
        fields.add(createDbField(false, false, "FIELD4", false, true, Long.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER PRIMARY KEY, \"FIELD2\" TEXT UNIQUE, \"FIELD3\" REAL NOT NULL, \"FIELD4\" INTEGER NOT NULL UNIQUE);", descriptor);
    }

    @Test
    public void testGetTableDescriptor_uniqueConstraints_singleConstraintSingleColumn() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(5);
        fields.add(createDbField(false, false, "FIELD1", true, false, Integer.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);
        stub(entity.getUniqueConstraints()).toReturn(new String[][]{{"F1"}});

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER, UNIQUE(\"F1\"));", descriptor);
    }

    @Test
    public void testGetTableDescriptor_uniqueConstraints_singleConstraintMultipleColumn() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(5);
        fields.add(createDbField(false, false, "FIELD1", true, false, Integer.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);
        stub(entity.getUniqueConstraints()).toReturn(new String[][]{{"F1", "F2"}});

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER, UNIQUE(\"F1\", \"F2\"));", descriptor);
    }

    @Test
    public void testGetTableDescriptor_uniqueConstraints_multipleConstraintSingleColumn() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(5);
        fields.add(createDbField(false, false, "FIELD1", true, false, Integer.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);
        stub(entity.getUniqueConstraints()).toReturn(new String[][]{{"F1"}, {"F2"}});

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER, UNIQUE(\"F1\"), UNIQUE(\"F2\"));", descriptor);
    }

    @Test
    public void testGetTableDescriptor_uniqueConstraints_multipleConstraintMultipleColumn() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(5);
        fields.add(createDbField(false, false, "FIELD1", true, false, Integer.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);
        stub(entity.getUniqueConstraints()).toReturn(new String[][]{{"F1", "F2"}, {"F3", "F4"}});

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER, UNIQUE(\"F1\", \"F2\"), UNIQUE(\"F3\", \"F4\"));", descriptor);
    }

    @Test
    public void testGetTableDescriptor_uniqueConstraints_noConstraints() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(5);
        fields.add(createDbField(false, false, "FIELD1", true, false, Integer.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);
        stub(entity.getUniqueConstraints()).toReturn(new String[][]{});

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER);", descriptor);
    }

    @Test
    public void testGetTableDescriptor_uniqueConstraints_emptyConstraint() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(5);
        fields.add(createDbField(false, false, "FIELD1", true, false, Integer.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);
        stub(entity.getUniqueConstraints()).toReturn(new String[][]{{}});

        String descriptor = TableGenerator.getTableDescriptor(entity);
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER, UNIQUE(\"\"));", descriptor);
    }

    @Test
    public void testGetTableDescriptors_oneTable() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(5);
        fields.add(createDbField(false, false, "FIELD1", true, false, Integer.class));
        fields.add(createDbField(false, false, "FIELD2", true, false, String.class));
        fields.add(createDbField(false, false, "FIELD3", true, false, float.class));
        fields.add(createDbField(false, false, "FIELD4", true, false, String.class));
        fields.add(createDbField(false, false, "FIELD5", true, false, long.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);

        String descriptor = TableGenerator.getTableDescriptors(Collections.singletonList(entity));
        assertEquals("CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER, \"FIELD2\" TEXT, \"FIELD3\" REAL, \"FIELD4\" TEXT, \"FIELD5\" INTEGER);\n", descriptor);
    }

    @Test
    public void testGetTableDescriptors_multipleTable() throws AndroidOrmException {
        List<DbAccessor> fields = new ArrayList<DbAccessor>(5);
        fields.add(createDbField(false, false, "FIELD1", true, false, Integer.class));
        fields.add(createDbField(false, false, "FIELD2", true, false, String.class));
        fields.add(createDbField(false, false, "FIELD3", true, false, float.class));
        fields.add(createDbField(false, false, "FIELD4", true, false, String.class));
        fields.add(createDbField(false, false, "FIELD5", true, false, long.class));

        DbEntity entity = mock(DbEntity.class);
        stub(entity.getTableName()).toReturn("EX1");
        stub(entity.getSimpleAccessors()).toReturn(fields);

        List<DbAccessor> fields2 = new ArrayList<DbAccessor>(5);
        fields2.add(createDbField(false, false, "FIELD6", true, false, Integer.class));

        DbEntity entity2 = mock(DbEntity.class);
        stub(entity2.getTableName()).toReturn("EX2");
        stub(entity2.getSimpleAccessors()).toReturn(fields2);

        String descriptor = TableGenerator.getTableDescriptors(Arrays.asList(entity, entity2));
        assertEquals(
                "CREATE TABLE \"EX1\" (\"FIELD1\" INTEGER, \"FIELD2\" TEXT, \"FIELD3\" REAL, \"FIELD4\" TEXT, \"FIELD5\" INTEGER);\nCREATE TABLE \"EX2\" (\"FIELD6\" INTEGER);\n",
                descriptor);
    }

    private DbAccessor createDbField(boolean id, boolean auto, String name, boolean nullable, boolean unique, Class<?> type) {
        DbAccessor ret = mock(DbAccessor.class);
        stub(ret.isId()).toReturn(id);
        stub(ret.isAutogenerated()).toReturn(auto);
        stub(ret.getName()).toReturn(name);
        stub(ret.isNullable()).toReturn(nullable);
        stub(ret.isUnique()).toReturn(unique);
        stub(ret.getType()).toReturn(SimpleType.convert(type));

        return ret;
    }
}
