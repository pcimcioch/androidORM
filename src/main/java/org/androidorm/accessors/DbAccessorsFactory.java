package org.androidorm.accessors;

import org.androidorm.annotations.AnnotationApplier;
import org.androidorm.annotations.ManyToOne;
import org.androidorm.annotations.OneToMany;
import org.androidorm.annotations.OneToOne;
import org.androidorm.entities.DbEntity;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.MappingException;
import org.androidorm.mutator.IMutator;
import org.androidorm.mutator.MutatorHelper;
import org.androidorm.relations.MultiRelation;
import org.androidorm.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class to construct accessors {@link DbAccessor} based on entity class.
 */
public final class DbAccessorsFactory {

    private DbAccessorsFactory() {

    }

    /**
     * Build list of simple accessors for given class.
     *
     * @param clazz the class
     * @return list of class simple accessors
     * @throws AndroidOrmException if building of any accessor failed or if there are no accessors for class or class doesn't have exactly one id accessor
     */
    public static List<DbPropertyAccessor> buildSimpleAccessors(Class<?> clazz) throws AndroidOrmException {
        List<DbPropertyAccessor> ret = new ArrayList<DbPropertyAccessor>(createSimpleAccessors(clazz));
        Validators.verifyAccessors(ret, clazz);

        return ret;
    }

    /**
     * Build list of accessors for "ToOne" relations.
     *
     * @param clazz         the class
     * @param dbEntitiesMap dbEntities map with simple accessors configured
     * @return list of accessors
     * @throws AndroidOrmException if anything wrong happend building accessors
     */
    public static List<DbForeignEntityAcessor> buildToOneForeignAccessors(Class<?> clazz, Map<Class<?>, DbEntity> dbEntitiesMap) throws AndroidOrmException {
        List<DbForeignEntityAcessor> ret = new ArrayList<DbForeignEntityAcessor>();
        for (IMutator mutator : getOneToAccessibleMutators(clazz)) {
            DbEntity foreignEntity = safeDbEntitySearch(mutator.getType(), dbEntitiesMap);

            // this field is entity field
            DbForeignEntityAcessor dbAccessor = new DbForeignEntityAcessor();
            dbAccessor.configure(mutator, foreignEntity.getIdAccessor());
            ret.add(dbAccessor);
        }

        return ret;
    }

    /**
     * Builds list of accessors for "One To Many" relations for clazz.
     *
     * @param clazz         the class
     * @param dbEntitiesMap map of existing DbEntities
     * @return list of accessors
     * @throws MappingException if type is not correct collection or if something went wrong creating accessors
     */
    public static List<MultiRelation> buildOneToManyRelations(Class<?> clazz, Map<Class<?>, DbEntity> dbEntitiesMap) throws MappingException {
        List<MultiRelation> ret = new ArrayList<MultiRelation>();
        for (IMutator mutator : getOneToManyAccessibleMutators(clazz)) {
            Class<?> relationType = mutator.getCollectionParameterizedType();
            if (relationType == null) {
                throw new MappingException("Incorrect one to many type: %s.%s.", clazz, mutator.getName());
            }
            DbEntity foreignEntity = safeDbEntitySearch(relationType, dbEntitiesMap);

            // this field is multi relation field
            String mappedBy = mutator.getAnnotation(OneToMany.class).mappedBy();
            DbForeignEntityAcessor inverseAccessor = getForeignAccessor(foreignEntity, mappedBy, clazz);
            MultiRelation ralation = new MultiRelation(inverseAccessor, mutator);
            ret.add(ralation);
        }

        return ret;
    }

    /**
     * Creates simple files accessors for given class.
     *
     * @param clazz class to construct accessors for
     * @return list of simple accessors
     * @throws AndroidOrmException if there was any problem building accessors
     */
    static List<DbPropertyAccessor> createSimpleAccessors(Class<?> clazz) throws AndroidOrmException {
        List<DbPropertyAccessor> ret = new ArrayList<DbPropertyAccessor>();

        for (IMutator mutator : MutatorHelper.getAccesibleMutators(clazz, null)) {
            DbPropertyAccessor dbAccessor = createAccessor(mutator.getType());

            if (dbAccessor != null) {
                dbAccessor.setMutator(mutator);
                AnnotationApplier.applyField(dbAccessor);
                ret.add(dbAccessor);
            }
        }

        return ret;
    }

    /**
     * Creates concrete {@link DbAccessor} for given type.
     *
     * @param type the type
     * @return concrete {@link DbAccessor} for given type. Returns null if type is not supported by any accessor
     */
    private static DbPropertyAccessor createAccessor(Class<?> type) {
        if (DbSimpleFieldAccessor.isTypeSupportedStatic(type)) {
            return new DbSimpleFieldAccessor();
        } else if (DbDateFieldAccessor.isTypeSupportedStatic(type)) {
            return new DbDateFieldAccessor();
        } else if (DbEnumFieldAccessor.isTypeSupportedStatic(type)) {
            return new DbEnumFieldAccessor();
        }

        return null;
    }

