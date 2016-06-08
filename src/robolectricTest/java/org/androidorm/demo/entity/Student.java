package org.androidorm.demo.entity;

import org.androidorm.annotations.Column;
import org.androidorm.annotations.GeneratedValue;
import org.androidorm.annotations.Id;
import org.androidorm.annotations.ManyToOne;
import org.androidorm.annotations.Table;

import java.util.Date;

// Table annotation is not mandatory. By default, table will have the same name, as a class.
@Table(name = "T_STUDENT")
public class Student {

    // All entities must have an id. In this case, it is long.
    @Id
    // Id will not be provided by a developer, but generated automatically.
    @GeneratedValue
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // Column annotation is optional, by default all available field will be mapped.
    private Date birthDate;

    // Many to one relation.
    @ManyToOne
    private Major major;

    // Class must have a no-arg constructor. No need to be public though.
    protected Student() {
    }

    public Student(String firstName, String lastName, Date birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        setBirthDate(birthDate);
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate == null ? null : new Date(birthDate.getTime());
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate == null ? null : new Date(birthDate.getTime());
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major newMajor) {
        if (newMajor == this.major) {
            return;
        }

        Major oldMajor = this.major;
        if (oldMajor != null) {
            oldMajor.removeStudent(this);
        }

        this.major = newMajor;
        if (newMajor != null) {
            newMajor.addStudent(this);
        }
    }
}
