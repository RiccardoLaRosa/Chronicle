package it.aulab.chronicle.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.chronicle.dtos.ArticleDto;
import it.aulab.chronicle.dtos.UserDto;
import it.aulab.chronicle.models.User;
import it.aulab.chronicle.repositories.CareerRequestRepository;
import it.aulab.chronicle.services.ArticleService;
import it.aulab.chronicle.services.CategoryService;
import it.aulab.chronicle.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;



@Controller
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CareerRequestRepository careerRequestRepository;

    /* Rotta Home */
    @GetMapping("/")
    public String home(Model viewModel) {

        List<ArticleDto> articles = articleService.readAll();

        //articoli in ordine cronologico inverso
        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());

        List<ArticleDto> lastFourArticle = articles.stream().limit(3).collect(Collectors.toList());

        viewModel.addAttribute("articles", lastFourArticle);
        viewModel.addAttribute("categories", categoryService.readAll());

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

        /* Rotta per la ricerca degli articoli tramite l'utente */

        @GetMapping("/search/{id}")
        public String userArticleSearch(@PathVariable("id") Long id, Model viewModel) {

            User user = userService.read(id);

            viewModel.addAttribute("title", "Tutti gli articoli trovati per utente: " + user.getUsername());

            List<ArticleDto> articles = articleService.searchByUser(user);

            viewModel.addAttribute("articles", articles);

            return "article/articles";
        }

        /* Rotta per la dashboard */
        @GetMapping("/admin/dashboard")
        public String adminDashboard(Model viewModel) {
            viewModel.addAttribute("title", "Richieste ricevute:");
            viewModel.addAttribute("requests", careerRequestRepository.findByIsCheckedFalse());
            viewModel.addAttribute("categories", categoryService.readAll());
            return "admin/dashboard";
        }
        
        
}
