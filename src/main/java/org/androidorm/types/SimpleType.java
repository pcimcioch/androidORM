package org.androidorm.types;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Simple types supported by database.
 */
public enum SimpleType {
    BOOLEAN(DbType.INTEGER) {
        @Override
        public void insertImpl(ContentValues values, String key, Object value) {
            values.put(key, (Boolean) value);
        }

        @Override
        protected Object extractImpl(Cursor cursor, int columnIndex) {
            return cursor.getInt(columnIndex) == 1;
        }
    },
    BYTE(DbType.INTEGER) {
        @Override
        public void insertImpl(ContentValues values, String key, Object value) {
            values.put(key, (Byte) value);
        }

        @Override
        protected Object extractImpl(Cursor cursor, int columnIndex) {
            short val = cursor.getShort(columnIndex);
            if (val < Byte.MIN_VALUE || val > Byte.MAX_VALUE) {
                throw new IllegalArgumentException("Value " + val + " is not byte value");
            }
            return (byte) val;
        }

        @Override
        public Object castFromLong(long value) {
            if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
                throw new IllegalArgumentException("Value " + value + " is not in Byte range");
            }
            return (byte) value;
        }
    },
    SHORT(DbType.INTEGER) {
        @Override
        public void insertImpl(ContentValues values, String key, Object value) {
            values.put(key, (Short) value);
        }

        @Override
        protected Object extractImpl(Cursor cursor, int columnIndex) {
            return cursor.getShort(columnIndex);
        }

        @Override
        public Object castFromLong(long value) {
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
                throw new IllegalArgumentException("Value " + value + " is not in Short range");
            }
            return (short) value;
        }
    },
    INTEGER(DbType.INTEGER) {
        @Override
        public void insertImpl(ContentValues values, String key, Object value) {
            values.put(key, (Integer) value);
        }

        @Override
        protected Object extractImpl(Cursor cursor, int columnIndex) {
            return cursor.getInt(columnIndex);
        }

        @Override
        public Object castFromLong(long value) {
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Value " + value + " is not in Integer range");
            }
            return (int) value;
        }
    },
    LONG(DbType.INTEGER) {
        @Override
        public void insertImpl(ContentValues values, String key, Object value) {
            values.put(key, (Long) value);
        }

        @Override
        protected Object extractImpl(Cursor cursor, int columnIndex) {
            return cursor.getLong(columnIndex);
        }

        @Override
        public Object castFromLong(long value) {
            return value;
        }
    },
    STRING(DbType.TEXT) {
        @Override
        public void insertImpl(ContentValues values, String key, Object value) {
            values.put(key, (String) value);
        }

        @Override
        protected Object extractImpl(Cursor cursor, int columnIndex) {
            return cursor.getString(columnIndex);
        }
    },
    DOUBLE(DbType.REAL) {
        @Override
        public void insertImpl(ContentValues values, String key, Object value) {
            values.put(key, (Double) value);
        }

        @Override
        protected Object extractImpl(Cursor cursor, int columnIndex) {
            return cursor.getDouble(columnIndex);
        }
    },
    FLOAT(DbType.REAL) {
        @Override
        public void insertImpl(ContentValues values, String key, Object value) {
            values.put(key, (Float) value);
        }

        @Override
        protected Object extractImpl(Cursor cursor, int columnIndex) {
            return cursor.getFloat(columnIndex);
        }
    },
    BLOB(DbType.TEXT) {
        @Override
        public void insertImpl(ContentValues values, String key, Object value) {
            values.put(key, (byte[]) value);
        }

        @Override
        protected Object extractImpl(Cursor cursor, int columnIndex) {
            return cursor.getBlob(columnIndex);
        }
    },
    CHARACTER(DbType.TEXT) {
        @Override
        protected void insertImpl(ContentValues values, String key, Object value) {
            values.put(key, String.valueOf(value));
        }

        @Override
        protected Object extractImpl(Cursor cursor, int columnIndex) {
            String s = cursor.getString(columnIndex);
            return s.charAt(0);
        }
    };

    private final DbType dbType;

    /**
     * Constructor.
     *
     * @param dbType database type used by this simple type
     */
    SimpleType(DbType dbType) {
        this.dbType = dbType;
    }

    /**
     * Returns Datbase type this type represents.
     *
     * @return database type
     */
    public DbType getDbType() {
        return this.dbType;
    }

    /**
     * Checks if given type can be considered simple type.
     *
     * @param type type to check
     * @return if given type is simple type
     */
    public static boolean isSimple(Class<?> type) {
        return convert(type) != null;
    }

    /**
     * Converts given type to simple type enum.
     *
     * @param type type to convert
     * @return simple type or null if given type cannot be converted to simple type
     */
    public static SimpleType convert(Class<?> type) {
        if (int.class.equals(type) || Integer.class.equals(type)) {
            return INTEGER;
        }

        if (long.class.equals(type) || Long.class.equals(type)) {
            return LONG;
        }

        if (String.class.equals(type)) {
            return STRING;
        }

        if (double.class.equals(type) || Double.class.equals(type)) {
            return DOUBLE;
        }

        if (float.class.equals(type) || Float.class.equals(type)) {
            return FLOAT;
        }

        if (char.class.equals(type) || Character.class.equals(type)) {
            return CHARACTER;
        }

        if (byte[].class.equals(type) || Byte[].class.equals(type)) {
            return BLOB;
        }

        if (boolean.class.equals(type) || Boolean.class.equals(type)) {
            return BOOLEAN;
        }

        if (short.class.equals(type) || Short.class.equals(type)) {
            return SHORT;
        }

        if (byte.class.equals(type) || Byte.class.equals(type)) {
            return BYTE;
        }

        return null;
    }

    /**
     * Implementation for insertion.
     *
     * @param values values
     * @param key    key
     * @param value  value to insert
     */
    protected abstract void insertImpl(ContentValues values, String key, Object value);

    /**
     * Implementation for {@link #extract(Cursor, int)}.
     *
     * @param cursor      cursor
     * @param columnIndex column index
     * @return extracted value
     */
    protected abstract Object extractImpl(Cursor cursor, int columnIndex);

    /**
     * Insert into values under key, value of this simple type.
     *
     * @param values values
     * @param key    key
     * @param value  value to insert into values
     */
    public void insert(ContentValues values, String key, Object value) {
        if (value == null) {
            values.putNull(key);
        } else {
            insertImpl(values, key, value);
        }
    }

    /**
     * Extract from cursor from column of columnIndex value and casts it as this simple value.
     *
     * @param cursor      cursor
     * @param columnIndex column index
     * @return extracted value
     */
    public Object extract(Cursor cursor, int columnIndex) {
        if (cursor.isNull(columnIndex)) {
            return null;
        }
        return extractImpl(cursor, columnIndex);
    }

    /**
     * Casts to this simple value from long.Throws exception if type can't be converted from long.
     *
     * @param value value to case
     * @return casted object.
     */
    public Object castFromLong(long value) {
        throw new IllegalArgumentException("Long can't be casted to " + this.name());
    }
}
