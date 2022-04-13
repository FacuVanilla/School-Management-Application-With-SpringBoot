package com.seun.pree6.controller;

import com.seun.pree6.session.SessionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Logout {

    @GetMapping(value="/logout")
    public String logout() {
        SessionHandler.getSession().setLoggedIn(false);
        SessionHandler.getSession().setUserId(-1);
        SessionHandler.getSession().setUserType("");
        return "redirect:/";
    }

}
