package com.seun.pree6.controller;

import com.seun.pree6.entity.Grade;
import com.seun.pree6.entity.Student;
import com.seun.pree6.repository.StudentRepository;
import com.seun.pree6.session.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value="/admin")
public class AdminStudentManagement {

    private String portalType="ADMIN";

    @Autowired
    StudentRepository studentRepository;

    @GetMapping(value="/ManageStudents")
    public String manageStudents(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        model.addAttribute("student", new Student());
        return "admin-portal/admin-manage-students.html";
    }

    @PostMapping(value="/saveStudent")
    public String saveStudent(Student student, @RequestParam String password) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        student.setPassword(password);
        studentRepository.save(student);
        return "redirect:/admin/studentSaved";
    }

    @GetMapping(value="/studentSaved")
    public String studentSaved() {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        return "admin-portal/admin-student-saved.html";
    }

    @PostMapping(value="/searchStudent")
    public String searchStudent(@RequestParam String firstName, @RequestParam String lastName) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        for (Student student : studentRepository.findAll()) {
            if (student.getFirstName().toLowerCase().equals(firstName.toLowerCase()) &&
                    student.getLastName().toLowerCase().equals(lastName.toLowerCase())) {
                long studentId = student.getStudentId();
                return "redirect:/admin/foundStudent/" + studentId;
            }
        }
        return "redirect:/admin/studentNotFound";
    }

    @GetMapping(value="/foundStudent/{studentId}")
    public String foundStudent(@PathVariable long studentId, Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            return "redirect:/admin/studentNotFound";
        } else {
            model.addAttribute("student", student.get());

            List<Grade> grades = new ArrayList<>();
            for (Grade grade : student.get().getGrades()) {
                if (grade.getGrade() > 0.0) {
                    grades.add(grade);
                }
            }
            model.addAttribute("grades", grades);
        }
        return	"admin-portal/admin-single-student.html";
    }

    @GetMapping(value = "/studentNotFound")
    public String studentNotFound() {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        return "admin-portal/admin-student-not-found.html";
    }

    @PostMapping(value = "/resetStudentPassword")
    public String resetPassword(@RequestParam String password, @RequestParam long studentId) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        Student student = studentRepository.findById(studentId).get();
        student.setPassword(password);
        studentRepository.save(student);
        return "redirect:/admin/studentPasswordReset";
    }

    @GetMapping(value = "/studentPasswordReset")
    public String passwordIsReset() {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        return "admin-portal/admin-student-password-reset.html";
    }

    @GetMapping(value="/displayAllStudents")
    public String displayAllStudents(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        List<Student> allStudents = (List<Student>) studentRepository.findAll();
        model.addAttribute("allStudents", allStudents);
        return "admin-portal/admin-all-students.html";
    }
}

