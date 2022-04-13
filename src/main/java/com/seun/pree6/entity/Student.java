package com.seun.pree6.entity;


import javax.persistence.*;
import java.util.List;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long studentId;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy="theStudent")
    private List<Grade> grades;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinTable(name="student_course",
            joinColumns = @JoinColumn(name="student_id"),
            inverseJoinColumns =@JoinColumn(name="course_id"))
    private List<Course> courses;

    public Student() {

    }



    public List<Course> getCourses() {
        return courses;
    }



    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }



    public Student(String firstName, String lastName, String email, String password) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;

    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }


}
