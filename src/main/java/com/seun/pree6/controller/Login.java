package com.seun.pree6.controller;

import com.seun.pree6.entity.Admin;
import com.seun.pree6.entity.Lecturer;
import com.seun.pree6.entity.Student;
import com.seun.pree6.repository.AdminRepository;
import com.seun.pree6.repository.LecturerRepository;
import com.seun.pree6.repository.StudentRepository;
import com.seun.pree6.session.Session;
import com.seun.pree6.session.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Login {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    LecturerRepository lecturerRepository;

    @Autowired
    StudentRepository studentRepository;


    @GetMapping
    public String login() {
        return "login/LoginScreen";
    }

    @GetMapping(value="/pleasetryagain")
    public String tryAgain() {
        return "login/LoginScreenUserNotFound";
    }

    @PostMapping(value="/authorisation")
    public String authorisation(@RequestParam String email, @RequestParam String password) {

        for (Student student : studentRepository.findAll()) {
            if (student.getEmail().toLowerCase().equals(email.toLowerCase()) && student.getPassword().equals(password)) {
                SessionHandler.setSession(new Session(true, "STUDENT", student.getStudentId()));
                return "redirect:/students";
            }
        }

        for (Lecturer lecturer : lecturerRepository.findAll()) {
            if (lecturer.getEmail().toLowerCase().equals(email.toLowerCase()) && lecturer.getPassword().equals(password)) {
                SessionHandler.setSession(new Session(true, "LECTURER", lecturer.getLecturerId()));
                return "redirect:/lecturers";
            }
        }

        for (Admin admin : adminRepository.findAll()) {
            if (admin.getEmail().toLowerCase().equals(email.toLowerCase()) && admin.getPassword().equals(password)) {
                SessionHandler.setSession(new Session(true, "ADMIN", admin.getAdminId()));
                return "redirect:/admin";
            }
        }

        return "redirect:/pleasetryagain";
    }

}

