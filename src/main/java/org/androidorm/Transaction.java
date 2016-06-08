package org.androidorm;

import org.androidorm.db.DatabaseConnectionProvider;
import org.androidorm.db.PersistenceUnit;
import org.androidorm.exceptions.TransactionException;

/**
 * Class representing sql transaction.
 */
public class Transaction {

    private PersistenceUnit persisteneUnitSnapshot;

    private final PersistenceUnit persistenceUnit;

    private final DatabaseConnectionProvider databaseConnectionProvider;

    /**
     * Constructor.
     *
     * @param persistenceUnit            persistence unit
     * @param databaseConnectionProvider database connection provider
     */
    protected Transaction(PersistenceUnit persistenceUnit, DatabaseConnectionProvider databaseConnectionProvider) {
        this.persistenceUnit = persistenceUnit;
        this.databaseConnectionProvider = databaseConnectionProvider;
    }

    /**
     * Open transaction.
     *
     * @throws TransactionException if transaction can't be opened
     */
    public void open() throws TransactionException {
        databaseConnectionProvider.openTransaction();
        this.persisteneUnitSnapshot = new PersistenceUnit(persistenceUnit);
    }

    /**
     * Commit transaction.
     *
     * @throws TransactionException if transaction can't e committed
     */
    public void commit() throws TransactionException {
        databaseConnectionProvider.commitTransaction();
    }

    /**
     * Rollback transaction.
     *
     * @throws TransactionException if transaction can't be rollbacked
     */
    public void rollback() throws TransactionException {
        databaseConnectionProvider.rollbackTransaction();
        persistenceUnit.setState(persisteneUnitSnapshot);
    }

    /**
     * Return if transaction is opened.
     *
     * @return if transaction is opened
     */
    public boolean isOpened() {
        return databaseConnectionProvider.isTransactionOpened();
    }
}
