package org.androidorm.demo;

import org.androidorm.DatabaseManager;
import org.androidorm.EntityManager;
import org.androidorm.demo.entity.Major;
import org.androidorm.demo.entity.MajorType;
import org.androidorm.demo.entity.Student;
import org.androidorm.exceptions.AndroidOrmException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SimpleDemo {

    private static final String DB_PATH = "database";

    @Before
    public void setUp() {
        new File(DB_PATH).delete();
    }

    @After
    public void tearDown() {
        new File(DB_PATH).delete();
    }

    @Test
    public void testDemo() throws AndroidOrmException {
        // Create database. Must provide database name and list of all entities
        DatabaseManager db = new DatabaseManager(DB_PATH, Student.class, Major.class);

        // Creating db schema based on mapped entities.
        // Should be done once, if database doesn't exist.
        // Unfortunatelly, developer must check it himself.
        db.createSchema();

        // EntityManager manages all the entieties
        EntityManager em = db.getEntityManager();
        em.open();
        try {
            demo(em);
        } finally {
            // EntityManager must be closed!
            em.close();
        }
    }

    private void demo(EntityManager em) throws AndroidOrmException {
        // Few students
        Date birthDate = new GregorianCalendar(1991, 3, 12).getTime();
        Student john = new Student("John", "Smith", birthDate);
        birthDate = new GregorianCalendar(1990, 6, 20).getTime();
        Student mary = new Student("Mary", "Jared", birthDate);
        birthDate = new GregorianCalendar(1991, 7, 5).getTime();
        Student rick = new Student("Rick", "Tamal", birthDate);
        birthDate = new GregorianCalendar(1989, 10, 11).getTime();
        Student tom = new Student("Tom", "Simpson", birthDate);

        // Few majors
        Major math = new Major("Mathematics", MajorType.MATEMATICS);
        math.addStudent(john);
        Major it = new Major("IT", MajorType.IT);
        it.addStudent(mary);
        it.addStudent(rick);

        // Method persist will save entity in database
        em.persist(it);
        em.persist(math);
        em.persist(john);
        em.persist(mary);
        em.persist(rick);
        em.persist(tom);

        // Method update will update entity in database
        mary.setMajor(math);
        em.update(mary);

        // In "Major" class, primary key is a name. Find methow will find in database object, with key "Mathematics"
        assertEquals(math, em.find(Major.class, "Mathematics"));

        // "Economy" Major was never created, so this should return null
        assertNull(em.find(Major.class, "Economy"));

        // Searching for students, whose name starts with 'S'
        List<Student> sStudents;
        sStudents = em.findAll(Student.class, " where last_name like \"S%\"");
        assertEquals(2, sStudents.size());
        assertTrue(sStudents.contains(tom));
        assertTrue(sStudents.contains(john));

        //Remove method will delete object from database
        em.remove(tom);

        // After deleting Toma Simpsona, only one student has name starting with 'S'
        sStudents = em.findAll(Student.class, " where last_name like \"S%\"");
        assertEquals(1, sStudents.size());
        assertTrue(sStudents.contains(john));
    }
}
