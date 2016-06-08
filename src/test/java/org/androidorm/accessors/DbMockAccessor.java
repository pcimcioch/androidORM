package org.androidorm.accessors;

import org.androidorm.exceptions.ReflectionException;
import org.androidorm.types.SimpleType;

import java.lang.annotation.Annotation;

public class DbMockAccessor extends DbAccessor {

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        return null;
    }

    @Override
    public String extractValue(Object object) throws ReflectionException {
        return null;
    }

    @Override
    public boolean setValue(Object object, Object value) throws ReflectionException {
        return true;
    }

    @Override
    public Class<?> getRealType() {
        return null;
    }

    @Override
    public SimpleType getType() {
        return null;
    }

    @Override
    public String getFieldName() {
        return null;
    }

}
