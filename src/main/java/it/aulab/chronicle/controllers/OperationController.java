package it.aulab.chronicle.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.chronicle.models.CareerRequest;
import it.aulab.chronicle.models.Role;
import it.aulab.chronicle.models.User;
import it.aulab.chronicle.repositories.RoleRepository;
import it.aulab.chronicle.services.CareerRequestService;
import it.aulab.chronicle.services.UserService;



@Controller
@RequestMapping("/operations")
public class OperationController  {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CareerRequestService careerRequestService;

    /* Rotta per la creazione di una collaborazione */
    @GetMapping("/career/request")
    public String careerRequestCreate(Model viewModel) {

        viewModel.addAttribute("title", "Candidatura collaborazione");
        viewModel.addAttribute("careerRequest", new CareerRequest());

        List<Role> roles = roleRepository.findAll();

        roles.removeIf(e -> e.getName().equals("ROLE_USER"));

        viewModel.addAttribute("roles", roles);

        return "career/requestForm";
    }

    /* Rotta per il salvataggio del form */
    @PostMapping("/career/request/save")
    public String careerRequestStore(@ModelAttribute("careerRequest") CareerRequest careerRequest, Principal principal, RedirectAttributes redirectAttributes) {

        User user = userService.findUserByEmail(principal.getName());

        if (careerRequestService.AlreadyExist(user)) {
            redirectAttributes.addFlashAttribute("errorMessage","Hai già fatto una richiesta!");
            return "redirect:/";
        }
        
        careerRequestService.save(careerRequest, user);

        redirectAttributes.addFlashAttribute("successMessage", "Richiesta inviata con successo");

        return "redirect:/";
    }
    

}
