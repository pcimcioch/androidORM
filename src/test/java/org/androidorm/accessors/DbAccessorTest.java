package org.androidorm.accessors;

import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.MappingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DbAccessorTest {

    @Test
    public void testDefaultConstructorTest() {
        DbAccessor field = new DbMockAccessor();

        assertEquals(255, field.getLength());
        assertEquals(false, field.isId());
        assertEquals(true, field.isNullable());
        assertEquals(false, field.isUnique());
    }

    @Test
    public void testSetName_toUpperCaseTest() throws AndroidOrmException {
        DbAccessor field = new DbMockAccessor();

        field.setName("name");
        assertEquals("NAME", field.getName());

        field.setName("NaMe");
        assertEquals("NAME", field.getName());

        field.setName("NAME");
        assertEquals("NAME", field.getName());

        field.setName("name1");
        assertEquals("NAME1", field.getName());

        field.setName("_nAme_1");
        assertEquals("_NAME_1", field.getName());
    }

    @Test
    public void testSetName_incorectValueTest() {
        DbAccessor field = new DbMockAccessor();

        try {
            field.setName("name v");
            fail();
        } catch (MappingException ex) {
            // expected
        }

        try {
            field.setName("name-1");
            fail();
        } catch (MappingException ex) {
            // expected
        }

        try {
            field.setName("1name");
            fail();
        } catch (MappingException ex) {
            // expected
        }

        try {
            field.setName("name?");
            fail();
        } catch (MappingException ex) {
            // expected
        }

        try {
            field.setName("?name");
            fail();
        } catch (MappingException ex) {
            // expected
        }
    }

    @Test
    public void testDontSetEmptyName() throws MappingException {
        DbAccessor field = new DbMockAccessor();

        field.setName("fooName");
        assertEquals("FOONAME", field.getName());

        field.setName("");
        assertEquals("FOONAME", field.getName());

        field.setName(null);
        assertEquals("FOONAME", field.getName());

        field.setName("BarName");
        assertEquals("BARNAME", field.getName());
    }
}
