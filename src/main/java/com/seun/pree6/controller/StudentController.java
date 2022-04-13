package com.seun.pree6.controller;

import com.seun.pree6.entity.Course;
import com.seun.pree6.entity.Grade;
import com.seun.pree6.entity.Student;
import com.seun.pree6.other.CourseSelector;
import com.seun.pree6.repository.CourseRepository;
import com.seun.pree6.repository.GradeRepository;
import com.seun.pree6.repository.StudentRepository;
import com.seun.pree6.session.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping(value="/students")
public class StudentController {
    private String portalType = "STUDENT";

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    CourseSelector courseSelector;

    @GetMapping
    public String mainPage(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Student currentStudent = studentRepository.findById(SessionHandler.getSession().getUserId()).get();
        model.addAttribute("user", currentStudent);
        model.addAttribute("notStarted", courseSelector.getCoursesNotStarted(currentStudent));
        model.addAttribute("inProgress", courseSelector.getCoursesInProgress(currentStudent));
        model.addAttribute("completed", courseSelector.getCoursesCompleted(currentStudent));
        return "student-portal/student-page-main.html";
    }

    @GetMapping(value="/ManageAccount")
    public String manageAccount(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        model.addAttribute("student", studentRepository.findById(SessionHandler.getSession().getUserId()).get());
        return"student-portal/student-manage-account.html";
    }

    @PostMapping(value="changePassword")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String newPassword2) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Student student = studentRepository.findById(SessionHandler.getSession().getUserId()).get();

        if( !(oldPassword.equals(student.getPassword())) || !(newPassword.equals(newPassword2))){
            return "student-portal/student-change-password-failed.html";
        } else {
            student.setPassword(newPassword);
            studentRepository.save(student);
            return "student-portal/student-change-password-succeeded.html";
        }
    }

    @GetMapping(value="/manageCourses")
    public String manageCourses(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Student currentStudent = studentRepository.findById(SessionHandler.getSession().getUserId()).get();
        List<Course> availableCourses = courseSelector.getAvailableCourses(currentStudent);
        List<Course>enrolledCourses = courseSelector.getCoursesNotStarted(studentRepository.findById(SessionHandler.getSession().getUserId()).get());
        model.addAttribute("availableCourses", availableCourses);
        model.addAttribute("enrolledCourses", enrolledCourses);
        return "student-portal/student-manage-courses.html";
    }

    @PostMapping(value="enrollForCourse")
    public String enrollForCourse(@RequestParam Long enrollingCourse) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();

        Course course = courseRepository.findById(enrollingCourse).get();
        Student student = studentRepository.findById(SessionHandler.getSession().getUserId()).get();

        Grade grade = new Grade(0.0);
        grade.setTheCourse(course);
        grade.setTheStudent(student);

        student.getCourses().add(course);
        studentRepository.save(student);
        courseRepository.save(course);
        gradeRepository.save(grade);

        return "redirect:/students/manageCourses";
    }

    @GetMapping(value="/displayGrades")
    public String displayGrades(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Student student = studentRepository.findById(SessionHandler.getSession().getUserId()).get();
        List<Grade> grades = student.getGrades();
        model.addAttribute("grades", grades);
        return "student-portal/student-display-grades.html";
    }
}

