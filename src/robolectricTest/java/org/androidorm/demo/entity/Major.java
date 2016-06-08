package org.androidorm.demo.entity;

import edu.emory.mathcs.backport.java.util.Collections;
import org.androidorm.annotations.EnumType;
import org.androidorm.annotations.Enumerated;
import org.androidorm.annotations.Id;
import org.androidorm.annotations.OneToMany;

import java.util.ArrayList;
import java.util.List;

public class Major {

    // Id is not generated - it is provided by the developer.
    @Id
    private String name;

    // Enums can be mapped as well.
    @Enumerated(EnumType.STRING)
    private MajorType type;

    // One to many relation.
    @OneToMany
    private List<Student> students = new ArrayList<Student>();

    protected Major() {
    }

    public Major(String name, MajorType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MajorType getType() {
        return type;
    }

    public void setType(MajorType type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public void addStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
            student.setMajor(this);
        }
    }

    public void removeStudent(Student student) {
        if (students.contains(student)) {
            students.remove(student);
            student.setMajor(null);
        }
    }
}
