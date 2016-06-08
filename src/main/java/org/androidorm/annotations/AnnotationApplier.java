package org.androidorm.annotations;

import org.androidorm.accessors.DbAccessor;
import org.androidorm.annotations.column.ColumnApplier;
import org.androidorm.annotations.column.EnumeratedApplier;
import org.androidorm.annotations.column.GeneratedValueApplier;
import org.androidorm.annotations.column.IdApplier;
import org.androidorm.annotations.entity.TableApplier;
import org.androidorm.entities.DbEntity;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.MappingException;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for applying orm mapping annotations to entity classes.
 */
public final class AnnotationApplier {

    private static final List<Applier<DbAccessor, ? extends Annotation>> FIELD_APPLIERS = new ArrayList<Applier<DbAccessor, ? extends Annotation>>();

    private static final List<Applier<DbEntity, ? extends Annotation>> ENTITY_APPLIERS = new ArrayList<Applier<DbEntity, ? extends Annotation>>();

    static {
        addFieldApplier(new ColumnApplier());
        addFieldApplier(new IdApplier());
        addFieldApplier(new GeneratedValueApplier());
        addFieldApplier(new EnumeratedApplier());

        addEntityApplier(new TableApplier());
    }

    private AnnotationApplier() {

    }

    /**
     * Add field applier.
     *
     * @param applier applier to add
     */
    private static void addFieldApplier(Applier<DbAccessor, ? extends Annotation> applier) {
        FIELD_APPLIERS.add(applier);
    }

    /**
     * Add entity applier.
     *
     * @param applier applier to add
     */
    private static void addEntityApplier(Applier<DbEntity, ? extends Annotation> applier) {
        ENTITY_APPLIERS.add(applier);
    }

    /**
     * Configure given accessor with every possible applier.
     *
     * @param accessor accessor to configure using appliers
     * @throws AndroidOrmException if applying went wrong
     */
    public static void applyField(DbAccessor accessor) throws AndroidOrmException {
        for (Applier<DbAccessor, ?> applier : FIELD_APPLIERS) {
            applier.apply(accessor);
        }
    }

    /**
     * Configure given dbEntity with every possible applier.
     *
     * @param dbEntity dbEntity to configure using appliers
     * @throws MappingException if applying went wrong
     */
    public static void applyEntity(DbEntity dbEntity) throws MappingException {
        for (Applier<DbEntity, ?> applier : ENTITY_APPLIERS) {
            applier.apply(dbEntity);
        }
    }

    /**
     * Returns if any of given annotations is orm annotation.
     *
     * @param annotations array of annotations to check
     * @return if any of given annotation is orm annotation
     */
    public static boolean containOrmAnnotation(Annotation[] annotations) {
        for (Applier<DbAccessor, ?> applier : FIELD_APPLIERS) {
            for (Annotation annotation : annotations) {
                Class<?> applierAnnotationClass = applier.getAnnotationType();
                Class<?> annotationClass = annotation.annotationType();
                if (applierAnnotationClass.equals(annotationClass)) {
                    return true;
                }
            }
        }

        return false;
    }
}
