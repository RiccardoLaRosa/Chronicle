package it.aulab.chronicle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.chronicle.dtos.UserDto;
import it.aulab.chronicle.models.User;
import it.aulab.chronicle.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;

    /* Rotta Home */
    @GetMapping("/")
    public String home() {
        return "home";
    }

     /* Rotta di Pagina Registrazione */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/register";
    }

    /* Rotta di Salvataggio Utente */
    @PostMapping("/register/save")
    public String registration(
        @Valid
        @ModelAttribute("user") UserDto userDto,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes,
        HttpServletRequest request,
        HttpServletResponse response){

            User userExist = userService.findUserByEmail(userDto.getEmail());

            if (userExist != null && userExist.getEmail() != null && !userExist.getEmail().isEmpty()) {
                result.rejectValue("email", null, "Questo email è già presente");
            }

            if (result.hasErrors()) {
                model.addAttribute("user", new UserDto());
                return "auth/register";
            }

            userService.saveUser(userDto, redirectAttributes, request, response);

            return "redirect:/register?success";
        }

        /* Rotta per il Login */
        @GetMapping("/login")
        public String login() {
            return "auth/login";
        }
    
}
