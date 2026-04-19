package it.aulab.chronicle.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aulab.chronicle.dtos.CategoryDto;
import it.aulab.chronicle.models.Category;
import it.aulab.chronicle.services.ArticleService;
import it.aulab.chronicle.services.CategoryService;



@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ModelMapper modelMapper;
    

    /* Rotta per la ricerca degli articoli in base alla categoria */
    @GetMapping("search/{id}")
    public String searchByCategory(@PathVariable("id") Long id, Model viewModel) {

        CategoryDto category = categoryService.read(id);

        viewModel.addAttribute("title", "Tutti gli articoli della Categoria: "+ category.getName());
        viewModel.addAttribute("articles", articleService.searchByCategory(modelMapper.map(category, Category.class)));

        return "article/articles";
    }

    
    /* Rotta per la creazione di una categoria */
    @GetMapping("create")
    public String categoryCreate(Model viewModel) {
        viewModel.addAttribute("Title", "Crea una categoria");
        viewModel.addAttribute("category", new Category());
        return "category/create";
    }
    

    
}
