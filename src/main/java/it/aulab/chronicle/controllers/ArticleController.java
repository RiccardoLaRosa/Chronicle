package it.aulab.chronicle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aulab.chronicle.models.Article;
import it.aulab.chronicle.services.CategoryService;


@Controller
@RequestMapping("/articles")
public class ArticleController {

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
    
}
