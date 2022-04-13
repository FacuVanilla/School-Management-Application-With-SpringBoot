package com.seun.pree6.other;

import com.seun.pree6.entity.Course;
import com.seun.pree6.entity.Lecturer;
import com.seun.pree6.entity.Student;
import com.seun.pree6.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CourseSelector {

    @Autowired
    CourseRepository courseRepository;

    public CourseSelector() {

    }

    public List<Course> getCoursesNotStarted(Lecturer lecturer){
        List<Course> coursesNotStarted = new ArrayList<>();
        for (Course course : courseRepository.findAll()) {
            if (course.getTheLecturer().getLecturerId() == lecturer.getLecturerId()
                    && course.getStage().equals("NOTSTARTED")) coursesNotStarted.add(course);
        }
        return coursesNotStarted;
    }

    public List<Course> getCoursesInProgress(Lecturer lecturer){
        List<Course> coursesInProgress = new ArrayList<>();
        for (Course course : courseRepository.findAll()) {
            if(course.getTheLecturer().getLecturerId() == lecturer.getLecturerId()
                    && course.getStage().equals("INPROGRESS")) coursesInProgress.add(course);
        }
        return coursesInProgress;
    }

    public List<Course> getCoursesCompleted(Lecturer lecturer){
        List<Course> coursesCompleted = new ArrayList<>();
        for (Course course : courseRepository.findAll()) {
            if (course.getTheLecturer().getLecturerId() == lecturer.getLecturerId()
                    && course.getStage().equals("COMPLETED")) coursesCompleted.add(course);
        }
        return coursesCompleted;
    }

    public List<Course> getCoursesNotStarted(Student student){
        List<Course> coursesNotStarted = new ArrayList<>();
        for (Course course : student.getCourses()) {
            if (course.getStage().equals("NOTSTARTED")) coursesNotStarted.add(course);
        }
        return coursesNotStarted;
    }

    public List<Course> getCoursesInProgress(Student student){
        List<Course> coursesInProgress = new ArrayList<>();
        for (Course course : student.getCourses()) {
            if (course.getStage().equals("INPROGRESS")) coursesInProgress.add(course);
        }
        return coursesInProgress;
    }

    public List<Course> getCoursesCompleted(Student student){
        List<Course> coursesCompleted = new ArrayList<>();
        for (Course course : student.getCourses()) {
            if (course.getStage().equals("COMPLETED")) coursesCompleted.add(course);
        }
        return coursesCompleted;
    }

    public List<Course> getAvailableCourses(Student student){
        List<Course> availableCourses = new ArrayList<>();
        for (Course course : courseRepository.findAll()) {
            if(course.getStage().equals("NOTSTARTED") && course.getMaxStudents() > course.getStudents().size() &&
                    !(course.getStudents().contains(student))) {
                availableCourses.add(course);
            }
        }
        return availableCourses;
    }



}