    /**
     * Safe search for DbEntity. Will either return correct DbEntity or throw exception.
     *
     * @param entityType    type of looked DbEntity
     * @param dbEntitiesMap map of existing DbEntities
     * @return found DbEntity object
     * @throws MappingException if entity was not found
     */
    private static DbEntity safeDbEntitySearch(Class<?> entityType, Map<Class<?>, DbEntity> dbEntitiesMap) throws MappingException {
        DbEntity foreignEntity = dbEntitiesMap.get(entityType);
        if (foreignEntity == null) {
            throw new MappingException("Class %s is not entity class", entityType);
        }

        return foreignEntity;
    }

    /**
     * Get accessible mutators of clazz that are annotated with {@link OneToOne} or {@link ManyToOne} annotation.
     *
     * @param clazz the class
     * @return list of accessible mutators
     * @throws MappingException if mapping is incorrect
     */
    private static List<IMutator> getOneToAccessibleMutators(Class<?> clazz) throws MappingException {
        Set<Class<? extends Annotation>> toOneAnnotations = new HashSet<Class<? extends Annotation>>();
        toOneAnnotations.add(OneToOne.class);
        toOneAnnotations.add(ManyToOne.class);

        return MutatorHelper.getAccesibleMutators(clazz, toOneAnnotations);
    }

    /**
     * Get accessible mutators of clazz that are annotated with {@link OneToMany} annotation.
     *
     * @param clazz the class
     * @return list of accessible mutators
     * @throws MappingException if mapping is incorrect
     */
    private static List<IMutator> getOneToManyAccessibleMutators(Class<?> clazz) throws MappingException {
        Set<Class<? extends Annotation>> toOneAnnotations = new HashSet<Class<? extends Annotation>>();
        toOneAnnotations.add(OneToMany.class);

        return MutatorHelper.getAccesibleMutators(clazz, toOneAnnotations);
    }

    /**
     * Searches for foreign accessor. Will either return correct accessor or throw exception.
     *
     * @param foreignEntity foreign db entity to extract accessor from
     * @param mappedBy      mappedBy property. Optional, can be null. If not empty, then accessor field name will equal to mappedBy
     * @param relationType  type that selected accessor must point to
     * @return single accessor
     * @throws MappingException if none or more than one candidate was found
     */
    private static DbForeignEntityAcessor getForeignAccessor(DbEntity foreignEntity, String mappedBy, Class<?> relationType) throws MappingException {
        List<DbForeignEntityAcessor> accessors = getForeignAccessors(foreignEntity, mappedBy, relationType);
        if (accessors.isEmpty()) {
            throw new MappingException("Could not find any type matching [type=%s, mappedBy=%s] for one to many relation in class %s", relationType, mappedBy,
                    foreignEntity.getEntityClass());
        }

        if (accessors.size() > 1) {
            throw new MappingException("Found multiple types matching [type=%s, mappedBy=%s] for one to many relation in class %s", relationType, mappedBy,
                    foreignEntity.getEntityClass());
        }

        return accessors.get(0);
    }

    /**
     * Gets all foreignEntity accessors that points to relationType.
     *
     * @param foreignEntity foreign db entity to extract accessors from
     * @param mappedBy      mappedBy property. Optional, can be null. If not empty, then accessors set will be narrowed only to accessors with field name equal to mappedBy
     * @param relationType  type that selected accessors must point to
     * @return list of all foreignEntity accessors that points to relationType
     */
    private static List<DbForeignEntityAcessor> getForeignAccessors(DbEntity foreignEntity, String mappedBy, Class<?> relationType) {
        List<DbForeignEntityAcessor> relationAccessors = new ArrayList<DbForeignEntityAcessor>();

        for (DbForeignEntityAcessor foreignAccessor : foreignEntity.getToOneForeignAccessors()) {
            if (isInverseForeignAccessor(foreignAccessor, mappedBy, relationType)) {
                relationAccessors.add(foreignAccessor);
            }
        }

        return relationAccessors;
    }

    /**
     * Returns if foreignAccessor points to relationType.
     *
     * @param foreignAccessor foreign accessor to test
     * @param mappedBy        optional, can be null. If not empty then foreignAccessor field name must be equal to mappedBy
     * @param relationType    type that selected accessor must point to
     * @return if foreignAccessor points to relationType
     */
    private static boolean isInverseForeignAccessor(DbForeignEntityAcessor foreignAccessor, String mappedBy, Class<?> relationType) {
        if (StringUtils.isNotEmpty(mappedBy)) {
            return foreignAccessor.getFieldName().equals(mappedBy);
        }

        return foreignAccessor.getRealType().equals(relationType);
    }
}
