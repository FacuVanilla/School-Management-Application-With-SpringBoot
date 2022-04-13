package com.seun.pree6.controller;

import java.util.List;

import com.seun.pree6.entity.Admin;
import com.seun.pree6.repository.AdminRepository;
import com.seun.pree6.session.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;





@Controller
@RequestMapping(value="/admin")
public class AdminAdminManagement {

    private String portalType="ADMIN";

    @Autowired
    AdminRepository adminRepository;

    @GetMapping(value="/ManageAdmins")
    public String manageAdmins(Model model) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        model.addAttribute("admin", new Admin());
        List<Admin> allAdmins = (List<Admin>) adminRepository.findAll();
        model.addAttribute("allAdmins", allAdmins);
        return "admin-portal/admin-manage-admins.html";
    }

    @PostMapping(value="/saveAdmin")
    public String saveAdmin(Admin admin, @RequestParam String password) {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        admin.setPassword(password);
        adminRepository.save(admin);
        return "redirect:/admin/adminSaved";
    }

    @GetMapping(value="/adminSaved")
    public String adminSaved() {
        if (SessionHandler.accessNotAllowed(portalType)) return SessionHandler.redirect();
        return "admin-portal/admin-admin-saved.html";
    }
}
