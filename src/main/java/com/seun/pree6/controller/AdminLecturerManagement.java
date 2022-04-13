package com.seun.pree6.controller;

import com.seun.pree6.entity.Course;
import com.seun.pree6.entity.Grade;
import com.seun.pree6.entity.Lecturer;
import com.seun.pree6.repository.GradeRepository;
import com.seun.pree6.repository.LecturerRepository;
import com.seun.pree6.session.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value="/admin")
public class AdminLecturerManagement {

    private String portalType="ADMIN";

    @Autowired
    LecturerRepository lecturerRepository;

    @Autowired
    GradeRepository gradeRepository;

    @GetMapping(value="/ManageLecturers")
    public String manageLecturers(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        model.addAttribute("lecturer", new Lecturer());
        return "admin-portal/admin-manage-lecturers.html";
    }

    @PostMapping(value="saveLecturer")
    public String saveTeacher(Lecturer lecturer, @RequestParam String password) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        lecturer.setPassword(password);
        lecturerRepository.save(lecturer);
        return "redirect:/admin/lecturerSaved";
    }

    @GetMapping(value="/lecturerSaved")
    public String teacherSaved() {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        return "admin-portal/admin-lecturer-saved.html";
    }

    @PostMapping(value="/searchLecturer")
    public String searchTeacher(@RequestParam String firstName, @RequestParam String lastName) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        for (Lecturer lecturer : lecturerRepository.findAll()) {
            if (lecturer.getFirstName().toLowerCase().equals(firstName.toLowerCase()) &&
                    lecturer.getLastName().toLowerCase().equals(lastName.toLowerCase())) {
                long lecturerId = lecturer.getLecturerId();
                return "redirect:/admin/foundLecturer/" + lecturerId;
            }
        }
        return "redirect:/admin/LecturerNotFound";
    }

    @GetMapping(value="/LecturerNotFound")
    public String teacherNotFound() {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        return "admin-portal/admin-lecturer-not-found.html";
    }

    @GetMapping(value="/foundLecturer/{lecturerId}")
    public String foundLecturer(@PathVariable long lecturerId, Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Optional<Lecturer> lecturer = lecturerRepository.findById(lecturerId);
        if (lecturer.isEmpty()) {
            return "redirect:/admin/LecturerNotFound";
        } else {
            model.addAttribute("lecturer", lecturer.get());
            List<Course> courses = lecturer.get().getCourses();
            Map<String, Double> averageGrades = new HashMap<String, Double>();

            for(Course course : courses) {
                double totalOfGrades = 0.0;
                double count = 0.0;
                double averageGrade = 0.0;
                for (Grade grade : gradeRepository.findAll() ) {
                    if(course.getCourseId() == grade.getTheCourse().getCourseId() && grade.getGrade() > 0.0) {
                        totalOfGrades += grade.getGrade();
                        count+=1;
                    }

                }

                averageGrade = totalOfGrades / count;

                if(averageGrade > 0.0){
                    averageGrades.put(course.getCourseName(), averageGrade);

                    System.out.println("TESTING:" + course.getCourseName() + "GRADE" + averageGrade);
                }



                model.addAttribute("averageGrades", averageGrades);
            }
        }
        return	"admin-portal/admin-single-lecturer.html";
    }

    @PostMapping(value = "/resetLecturerPassword")
    public String resetPassword(@RequestParam String password, @RequestParam long teacherId) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Lecturer lecturer = lecturerRepository.findById(teacherId).get();
        lecturer.setPassword(password);
        lecturerRepository.save(lecturer);
        return "redirect:/admin/lecturerPasswordReset";
    }

    @GetMapping(value = "/lecturerPasswordReset")
    public String passwordIsReset() {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        return "admin-portal/admin-lecturer-password-reset.html";
    }

    @GetMapping(value="/displayAllLecturers")
    public String displayAllTeachers(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        List<Lecturer> allLecturers = (List<Lecturer>) lecturerRepository.findAll();
        model.addAttribute("allLecturers", allLecturers);
        return "admin-portal/admin-all-lecturers.html";
    }
}
