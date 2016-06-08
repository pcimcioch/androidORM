package org.androidorm.accessors;

import org.androidorm.accessors.testclasses.method.TestClassMethod;
import org.androidorm.accessors.testclasses.method.TestClassMethodInheritance;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManyChild;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManyChildInverse1;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManyChildInverse2;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManyChildMappedBy;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManyChildMappedBy2;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManyList;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManyMappedBy;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManyMappedBy2;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManyNoInverseSet1;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManyNoInverseSet2;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManyRaw;
import org.androidorm.accessors.testclasses.onetomany.TestClassOneToManySet;
import org.androidorm.accessors.testclasses.simple.TestClassSimpleCorrect;
import org.androidorm.accessors.testclasses.simple.TestClassSimpleExtension;
import org.androidorm.accessors.testclasses.simple.TestClassSimpleId;
import org.androidorm.accessors.testclasses.simple.TestClassSimpleNoId;
import org.androidorm.accessors.testclasses.simple.TestClassSimpleTransient;
import org.androidorm.accessors.testclasses.simple.TestClassSimpleTwoId;
import org.androidorm.accessors.testclasses.simple.TestClassSimpleTypes;
import org.androidorm.accessors.testclasses.simple.TestClassSimpleUnaccessible;
import org.androidorm.accessors.testclasses.simple.TestClassSimpleVisibility;
import org.androidorm.entities.DbEntitiesFactory;
import org.androidorm.entities.DbEntity;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.MappingException;
import org.androidorm.relations.MultiRelation;
import org.androidorm.types.SimpleType;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DbAccessorsFactoryTest {

    @Test
    public void testCreateSimple_transientTest() throws AndroidOrmException {
        Collection<DbPropertyAccessor> list = DbAccessorsFactory.createSimpleAccessors(TestClassSimpleTransient.class);
        assertEquals(2, list.size());
        assertContains("FIELD5", list);
        assertContains("FIELD6", list);
    }

    @Test
    public void testCreateSimple_typesTest() throws AndroidOrmException {
        Collection<DbPropertyAccessor> list = DbAccessorsFactory.createSimpleAccessors(TestClassSimpleTypes.class);

        assertEquals(10, list.size());
        assertContains("FIELD1", list);
        assertContains("FIELD2", list);
        assertContains("FIELD3", list);
        assertContains("FIELD4", list);
        assertContains("FIELD5", list);
        assertContains("FIELD6", list);
        assertContains("FIELD7", list);
        assertContains("FIELD8", list);
        assertContains("FIELD9", list);
        assertContains("FIELD10", list);
    }

    @Test
    public void testCreateSimple_visibilityTest() throws AndroidOrmException {
        Collection<DbPropertyAccessor> list = DbAccessorsFactory.createSimpleAccessors(TestClassSimpleVisibility.class);

        assertEquals(4, list.size());
        assertContains("FIELD1", list);
        assertContains("FIELD2", list);
        assertContains("FIELD3", list);
        assertContains("FIELD4", list);
    }

    @Test
    public void testCreateAccessors_idTest() throws AndroidOrmException {
        try {
            DbAccessorsFactory.buildSimpleAccessors(TestClassSimpleNoId.class);
            fail();
        } catch (MappingException ex) {
            // ok
        }

        DbAccessorsFactory.buildSimpleAccessors(TestClassSimpleId.class);

        try {
            DbAccessorsFactory.buildSimpleAccessors(TestClassSimpleTwoId.class);
            fail();
        } catch (MappingException ex) {
            // ok
        }
    }

    @Test
    public void testCreateSimple_correctTest() throws AndroidOrmException {
        Collection<DbPropertyAccessor> list = DbAccessorsFactory.createSimpleAccessors(TestClassSimpleCorrect.class);

        assertEquals(5, list.size());
        assertContains("ID", true, SimpleType.INTEGER, true, false, list);
        assertContains("FIELD2", false, SimpleType.STRING, false, true, list);
        assertContains("FIELD3", false, SimpleType.STRING, false, true, list);
        assertContains("FIELD4", false, SimpleType.DOUBLE, true, true, list);
        assertContains("FIELD6", false, SimpleType.FLOAT, false, false, list);
    }

    @Test
    public void testCreateSimple_extendTest() throws AndroidOrmException {
        Collection<DbPropertyAccessor> list = DbAccessorsFactory.createSimpleAccessors(TestClassSimpleExtension.class);

        assertEquals(3, list.size());
        assertContains("FIELD1", list);
        assertContains("FIELD2", list);
        assertContains("FIELD3", list);
    }

    @Test
    public void testCreateSimple_unaccesibleTest() throws AndroidOrmException {
        Collection<DbPropertyAccessor> list = DbAccessorsFactory.createSimpleAccessors(TestClassSimpleUnaccessible.class);

        assertEquals(3, list.size());
        assertContains("FIELD1", list);
        assertContains("FIELD2", list);
        assertContains("FIELD5", list);
    }

    @Test
    public void testCreateSimple_methodTest() throws AndroidOrmException {
        Collection<DbPropertyAccessor> list = DbAccessorsFactory.createSimpleAccessors(TestClassMethod.class);

        assertEquals(4, list.size());
        assertContains("FIELD1", list);
        assertContains("FIELD2", list);
        assertContains("FIELD3", list);
        assertContains("FIELD5", list);
    }

    @Test
    public void testCreateSimple_methodInheritanceTest() throws AndroidOrmException {
        Collection<DbPropertyAccessor> list = DbAccessorsFactory.createSimpleAccessors(TestClassMethodInheritance.class);

        assertEquals(6, list.size());
        assertContains("FIELD1", list);
        assertContains("FIELD2", list);
        assertContains("FIELD3", list);
        assertContains("FIELD5", list);

        assertContains("FIELD6", list);
        assertContains("FIELD7", list);
    }

    @Test
    public void testCreateOneToManyRelations_simpleListTest() throws AndroidOrmException {
        Map<Class<?>, DbEntity> map = DbEntitiesFactory.buildEntities(TestClassOneToManyChild.class, TestClassOneToManyList.class, TestClassOneToManySet.class);
        DbEntity dbEntity1 = map.get(TestClassOneToManyChild.class);
        DbEntity dbEntity2 = map.get(TestClassOneToManyList.class);

        assertEquals(2, dbEntity1.getToOneForeignAccessors().size());
        assertEquals(1, dbEntity2.getMultiRelation().size());

        MultiRelation multiRelation = dbEntity2.getMultiRelation().get(0);
        DbForeignEntityAcessor relationAccessor = dbEntity1.getToOneForeignAccessors().get(0);
        assertEquals(relationAccessor, multiRelation.getInverseAccessor());
    }

    @Test
    public void testCreateOneToManyRelations_simpleSetTest() throws AndroidOrmException {
        Map<Class<?>, DbEntity> map = DbEntitiesFactory.buildEntities(TestClassOneToManyChild.class, TestClassOneToManyList.class, TestClassOneToManySet.class);
        DbEntity dbEntity1 = map.get(TestClassOneToManyChild.class);
        DbEntity dbEntity2 = map.get(TestClassOneToManySet.class);

        assertEquals(2, dbEntity1.getToOneForeignAccessors().size());
        assertEquals(1, dbEntity2.getMultiRelation().size());

        MultiRelation multiRelation = dbEntity2.getMultiRelation().get(0);
        DbForeignEntityAcessor relationAccessor = dbEntity1.getToOneForeignAccessors().get(1);
        assertEquals(relationAccessor, multiRelation.getInverseAccessor());
    }

    @Test
    public void testCreateOneToManyRelations_rawTypeTest() throws AndroidOrmException {
        try {
            DbEntitiesFactory.buildEntities(TestClassOneToManyRaw.class);
            fail();
        } catch (MappingException ex) {
            assertTrue(ex.getMessage().contains("raw"));
        }
    }

    @Test
    public void testCreateOneToManyRelations_mappedByTypeTest() throws AndroidOrmException {
        Map<Class<?>, DbEntity> map = DbEntitiesFactory.buildEntities(TestClassOneToManyMappedBy.class, TestClassOneToManyChildMappedBy.class);
        DbEntity dbEntity1 = map.get(TestClassOneToManyChildMappedBy.class);
        DbEntity dbEntity2 = map.get(TestClassOneToManyMappedBy.class);

        assertEquals(2, dbEntity1.getToOneForeignAccessors().size());
        assertEquals(1, dbEntity2.getMultiRelation().size());

        MultiRelation multiRelation = dbEntity2.getMultiRelation().get(0);
        DbForeignEntityAcessor relationAccessor = dbEntity1.getToOneForeignAccessors().get(1);
        assertEquals(relationAccessor, multiRelation.getInverseAccessor());
    }

    @Test
    public void testCreateOneToManyRelations_noInverseRelationTest() throws AndroidOrmException {
        try {
            DbEntitiesFactory.buildEntities(TestClassOneToManyNoInverseSet1.class, TestClassOneToManyChildInverse1.class);
            fail();
        } catch (MappingException ex) {
            assertTrue(ex.getMessage().contains("any type matching"));
        }

        try {
            DbEntitiesFactory.buildEntities(TestClassOneToManyNoInverseSet2.class, TestClassOneToManyChildInverse2.class);
            fail();
        } catch (MappingException ex) {
            assertTrue(ex.getMessage().contains("any type matching"));
        }
    }

    @Test
    public void testCreateOneToManyRelations_multipleInverseRelationsTest() throws AndroidOrmException {
        try {
            DbEntitiesFactory.buildEntities(TestClassOneToManyMappedBy2.class, TestClassOneToManyChildMappedBy2.class);
            fail();
        } catch (MappingException ex) {
            assertTrue(ex.getMessage().contains("multiple types matching"));
        }
    }

    private void assertContains(String name, boolean id, SimpleType type, boolean unique, boolean nullable, Collection<DbPropertyAccessor> list) {
        for (DbAccessor elem : list) {
            if (name.equals(elem.getName()) && id == elem.isId() && type.equals(elem.getType()) && unique == elem.isUnique() && nullable == elem.isNullable()) {
                return;
            }
        }

        fail();
    }

    private void assertContains(String name, Collection<DbPropertyAccessor> list) {
        for (DbAccessor elem : list) {
            if (name.equals(elem.getName())) {
                return;
            }
        }

        fail();
    }
}
