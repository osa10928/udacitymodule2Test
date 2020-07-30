package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller()
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView() {

        return "signup";
    }

    @PostMapping()
    public RedirectView signupUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        String signupError = null;
        RedirectView redirectView;

        if (!userService.isUserNameAvailable(user.getUsername())) {
            signupError = "Sorry, this username is already taken. Please try another";
        }

        if (signupError == null) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded <= 0) {
                signupError = "There was an internal error signing you up. Please Try again";
            }
        }

        if (signupError == null) {
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            redirectView = new RedirectView("/login", true);
        } else {
            redirectAttributes.addFlashAttribute("signupError", signupError);
            redirectView = new RedirectView("/signup", true);
        }

        return redirectView;
    }
}
