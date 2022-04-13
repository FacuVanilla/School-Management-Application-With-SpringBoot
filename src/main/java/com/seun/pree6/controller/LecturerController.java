package com.seun.pree6.controller;

import com.seun.pree6.entity.Course;
import com.seun.pree6.entity.Grade;
import com.seun.pree6.entity.Lecturer;
import com.seun.pree6.entity.Student;
import com.seun.pree6.other.CourseSelector;
import com.seun.pree6.repository.CourseRepository;
import com.seun.pree6.repository.GradeRepository;
import com.seun.pree6.repository.LecturerRepository;
import com.seun.pree6.session.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/lecturers")
public class LecturerController {

    private String portalType = "Lecturer";

    @Autowired
    LecturerRepository lecturerRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    CourseSelector courseSelector;

    @GetMapping
    public String mainPage(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Lecturer currentLecturer = lecturerRepository.findById(SessionHandler.getSession().getUserId()).get();
        model.addAttribute("user", currentLecturer);
        model.addAttribute("notStarted", courseSelector.getCoursesNotStarted(currentLecturer));
        model.addAttribute("inProgress", courseSelector.getCoursesInProgress(currentLecturer));
        model.addAttribute("completed", courseSelector.getCoursesCompleted(currentLecturer));
        return "lecturer-portal/lecturer-page-main.html";
    }

    @GetMapping(value="/ManageAccount")
    public String manageTeacherAccount(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Lecturer lecturer = lecturerRepository.findById(SessionHandler.getSession().getUserId()).get();
        model.addAttribute("lecturer", lecturer);
        return "lecturer-portal/lecturer-manage-account.html";
    }

    @PostMapping(value="/changePassword")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String newPassword2) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Lecturer lecturer = lecturerRepository.findById(SessionHandler.getSession().getUserId()).get();

        if ( !(oldPassword.equals(lecturer.getPassword())) || !(newPassword.equals(newPassword2)) ) {
            return "lecturer-portal/lecturer-change-password-failed.html";
        } else {
            lecturer.setPassword(newPassword);
            lecturerRepository.save(lecturer);
            return "lecturer-portal/lecturer-change-password-succeeded.html";
        }
    }

    @GetMapping(value="createNewCourse")
    public String createNewClass(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        model.addAttribute("course", new Course());
        return"lecturer-portal/lecturer-create-new-course.html";
    }

    @PostMapping(value="/saveCourse")
    public String saveCourse(Course newCourse) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Lecturer lecturer = lecturerRepository.findById(SessionHandler.getSession().getUserId()).get();
        newCourse.setTheLecturer(lecturer);
        courseRepository.save(newCourse);
        return"lecturer-portal/lecturer-course-saved.html";
    }

    @GetMapping(value="/manageCourses")
    public String manageCourses(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Lecturer lecturer = lecturerRepository.findById(SessionHandler.getSession().getUserId()).get();
        model.addAttribute("notStarted", courseSelector.getCoursesNotStarted(lecturer));
        model.addAttribute("inProgress", courseSelector.getCoursesInProgress(lecturer));
        model.addAttribute("completed", courseSelector.getCoursesCompleted(lecturer));
        return"lecturer-portal/lecturer-manage-courses.html";
    }

    @GetMapping(value="/manageTheCourse")
    public String manageTheCourse(@RequestParam Long chosenCourse, Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Course course = courseRepository.findById(chosenCourse).get();
        model.addAttribute("chosenCourse", course);

        List<Student> enrolledStudents = course.getStudents();

        List<Grade> grades = new ArrayList<>();

        for (Student student : enrolledStudents) {
            for (Grade grade : student.getGrades()) {
                if (grade.getTheStudent() == student && grade.getTheCourse().getCourseId() == chosenCourse) {
                    grades.add(grade);
                }
            }
        }

        model.addAttribute("grades", grades);
        return "lecturer-portal/lecturer-manage-chosen-course.html";
    }

    @PostMapping(value="/setGrade")
    public String setGrade(@RequestParam Long gradeId, @RequestParam Double courseGrade, @RequestParam Long chosenCourse) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Grade grade = gradeRepository.findById(gradeId).get();
        grade.setGrade(courseGrade);
        gradeRepository.save(grade);
        return "redirect:/lecturers/manageTheCourse?chosenCourse=" + chosenCourse;
    }



    @PostMapping(value="/saveCourseStage")
    public String saveCourseStage(@RequestParam String updateStage, @RequestParam Long chosenCourse) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Course course = courseRepository.findById(chosenCourse).get();
        course.setStage(updateStage);
        courseRepository.save(course);
        return "redirect:/lecturers/manageTheCourse?chosenCourse=" + chosenCourse;
    }
}
