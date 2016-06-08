package org.androidorm.connection;

import org.androidorm.db.DatabaseConnectionProvider;
import org.androidorm.exceptions.TransactionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DatabaseConnectionProviderTest {

    private static final String DB_PATH = "/TestDb.sqlite";

    private DatabaseConnectionProvider connProvider;

    @Before
    public void setUp() throws Exception {
        File dbFile = new File(DB_PATH);
        PrintWriter writer = new PrintWriter(dbFile, "UTF-8");
        writer.close();

        assertTrue(dbFile.exists());
        String dbPath = dbFile.getAbsolutePath();
        connProvider = new DatabaseConnectionProvider(dbPath);
    }

    @After
    public void tearDown() throws TransactionException {
        if (connProvider != null && connProvider.isTransactionOpened()) {
            connProvider.rollbackTransaction();
        }

        if (connProvider != null && connProvider.isOpened()) {
            connProvider.closeDatabase();
        }
    }

    @Test
    public void testOpenClose() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
    }

    @Test
    public void testOpenTwice() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        try {
            connProvider.openDatabase();
            fail();
        } catch (TransactionException ex) {
            // ok
        }
        checkState(true, false);
    }

    @Test
    public void testReopen() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
    }

    @Test
    public void testCloseTwice() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
        try {
            connProvider.closeDatabase();
            fail();
        } catch (TransactionException ex) {
            // ok
        }
        checkState(false, false);
    }

    @Test
    public void testCloseWithoutOpen() {
        checkState(false, false);
        try {
            connProvider.closeDatabase();
            fail();
        } catch (TransactionException ex) {
            // ok
        }
        checkState(false, false);
    }

    @Test
    public void testOpenCommitTransaction() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        connProvider.openTransaction();
        checkState(true, true);
        connProvider.commitTransaction();
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
    }

    @Test
    public void testOpenRollbackTransaction() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        connProvider.openTransaction();
        checkState(true, true);
        connProvider.rollbackTransaction();
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
    }

    @Test
    public void testOpenTransactionTwice() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        connProvider.openTransaction();
        checkState(true, true);
        try {
            connProvider.openTransaction();
            fail();
        } catch (TransactionException ex) {
            // ok
        }
        checkState(true, true);
        connProvider.commitTransaction();
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
    }

    @Test
    public void testCloseTransactionTwice() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        connProvider.openTransaction();
        checkState(true, true);
        connProvider.commitTransaction();
        checkState(true, false);
        try {
            connProvider.commitTransaction();
            fail();
        } catch (TransactionException ex) {
            // ok
        }
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
    }

    @Test
    public void testReopenTransaction() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        connProvider.openTransaction();
        checkState(true, true);
        connProvider.commitTransaction();
        checkState(true, false);
        connProvider.openTransaction();
        checkState(true, true);
        connProvider.rollbackTransaction();
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
    }

    @Test
    public void testOpenTransactionWithotDb() {
        checkState(false, false);
        try {
            connProvider.openTransaction();
            fail();
        } catch (TransactionException ex) {
            // ok
        }
        checkState(false, false);
    }

    @Test
    public void testCommitTransactionWithotDb() {
        checkState(false, false);
        try {
            connProvider.commitTransaction();
            fail();
        } catch (TransactionException ex) {
            // ok
        }
        checkState(false, false);
    }

    @Test
    public void testRollbackTransactionWithotDb() {
        checkState(false, false);
        try {
            connProvider.rollbackTransaction();
            fail();
        } catch (TransactionException ex) {
            // ok
        }
        checkState(false, false);
    }

    @Test
    public void testCommitTransactionWithotTransaction() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        try {
            connProvider.commitTransaction();
            fail();
        } catch (TransactionException ex) {
            // ok
        }
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
    }

    @Test
    public void testRollbackTransactionWithotTransaction() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        try {
            connProvider.rollbackTransaction();
            fail();
        } catch (TransactionException ex) {
            // ok
        }
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
    }

    @Test
    public void closeDbWithOpenedTransaction() throws TransactionException {
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        connProvider.openTransaction();
        checkState(true, true);
        connProvider.closeDatabase();
        checkState(false, false);
        connProvider.openDatabase();
        checkState(true, false);
        connProvider.closeDatabase();
        checkState(false, false);
    }

    private void checkState(boolean dbOpen, boolean txOpen) {
        assertEquals(dbOpen, connProvider.isOpened());
        assertEquals(txOpen, connProvider.isTransactionOpened());
    }
}
