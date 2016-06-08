package org.androidorm.db;

import android.database.sqlite.SQLiteDatabase;
import org.androidorm.exceptions.TransactionException;

/**
 * Provides connection and transaction for sqlite database.
 */
public class DatabaseConnectionProvider {

    private final String dbName;

    private SQLiteDatabase db;

    /**
     * Constructor.
     *
     * @param dbName database name
     */
    public DatabaseConnectionProvider(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Return database name.
     *
     * @return database name
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Open database connection.
     *
     * @throws TransactionException if can't open database connection
     */
    public void openDatabase() throws TransactionException {
        verifyDatabaseClosed();
        this.db = SQLiteDatabase.openDatabase(dbName, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Close database connection. If any transaction is opened it will be rolled back.
     *
     * @throws TransactionException if can't close database connection
     */
    public void closeDatabase() throws TransactionException {
        verifyDatabaseOpened();
        if (isTransactionOpened()) {
            rollbackTransaction();
        }
        db.close();
        db = null;
    }

    /**
     * Return if connection to database is established.
     *
     * @return if connection to database is established
     */
    public boolean isOpened() {
        return db != null;
    }

    /**
     * Return current database connection.
     *
     * @return curent database connection
     * @throws TransactionException if there is no connection opened
     */
    public SQLiteDatabase getConnection() throws TransactionException {
        verifyDatabaseOpened();
        return db;
    }

    /**
     * Open transaction.
     *
     * @throws TransactionException if transaction is already opened, or database connection is not present
     */
    public void openTransaction() throws TransactionException {
        verifyDatabaseOpened();
        verifyTransactionClosed();
        db.beginTransaction();
    }

    /**
     * Commit transaction.
     *
     * @throws TransactionException if transaction is not opened or database connection is not present
     */
    public void commitTransaction() throws TransactionException {
        verifyDatabaseOpened();
        verifyTransactionOpened();
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Rollback transaction.
     *
     * @throws TransactionException if transaction is not opened or database conntection is not present
     */
    public void rollbackTransaction() throws TransactionException {
        verifyDatabaseOpened();
        verifyTransactionOpened();
        db.endTransaction();
    }

    /**
     * Return if transaction is opened.
     *
     * @return if transaction is opened
     */
    public boolean isTransactionOpened() {
        return isOpened() && db.inTransaction();
    }

    /**
     * Verify if transaction is opened.
     *
     * @throws TransactionException if transaction is closed
     */
    private void verifyTransactionOpened() throws TransactionException {
        if (!isTransactionOpened()) {
            throw new TransactionException("Transaction closed");
        }
    }

    /**
     * Verify if transaction is closed.
     *
     * @throws TransactionException if transaction is opened
     */
    private void verifyTransactionClosed() throws TransactionException {
        if (isTransactionOpened()) {
            throw new TransactionException("Transaction already opened");
        }
    }

    /**
     * Verify if database connection is closed.
     *
     * @throws TransactionException if database connection is opened
     */
    private void verifyDatabaseClosed() throws TransactionException {
        if (isOpened()) {
            throw new TransactionException("Database connection already opened");
        }
    }

    /**
     * Verify if database connection is opened.
     *
     * @throws TransactionException if database connection is closed
     */
    private void verifyDatabaseOpened() throws TransactionException {
        if (!isOpened()) {
            throw new TransactionException("Database connection closed");
        }
    }
}
