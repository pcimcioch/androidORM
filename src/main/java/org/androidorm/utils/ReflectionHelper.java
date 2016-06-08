package org.androidorm.utils;

import org.androidorm.annotations.Transient;
import org.androidorm.exceptions.ReflectionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Util class fore reflection.
 */
public final class ReflectionHelper {

    private ReflectionHelper() {

    }

    /**
     * Creates instance of given class.
     *
     * @param clazz the class
     * @param <T>   type ehich instance should be created
     * @return new instance
     * @throws ReflectionException if instance could not be created
     */
    public static <T> T getInstance(Class<T> clazz) throws ReflectionException {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Can't create instance of %s: Can't find default constructor", e, clazz);
        } catch (InstantiationException e) {
            throw new ReflectionException("Can't create instance of %s: Can't find default constructor", e, clazz);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Can't create instance of %s: Can't find default constructor", e, clazz);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Can't create instance of %s: Can't find default constructor", e, clazz);
        } catch (InvocationTargetException e) {
            throw new ReflectionException("Can't create instance of %s: Can't find default constructor", e, clazz);
        }
    }

    /**
     * Get all fields for given class - declared fields and all super classes fields.
     *
     * @param type the class type
     * @return list of all fields
     */
    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(filterSynthetic(c.getDeclaredFields()));
        }
        return fields;
    }

    /**
     * Filters fields removing synthetic ones.
     *
     * @param fields fields to filter
     * @return non synthetic fields
     */
    private static List<Field> filterSynthetic(Field[] fields) {
        List<Field> realFields = new ArrayList<Field>();
        for (Field field : fields) {
            if (!field.isSynthetic()) {
                realFields.add(field);
            }
        }
        return realFields;
    }

    /**
     * Get all methods for given class - declared methods and all super classes methods.
     *
     * @param type the class type
     * @return list of all fields
     */
    public static List<Method> getAllMethods(Class<?> type) {
        List<Method> methods = new ArrayList<Method>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        }
        return methods;
    }

    /**
     * Gets all fields that are accessible.
     *
     * @param type the class type
     * @return list of all accessible fields
     */
    public static List<Field> getAllAccessibleFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Field field : getAllFields(type)) {
            if (isAccesible(field)) {
                fields.add(field);
            }
        }

        return fields;
    }

    /**
     * Gets all methods that are accessible.
     *
     * @param type the class type
     * @return list of all accessible methods
     */
    public static List<Method> getAllAccessibleMethods(Class<?> type) {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : getAllMethods(type)) {
            if (isAccesible(method)) {
                methods.add(method);
            }
        }

        return methods;
    }

    /**
     * Checks if field is accessible.
     *
     * @param field field to check
     * @return if field is accessible
     */
    public static boolean isAccesible(Field field) {
        return isAccesible(field.getModifiers()) && isAccesible(field.getAnnotations());

    }

    /**
     * Checks if method is accessible.
     *
     * @param method method to check
     * @return if method is accessible
     */
    public static boolean isAccesible(Method method) {
        return isAccesible(method.getModifiers()) && isAccesible(method.getAnnotations());

    }

    /**
     * Checks if modifiers indicate that field or method is accessible.
     *
     * @param modifiers modifiers
     * @return if modifiers indicate accessible
     */
    private static boolean isAccesible(int modifiers) {
        return !Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers);

    }

    /**
     * Checks if annotations indicate that field or method is accessible.
     *
     * @param annotations annotations
     * @return if annotations indicate accessible
     */
    private static boolean isAccesible(Annotation[] annotations) {
        for (Annotation annotaion : annotations) {
            if (annotaion instanceof Transient) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if SIMPLE type is empty - null, empty string, zero etc...
     *
     * @param value value to check
     * @return if given value is empty
     */
    public static boolean isEmpty(Object value) {
        return value == null || "".equals(value) || !(value instanceof String) && "0".equals(String.valueOf(value));

    }

    /**
     * Checks if class has default no arg constructor.
     *
     * @param clazz class to check
     * @return if given class has default constructor
     */
    public static boolean hasDefaultConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor() != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Gets actual type of parametrized type or throws exception if this is raw type.
     *
     * @param paramType parametrized type
     * @return actual type
     */
    public static Class<?> getActualTypeOfParametrizedType(ParameterizedType paramType) {
        try {
            Type[] arguments = paramType.getActualTypeArguments();
            return (Class<?>) arguments[0];
        } catch (Exception ex) {
            throw new IllegalArgumentException("Type " + paramType + " can't be raw type");
        }
    }

    /**
     * Copy two dimensional array.
     *
     * @param array array to copy
     * @return copied array
     */
    public static String[][] copyTwoDimArray(String[][] array) {
        if (array == null) {
            return null;
        }

        String[][] copy = new String[array.length][];
        for (int i = 0; i < array.length; ++i) {
            copy[i] = new String[array[i].length];
            System.arraycopy(array[i], 0, copy[i], 0, array[i].length);
        }

        return copy;
    }
}
