package org.androidorm.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import org.androidorm.accessors.DbAccessor;
import org.androidorm.exceptions.DatabaseSqlException;
import org.androidorm.exceptions.DatabaseStateException;
import org.androidorm.exceptions.TransactionException;
import org.androidorm.types.SimpleType;
import org.androidorm.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class providing operations on database.
 */
public class DatabaseProvider {

    private static final Pattern AS_PATTERN = Pattern.compile("(?i)\\s*as (\\w+).*");

    private final DatabaseConnectionProvider dbConnection;

    /**
     * Constructor.
     *
     * @param dbConnection database connection provider
     */
    public DatabaseProvider(DatabaseConnectionProvider dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Inserts data into database.
     *
     * @param tableName table name
     * @param values    values to insert
     * @return id of newly created row in database
     * @throws DatabaseSqlException if insertion failed
     * @throws TransactionException if database connection failed
     */
    public long insertToDb(String tableName, ContentValues values) throws DatabaseSqlException, TransactionException {
        try {
            return dbConnection.getConnection().insertOrThrow(tableName, null, values);
        } catch (SQLException ex) {
            throw new DatabaseSqlException("Can't insert %s", ex, ex.getCause().getMessage());
        }
    }

    /**
     * Removes data from database.
     *
     * @param tableName table name
     * @param idField   name of id field
     * @param id        id value
     * @return count of removed records
     * @throws DatabaseSqlException if removing failed
     * @throws TransactionException if database connection failed
     */
    public int removeFromDb(String tableName, String idField, Object id) throws DatabaseSqlException, TransactionException {
        try {
            return dbConnection.getConnection().delete(tableName, idField + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException ex) {
            throw new DatabaseSqlException("Can't remove %s", ex, ex.getCause().getMessage());
        }
    }

    /**
     * Updates record in database.
     *
     * @param tableName table name
     * @param values    new values
     * @param idField   name of the id field
     * @param oldId     id value of the record taht should be updated
     * @return count of updated rows
     * @throws DatabaseSqlException if update failed
     * @throws TransactionException if database connection failed
     */
    public int updateInDb(String tableName, ContentValues values, String idField, Object oldId) throws DatabaseSqlException, TransactionException {
        try {
            return dbConnection.getConnection().update(tableName, values, idField + "=?", new String[]{String.valueOf(oldId)});
        } catch (SQLException ex) {
            throw new DatabaseSqlException("Can't insert %s", ex, ex.getCause().getMessage());
        }
    }

    /**
     * Find data in database.
     *
     * @param tableName table name
     * @param idField   id field name
     * @param id        id value
     * @param accessors accessors that represents columns to get
     * @return array of values for given accessors
     * @throws DatabaseSqlException   if finding failed
     * @throws DatabaseStateException if database has incorrect state
     * @throws TransactionException   if database connection failed
     */
    //TODO instead of collection of acessors it would be better to get two arrays - column names and simple types
    public Object[] findInDatabase(String tableName, String idField, Object id, Collection<DbAccessor> accessors)
            throws DatabaseSqlException, DatabaseStateException, TransactionException {
        Cursor cursor = null;
        try {
            String[] columns = getColumns(accessors);
            cursor = dbConnection.getConnection().query(tableName, columns, idField + "=?", new String[]{String.valueOf(id)}, null, null, null);
            return getOneFromCursor(cursor, accessors, id);
        } catch (SQLException ex) {
            throw new DatabaseSqlException("Can't find %s", ex, ex.getCause().getMessage());
        } finally {
            releaseCursor(cursor);
        }
    }

    /**
     * Find all records in database for given query.
     *
     * @param sql       sql query
     * @param args      arguments for sql query
     * @param tableName table name
     * @param accessors accessors that represents columns to get
     * @return list of arrays of values for given accessors. Each list element is one record in database
     * @throws DatabaseSqlException   if finding failed
     * @throws DatabaseStateException if databse state is incorrect in respect to mapping
     * @throws TransactionException   if database connection failed
     */
    public List<Object[]> doFindAll(String sql, String[] args, String tableName, Collection<DbAccessor> accessors)
            throws DatabaseSqlException, DatabaseStateException, TransactionException {
        Cursor cursor = null;
        try {
            String[] columns = getColumns(accessors);
            String query = "SELECT " + StringUtils.join(columns, ", ", definePrefix(sql, tableName)) + " FROM " + tableName;
            if (StringUtils.isNotEmpty(sql)) {
                query += " " + sql;
            }

            cursor = dbConnection.getConnection().rawQuery(query, args);
            return getAllFromCursor(cursor, accessors);
        } catch (SQLException ex) {
            throw new DatabaseSqlException("Can't find %s", ex, ex.getCause().getMessage());
        } finally {
            releaseCursor(cursor);
        }
    }

    /**
     * Release given cursor.
     *
     * @param cursor cursor to release
     */
    private void releaseCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * Get exactly one record from given cursor.
     *
     * @param cursor    cursor with results
     * @param accessors accessors that represents columns to get
     * @param id        id of the row
     * @return list of column values
     * @throws DatabaseStateException if there are more or less results than 1
     */
    private Object[] getOneFromCursor(Cursor cursor, Collection<DbAccessor> accessors, Object id) throws DatabaseStateException {
        if (cursor.getCount() == 0) {
            return null;
        } else if (cursor.getCount() != 1) {
            throw new DatabaseStateException("Found more then one object with [id=%s]", id);
        }

        cursor.moveToFirst();
        return createFromCursor(accessors, cursor);
    }

    /**
     * For given sql appendix, defines how queried table is named (aliased). If the sql query contains 'as', then different name than tableName should be used.
     *
     * @param sql       sql query appendix
     * @param tableName table name
     * @return prefix of table alias / name
     */
    private String definePrefix(String sql, String tableName) {
        if (sql != null) {
            Matcher matcher = AS_PATTERN.matcher(sql);
            if (matcher.matches()) {
                return matcher.group(1) + ".";
            }
        }

        return tableName + ".";
    }

    /**
     * Extracts records values from cursor.
     *
     * @param cursor    cursor with results
     * @param accessors accessors that represents columns to get
     * @return list of arrays of values for given accessors. Each list element is one record in database
     * @throws DatabaseStateException if value extraction failed
     */
    private List<Object[]> getAllFromCursor(Cursor cursor, Collection<DbAccessor> accessors) throws DatabaseStateException {
        List<Object[]> result = new ArrayList<Object[]>(cursor.getCount());

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(createFromCursor(accessors, cursor));
            cursor.moveToNext();
        }

        return result;
    }

    /**
     * Creates one record from current cursor position.
     *
     * @param accessors accessors that represents columns to get
     * @param cursor    cursor
     * @return list of column values
     * @throws DatabaseStateException if value extraction failed
     */
    private Object[] createFromCursor(Collection<DbAccessor> accessors, Cursor cursor) throws DatabaseStateException {
        Object[] extractedValues = new Object[accessors.size()];
        int i = 0;
        for (DbAccessor accessor : accessors) {
            SimpleType accessorType = accessor.getType();
            Object extractedValue = extract(cursor, i, accessorType);
            extractedValues[i] = extractedValue;
            ++i;
        }

        return extractedValues;
    }

    /**
     * Extract value from cursor.
     *
     * @param cursor       cursor to extract from
     * @param index        index of value in cursor
     * @param accessorType type to cast to
     * @return extracted value cased to accessorType
     * @throws DatabaseStateException if value can't be extracted
     */
    private Object extract(Cursor cursor, int index, SimpleType accessorType) throws DatabaseStateException {
        try {
            return accessorType.extract(cursor, index);
        } catch (IllegalArgumentException ex) {
            throw new DatabaseStateException("Cannot extract database value %s", ex, ex.getMessage());
        }
    }

    /**
     * Create and return column names created from given accessors.
     *
     * @param accessors accessors
     * @return array of column names in the same order as accessors
     */
    protected String[] getColumns(Collection<DbAccessor> accessors) {
        String[] columns = new String[accessors.size()];
        int i = 0;
        for (DbAccessor accessor : accessors) {
            columns[i++] = accessor.getName();
        }

        return columns;
    }
}
