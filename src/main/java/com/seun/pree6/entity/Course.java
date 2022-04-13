package com.seun.pree6.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long courseId;

    private String courseName;
    private String stage;
    private Date startDate;
    private Date endDate;
    private int maxStudents;

    @OneToMany(mappedBy="theCourse")
    List<Grade> grades;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name="lecturer_Id")
    private Lecturer theLecturer;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinTable(name="student_course",
            joinColumns = @JoinColumn(name="course_id"),
            inverseJoinColumns =@JoinColumn(name="student_id"))
    private List<Student> students;

    public Course() {

    }

    public List<Student> getStudents() {
        return students;
    }



    public void setStudents(List<Student> students) {
        this.students = students;
    }



    public Course(String courseName, String stage, Date startDate, Date endDate, int maxStudents) {
        super();
        this.courseName = courseName;
        this.stage = stage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxStudents = maxStudents;
    }


    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Lecturer getTheLecturer() {
        return theLecturer;
    }

    public void setTheLecturer(Lecturer theLecturer) {
        this.theLecturer = theLecturer;
    }


}
