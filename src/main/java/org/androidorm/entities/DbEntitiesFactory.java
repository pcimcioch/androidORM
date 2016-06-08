package org.androidorm.entities;

import org.androidorm.accessors.DbAccessorsFactory;
import org.androidorm.accessors.DbForeignEntityAcessor;
import org.androidorm.accessors.DbPropertyAccessor;
import org.androidorm.annotations.AnnotationApplier;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.MappingException;
import org.androidorm.relations.MultiRelation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Factory for building dbEntities.
 */
public final class DbEntitiesFactory {

    private DbEntitiesFactory() {

    }

    /**
     * Build entities from classes.
     *
     * @param classes classes
     * @return map of class - dbEntity representing class
     * @throws AndroidOrmException if something went wrong creating dbEntities
     */
    public static Map<Class<?>, DbEntity> buildEntities(Class<?>... classes) throws AndroidOrmException {
        Map<Class<?>, DbEntity> ret = buildDbEntitiesBase(classes);
        buildAccessors(ret);
        verify(ret.values());

        return ret;
    }

    /**
     * Verify dbEntities correctness.
     *
     * @param entities entities to verify
     * @throws MappingException if any problems occurred
     */
    private static void verify(Collection<DbEntity> entities) throws MappingException {
        for (DbEntity entity : entities) {
            Validators.verifyDbEntity(entity);
        }
    }

    /**
     * Build accessors for dbEntities.
     *
     * @param dbEntitiesMap map of dbEntites
     * @throws AndroidOrmException if any problems occurred
     */
    static void buildAccessors(Map<Class<?>, DbEntity> dbEntitiesMap) throws AndroidOrmException {
        for (DbEntity entity : dbEntitiesMap.values()) {
            Collection<DbPropertyAccessor> accessors = DbAccessorsFactory.buildSimpleAccessors(entity.getEntityClass());
            entity.addAccessors(accessors);
        }

        for (DbEntity entity : dbEntitiesMap.values()) {
            List<DbForeignEntityAcessor> accessors = DbAccessorsFactory.buildToOneForeignAccessors(entity.getEntityClass(), dbEntitiesMap);
            entity.addAccessors(accessors);
        }

        for (DbEntity entity : dbEntitiesMap.values()) {
            List<MultiRelation> multi = DbAccessorsFactory.buildOneToManyRelations(entity.getEntityClass(), dbEntitiesMap);
            entity.addMultiRelations(multi);
        }
    }

    /**
     * Configure dbEntities.
     *
     * @param classes classes to create dbEntities from
     * @return map of class - dbEntity represented by class
     * @throws MappingException if dbEntity base couldn't be created
     */
    static Map<Class<?>, DbEntity> buildDbEntitiesBase(Class<?>... classes) throws MappingException {
        Set<String> uniqeTableNames = new HashSet<String>();
        Map<Class<?>, DbEntity> ret = new HashMap<Class<?>, DbEntity>();

        for (Class<?> clazz : classes) {
            DbEntity dbEntity = new DbEntity(clazz);

            AnnotationApplier.applyEntity(dbEntity);
            dbEntity.configureNames();

            if (!uniqeTableNames.add(dbEntity.getTableName())) {
                throw new MappingException("Table name %s duplicated", dbEntity.getTableName());
            }

            ret.put(clazz, dbEntity);
        }

        return ret;
    }
}
