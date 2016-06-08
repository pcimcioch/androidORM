package org.androidorm.utils;

import org.androidorm.exceptions.MappingException;

import java.util.regex.Pattern;

/**
 * Util class for strings.
 */
public final class StringUtils {

    private static final String NAME_REGEX = "[a-zA-Z_][a-zA-Z0-9_]*";

    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    private StringUtils() {

    }

    /**
     * Checks if name is correct.
     *
     * @param arg string to check
     * @throws MappingException if string is not correct
     */
    public static void checkName(String arg) throws MappingException {
        if (!NAME_PATTERN.matcher(arg).matches()) {
            throw new MappingException("Name %s must match %s", arg, NAME_REGEX);
        }
    }

    /**
     * Checks if string is null or empty (blank).
     *
     * @param arg string to check
     * @return if string is empty
     */
    public static boolean isEmpty(String arg) {
        return arg == null || "".equals(arg);
    }

    /**
     * Checks if string is not null nor empty (blank).
     *
     * @param arg string to check
     * @return if string is not empty
     */
    public static boolean isNotEmpty(String arg) {
        return arg != null && !"".equals(arg);
    }

    /**
     * Joins array of strings separating them by delimiter and adds prefix to each one.
     *
     * @param elements  arrays of elements to join
     * @param delimiter delimiter
     * @param prefix    prefix
     * @return string of joined elements
     */
    public static String join(String[] elements, String delimiter, String prefix) {
        StringBuilder builder = new StringBuilder();
        if (elements != null && elements.length > 0) {
            for (int i = 0; i < elements.length; ++i) {
                if (prefix != null) {
                    builder.append(prefix);
                }
                builder.append(elements[i]);
                if (i < elements.length - 1) {
                    builder.append(delimiter);
                }
            }
        }

        return builder.toString();
    }

    /**
     * Creates array of string from array of objects, calling {@link #toString()} on each.
     *
     * @param elements elements to map
     * @return array of strings
     */
    public static String[] toStringArray(Object[] elements) {
        String[] stringArgs = null;
        if (elements != null) {
            stringArgs = new String[elements.length];
            for (int i = 0; i < elements.length; ++i) {
                stringArgs[i] = String.valueOf(elements[i]);
            }
        }

        return stringArgs;
    }
}
