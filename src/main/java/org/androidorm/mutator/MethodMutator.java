package org.androidorm.mutator;

import org.androidorm.exceptions.MappingException;
import org.androidorm.types.CollectionType;
import org.androidorm.utils.ReflectionHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mutator that uses getters / setters to get / set values.
 */
public class MethodMutator implements IMutator {

    private static final Pattern SETTER_PATTERN = Pattern.compile("set(.*)");

    private static final Pattern GETTER_PATTERN = Pattern.compile("get(.*)");

    private static final Pattern GETTER_IS_PATTERN = Pattern.compile("is(.*)");

    private Class<?> type;

    private Class<?> collectionParametrizedType;

    private CollectionType collectionType;

    private String name;

    private Method getter;

    private Method setter;

    private Method annotedMethod;

    /**
     * Constructor.
     *
     * @param method either getter or setter method
     * @throws MappingException if mapping is incorrect
     */
    public MethodMutator(Method method) throws MappingException {
        this.annotedMethod = method;

        if (configureAnnotedSetter()) {
            findGetter();
        } else if (configureAnnotedGetter()) {
            findSetter();
        } else {
            throw new MappingException("Method: %s is not getter/setter method", method.getName());
        }

        getter.setAccessible(true);
        setter.setAccessible(true);

        tryConfigureCollection();
    }

    /**
     * If methods are collection setters / getters, configures this mutator accordingly.
     *
     * @throws MappingException if type is raw type
     */
    private void tryConfigureCollection() throws MappingException {
        collectionType = CollectionType.getCollectionType(getType());
        if (collectionType != null) {
            Type genType = getter.getGenericReturnType();
            if (genType instanceof ParameterizedType) {
                collectionParametrizedType = ReflectionHelper.getActualTypeOfParametrizedType((ParameterizedType) genType);
            } else {
                throw new MappingException("Type of method %s can't be raw type", getter.getName());
            }
        }
    }

    /**
     * Tries to configure annotedMethod as setter.
     *
     * @return if configuration as setter succeeded
     */
    private boolean configureAnnotedSetter() {
        if (annotedMethod.getReturnType() == void.class && annotedMethod.getParameterTypes().length == 1) {
            Matcher m = SETTER_PATTERN.matcher(annotedMethod.getName());
            if (m.matches()) {
                this.name = m.group(1);
                this.type = annotedMethod.getParameterTypes()[0];
                this.setter = annotedMethod;
                return true;
            }
        }

        return false;
    }

    /**
     * Tries to configure annotedMethod as getter.
     *
     * @return if configuration as getter succeeded
     */
    private boolean configureAnnotedGetter() {
        Class<?> returnType = annotedMethod.getReturnType();
        String methodName = annotedMethod.getName();

        if (returnType != void.class && annotedMethod.getParameterTypes().length == 0) {
            Matcher m = GETTER_PATTERN.matcher(methodName);
            if (!m.matches() && (boolean.class.equals(returnType) || Boolean.class.equals(returnType))) {
                m = GETTER_IS_PATTERN.matcher(methodName);
            }

            if (m.matches()) {
                this.name = m.group(1);
                this.type = returnType;
                this.getter = annotedMethod;
                return true;
            }
        }

        return false;
    }

    /**
     * Finds and configures getter for annotedMethod, which is setter.
     *
     * @throws MappingException if getter method is missing
     */
    private void findGetter() throws MappingException {
        Class<?> clazz = annotedMethod.getDeclaringClass();
        Method getterMethod = null;

        try {
            getterMethod = clazz.getDeclaredMethod("get" + name);
        } catch (NoSuchMethodException e) {
            try {
                getterMethod = clazz.getDeclaredMethod("is" + name);
                if (getterMethod != null) {
                    Class<?> returnType = getterMethod.getReturnType();

                    if (!boolean.class.equals(returnType) && !Boolean.class.equals(returnType)) {
                        getterMethod = null;
                    }
                }
            } catch (NoSuchMethodException ex) {
                // ignore
            }
        }

        if (getterMethod == null || !getterMethod.getReturnType().equals(type)) {
            throw new MappingException("Missing getter method for: %s of type: %s", name, type.getName());
        }

        this.getter = getterMethod;
    }

    /**
     * Finds and configures setter for annotedMethod, which is getter.
     *
     * @throws MappingException if setter method is missing
     */
    private void findSetter() throws MappingException {
        Class<?> clazz = annotedMethod.getDeclaringClass();
        Method setterMethod = null;

        try {
            setterMethod = clazz.getDeclaredMethod("set" + name, type);
        } catch (NoSuchMethodException ex) {
            // ignore
        }

        if (setterMethod == null || !setterMethod.getReturnType().equals(void.class)) {
            throw new MappingException("Missing setter method for: %s of type: %s", name, type.getName());
        }

        this.setter = setterMethod;
    }

    /**
     * Return getter method.
     *
     * @return getter method
     */
    Method getGetter() {
        return this.getter;
    }

    /**
     * Return setter method.
     *
     * @return setter method
     */
    Method getSetter() {
        return this.setter;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return annotedMethod.getAnnotation(annotationClass);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void set(Object target, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        setter.invoke(target, value);
    }

    @Override
    public Object get(Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return getter.invoke(target);
    }

    @Override
    public Class<?> getCollectionParameterizedType() {
        return collectionParametrizedType;
    }

    @Override
    public CollectionType getCollectionType() {
        return collectionType;
    }
}
