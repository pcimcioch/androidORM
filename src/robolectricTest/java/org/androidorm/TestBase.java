package org.androidorm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.androidorm.annotations.Table;
import org.androidorm.exceptions.AndroidOrmException;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class TestBase {

    private static final Map<Class<?>, String> tableBuildScript = new HashMap<Class<?>, String>();

    private static final Map<Class<?>, String> tableName = new HashMap<Class<?>, String>();

    private static final String DB_PATH = "/TestDb.sqlite";

    protected String dbPath;

    protected SQLiteDatabase db;

    protected EntityManager em;

    public static void registerTestClass(Class<?> clazz, String script) {
        Table tabAnn = clazz.getAnnotation(Table.class);
        tableBuildScript.put(clazz, script);
        tableName.put(clazz, tabAnn.name());
    }

    @Before
    public void setUp() throws Exception {
        // String path = getClass().getResource(DB_PATH).toURI().getPath();
        File dbFile = new File(DB_PATH);
        PrintWriter writer = new PrintWriter(dbFile, "UTF-8");
        writer.close();

        assertTrue(dbFile.exists());
        dbPath = dbFile.getAbsolutePath();
    }

    @After
    public void tearDown() throws Exception {
        if (db != null) {
            db.close();
        }
        if (em != null && em.isOpened()) {
            em.close();
        }
    }

    protected void init(Class<?>... classes) throws AndroidOrmException {
        db = getDatabase();
        for (Class<?> clazz : classes) {
            load(clazz);
            String query = tableBuildScript.get(clazz);
            if (query == null) {
                throw new IllegalArgumentException("Class " + clazz.getName() + " is not registered test class");
            }
            db.execSQL(query);
        }

        DatabaseManager dbManager = new DatabaseManager(dbPath, classes);
        em = dbManager.getEntityManager();
        em.open();
    }

    private SQLiteDatabase getDatabase() {
        return SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void load(Class<?> clazz) {
        try {
            Class.forName(clazz.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected Cursor getAll(Class<?> clazz, SQLiteDatabase database) {
        String table = TestBase.tableName.get(clazz);
        if (table == null) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not registered test class");
        }

        return database.query(table, null, null, null, null, null, null);
    }

    protected void assertEq(Cursor cursor, RoboTestBase... objects) {
        try {
            assertEquals(objects.length, cursor.getCount());
            cursor.moveToFirst();

            for (RoboTestBase obj : objects) {
                obj.assertEq(cursor);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    protected <T> void assertContains(Collection<T> col, T... elems) {
        assertEquals(elems.length, col.size());

        for (T elem : elems) {
            assertTrue(col.contains(elem));
        }
    }

    protected <T> void assertQueryArgs(Class<T> clazz, String sql, Object[] arguments, T... elems) throws AndroidOrmException {
        List<T> found;
        found = arguments == null || arguments.length == 0 ? em.findAll(clazz, sql) : em.findAll(clazz, sql, arguments);
        assertContains(found, elems);
    }

    protected <T> void assertQuery(Class<T> clazz, String sql, T... elems) throws AndroidOrmException {
        assertQueryArgs(clazz, sql, null, elems);
    }
}
