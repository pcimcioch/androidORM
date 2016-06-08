package org.androidorm.entities;

import org.androidorm.accessors.DbAccessor;
import org.androidorm.accessors.DbForeignEntityAcessor;
import org.androidorm.accessors.DbPropertyAccessor;
import org.androidorm.exceptions.MappingException;
import org.androidorm.relations.MultiRelation;
import org.androidorm.utils.IAnnotable;
import org.androidorm.utils.ReflectionHelper;
import org.androidorm.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Class representing mapping between java class and database.
 */
public class DbEntity implements IAnnotable {

    private String tableName;

    private Class<?> clazz;

    private String[][] uniqueConstraints;

    private List<DbAccessor> simpleAccessors = new ArrayList<DbAccessor>();

    private List<MultiRelation> multiRelations = new ArrayList<MultiRelation>();

    private DbPropertyAccessor idAccessor;

    private int idPosition;

    /**
     * Constructor.
     *
     * @param clazz class this dbEntity represents
     */
    public DbEntity(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Return table name.
     *
     * @return table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Return class this entity represents.
     *
     * @return class type this entity represents
     */
    public Class<?> getEntityClass() {
        return clazz;
    }

    /**
     * Return array of database unique constraints on this dbEntity.
     *
     * @return array of constraints
     */
    public String[][] getUniqueConstraints() {
        return ReflectionHelper.copyTwoDimArray(uniqueConstraints);
    }

    /**
     * Set array of database unique constraints on this dbEntity.
     *
     * @param uniqueConstraints array of constraints
     */
    public void setUniqueConstraints(String[][] uniqueConstraints) {
        this.uniqueConstraints = ReflectionHelper.copyTwoDimArray(uniqueConstraints);
    }

    /**
     * Set table name.
     *
     * @param tableName name
     * @throws MappingException if name has incorrect format
     */
    public void setTableName(String tableName) throws MappingException {
        if (StringUtils.isNotEmpty(tableName)) {
            StringUtils.checkName(tableName);
            this.tableName = tableName.toUpperCase(Locale.ENGLISH);
        }
    }

    /**
     * Get instance of annotationClass applied to class represented by this dbEntity.
     *
     * @param annotationClass class of the annotation to retrieve
     * @param <T>             annotation type
     * @return annotation instance or null, if annotation of given class do not exist
     */
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return this.clazz.getAnnotation(annotationClass);
    }

    /**
     * Return list of simple accessors.
     *
     * @return list of accessors
     */
    public List<DbAccessor> getSimpleAccessors() {
        return simpleAccessors;
    }

    /**
     * Return list of foreign entity accessors.
     *
     * @return list of foreign entity accessors
     */
    public List<DbForeignEntityAcessor> getToOneForeignAccessors() {
        List<DbForeignEntityAcessor> ret = new ArrayList<DbForeignEntityAcessor>();
        for (DbAccessor accessor : simpleAccessors) {
            if (accessor instanceof DbForeignEntityAcessor) {
                ret.add((DbForeignEntityAcessor) accessor);
            }
        }

        return ret;
    }

    /**
     * Configure this dbEntity table name.
     *
     * @throws MappingException if names cannot be determined
     */
    void configureNames() throws MappingException {
        if (StringUtils.isEmpty(tableName)) {
            setTableName(clazz.getSimpleName());
        }

        if (StringUtils.isEmpty(tableName)) {
            throw new MappingException("Anonymous nameless class passed without table name");
        }
    }

    /**
     * Get accessor for identifier.
     *
     * @return identifier accessor
     */
    public DbAccessor getIdAccessor() {
        return idAccessor;
    }

    /**
     * Add multiRelations.
     *
     * @param toAdd relations to add
     */
    public void addMultiRelations(Collection<? extends MultiRelation> toAdd) {
        for (MultiRelation multiRelation : toAdd) {
            addMultiRelation(multiRelation);
        }
    }

    /**
     * Add multiRelation.
     *
     * @param multiRelation relation to add
     */
    public void addMultiRelation(MultiRelation multiRelation) {
        this.multiRelations.add(multiRelation);
    }

    /**
     * Get list of multiRelations.
     *
     * @return list of multiRelations
     */
    public List<MultiRelation> getMultiRelation() {
        return multiRelations;
    }

    /**
     * Add accessors.
     *
     * @param toAdd collection of accessors to add
     */
    public void addAccessors(Collection<? extends DbAccessor> toAdd) {
        for (DbAccessor accessor : toAdd) {
            addAccessor(accessor);
        }
    }

    /**
     * Add accessor.
     *
     * @param accessor accessor to add
     */
    public void addAccessor(DbAccessor accessor) {
        if (accessor.isId()) {
            this.idAccessor = (DbPropertyAccessor) accessor;
            this.idPosition = this.simpleAccessors.size();
        }

        this.simpleAccessors.add(accessor);
    }

    /**
     * Return position of identifier accessor in accessors list.
     *
     * @return position of identifier accessor
     */
    public int getIdPosition() {
        return idPosition;
    }
}
