package it.aulab.chronicle.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.chronicle.dtos.ArticleDto;
import it.aulab.chronicle.models.Article;
import it.aulab.chronicle.repositories.ArticleRepository;
import it.aulab.chronicle.services.ArticleService;
import it.aulab.chronicle.services.CategoryService;
import jakarta.validation.Valid;




@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    
    /* Rotta Index degli articoli */
    @GetMapping("index")
    public String articlesIndex(Model viewModel) {
        viewModel.addAttribute("title", "Esplora tutti gli articoli");
        
        List<ArticleDto> articles = new ArrayList<>();
        for (Article article : articleRepository.findByIsAcceptedTrue()){
            articles.add(modelMapper.map(article, ArticleDto.class));
        }

        //articoli in ordine cronologico inverso
        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());

        viewModel.addAttribute("articles", articles);

        return "article/articles";
    }

    @GetMapping("details/{id}")
    public String detailArticle(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Article details");
        viewModel.addAttribute("article", articleService.read(id));

        return "article/detail";
    }
    

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
    public String articleStore(@Valid @ModelAttribute("article") Article article,
                                BindingResult result, //errori della validazione
                                RedirectAttributes redirectAttributes, //messaggi dopo redirect
                                Principal principal,    //utente loggato
                                @RequestParam("file") MultipartFile file,     //file caricato dal form (immagine)
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

    /* Rotta per azioni del revisor */
    @PostMapping("accept")
    public String articleSetAccepted(@RequestParam("action") String action, @RequestParam("articleId") Long articleId, RedirectAttributes redirectAttributes) {

        if (action.equals("accept")) {
            articleService.setIsAccepted(true, articleId);
            redirectAttributes.addFlashAttribute("successMessage", "Articolo Accettato Correttamente");
        } else if(action.equals("reject")) {
            articleService.setIsAccepted(false, articleId);
            redirectAttributes.addFlashAttribute("successMessage", "Articolo Rifiutato Correttamente");
        } else {
             redirectAttributes.addFlashAttribute("successMessage", "Azione non corretta!");
        }

        return "redirect:/revisor/dashboard";
    }

    /* Rotta per la ricerca degli articoli */

    @GetMapping("search")
    public String articleSearch(@Param("keyword") String keyword, Model viewModel) {

        viewModel.addAttribute("title", "Tutti gli articoli trovati");

        List<ArticleDto> articles = articleService.search(keyword);
        List<ArticleDto> acceptedArticles = articles.stream().filter(article -> Boolean.TRUE.equals(article.getIsAccepted())).collect(Collectors.toList());

        viewModel.addAttribute("articles", acceptedArticles);

        return "article/articles";
    }
    
    
    
    
}
