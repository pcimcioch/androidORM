package org.androidorm.annotations;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnnotationApplierTest {

    @Test
    public void testContainOrmAnnotation_noAnnotations() {
        assertFalse(AnnotationApplier.containOrmAnnotation(new Annotation[]{}));
    }

    @Test
    public void testContainOrmAnnotation_noOrmAnnotations() throws SecurityException, NoSuchFieldException {
        Annotation[] ann = AnnotationApplierTestTestClass.class.getField("noAnnotations").getAnnotations();
        assertFalse(AnnotationApplier.containOrmAnnotation(ann));

        ann = AnnotationApplierTestTestClass.class.getField("noOrmAnnotations").getAnnotations();
        assertFalse(AnnotationApplier.containOrmAnnotation(ann));

        ann = AnnotationApplierTestTestClass.class.getField("noMultipleOrmAnnotations").getAnnotations();
        assertFalse(AnnotationApplier.containOrmAnnotation(ann));
    }

    @Test
    public void testContainOrmAnnotation_ormAnnotations() throws SecurityException, NoSuchFieldException {
        Annotation[] ann = AnnotationApplierTestTestClass.class.getField("ormAnnotations").getAnnotations();
        assertTrue(AnnotationApplier.containOrmAnnotation(ann));

        ann = AnnotationApplierTestTestClass.class.getField("ormMultipleAnnotations").getAnnotations();
        assertTrue(AnnotationApplier.containOrmAnnotation(ann));

        ann = AnnotationApplierTestTestClass.class.getField("onlyOneAnnotationIsOrm").getAnnotations();
        assertTrue(AnnotationApplier.containOrmAnnotation(ann));
    }

    private static class AnnotationApplierTestTestClass {

        public int noAnnotations;

        @Mock
        public int noOrmAnnotations;

        @Mock
        @InjectMocks
        public int noMultipleOrmAnnotations;

        @Id
        public int ormAnnotations;

        @Id
        @Column
        public int ormMultipleAnnotations;

        @Mock
        @Id
        @InjectMocks
        public int onlyOneAnnotationIsOrm;
    }
}