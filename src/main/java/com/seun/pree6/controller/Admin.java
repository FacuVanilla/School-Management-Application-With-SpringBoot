package com.seun.pree6.controller;


import com.seun.pree6.entity.Grade;
import com.seun.pree6.entity.Lecturer;
import com.seun.pree6.entity.Student;
import com.seun.pree6.repository.AdminRepository;
import com.seun.pree6.repository.GradeRepository;
import com.seun.pree6.repository.LecturerRepository;
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
@RequestMapping(value="/admin")
public class Admin {

    private String portalType="ADMIN";

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    LecturerRepository lecturerRepository;

    @Autowired
    GradeRepository gradeRepository;

    @GetMapping
    public String mainPage(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        com.seun.pree6.entity.Admin currentAdmin = adminRepository.findById(SessionHandler.getSession().getUserId()).get();
        model.addAttribute("user", currentAdmin);

        List<Student> students = (List<Student>) studentRepository.findAll();
        int amountOfStudents = students.size();
        model.addAttribute("studentAmount", amountOfStudents);

        List<Lecturer> lecturers = (List<Lecturer>) lecturerRepository.findAll();
        int amountOfLecturers = lecturers.size();
        model.addAttribute("lecturerAmount", amountOfLecturers);

        List<Grade> grades = (List<Grade>) gradeRepository.findAll();
        double totalGrade = 0;
        double count = 0;
        for (Grade grade : grades) {
            if(grade.getGrade() > 0.0) {
                totalGrade += grade.getGrade();
                count += 1;
            }
        }
        double averageGrade = totalGrade / count;
        model.addAttribute("averageGrade", averageGrade);

        return "admin-portal/admin-page-main.html";
    }

    @GetMapping(value="/ManageAccount")
    public String manageAccount(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        com.seun.pree6.entity.Admin currentAdmin = adminRepository.findById(SessionHandler.getSession().getUserId()).get();
        model.addAttribute("admin", currentAdmin);
        return "admin-portal/admin-manage-account.html";
    }

    @PostMapping(value="/changePassword")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String newPassword2) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        com.seun.pree6.entity.Admin currentAdmin = adminRepository.findById(SessionHandler.getSession().getUserId()).get();

        if ( (!currentAdmin.getPassword().equals(oldPassword)) || (!newPassword.equals(newPassword2)) ){
            return "admin-portal/admin-change-password-failed.html";
        } else {
            currentAdmin.setPassword(newPassword);
            adminRepository.save(currentAdmin);
            return "admin-portal/admin-change-password-succeeded.html";
        }
    }
}
