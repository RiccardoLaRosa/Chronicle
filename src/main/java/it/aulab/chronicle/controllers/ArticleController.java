package it.aulab.chronicle.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.chronicle.models.Article;
import it.aulab.chronicle.services.ArticleService;
import it.aulab.chronicle.services.CategoryService;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    CategoryService categoryService;
    

    /* Rotta per la creazione di un articolo */
    @GetMapping("create")
    public String articleCreate(Model viewModel) {
        viewModel.addAttribute("title", "Crea un Articolo");
        viewModel.addAttribute("article", new Article());
        viewModel.addAttribute("categories", categoryService.readAll());
        return "article/create";
    }

    /* Rotta per lo store di un articolo */
    @PostMapping
    public String articleStore(@Valid @ModelAttribute("article") Article article, //Spring controlla TUTTE le annotazioni che hai nel model
                                BindingResult result, //errori della validazione
                                RedirectAttributes redirectAttributes, //messaggi dopo redirect
                                Principal principal,    //utente loggato
                                MultipartFile file,     //file caricato dal form (immagine)
                                Model viewModel) {      //per mandare dati alla view
        //Controllo degli errori
        if (result.hasErrors()) {
            viewModel.addAttribute("title", "Crea un Articolo");
            viewModel.addAttribute("article", article);
            viewModel.addAttribute("categories", categoryService.readAll());
            redirectAttributes.addFlashAttribute("errorMessage", "Articolo  non Salvato");
            return "article/create";
        }

        articleService.create(article, principal, file);
        redirectAttributes.addFlashAttribute("successMessage", "Articolo Salvato Correttamente");
        
        return "redirect:/";
    }
    
    
}
