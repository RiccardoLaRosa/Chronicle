package it.aulab.chronicle.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.aulab.chronicle.dtos.ArticleDto;
import it.aulab.chronicle.models.Article;
import it.aulab.chronicle.models.Category;
import it.aulab.chronicle.models.User;
import it.aulab.chronicle.repositories.ArticleRepository;
import it.aulab.chronicle.repositories.UserRepository;

@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long> {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Override
    public List<ArticleDto> readAll() {
        List<ArticleDto> dtos = new ArrayList<>();
        for(Article article : articleRepository.findAll()) {
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

    @Override
    public ArticleDto read(Long key) {

        Optional<Article> optArticle = articleRepository.findById(key);

        if (optArticle.isPresent()) {
            return modelMapper.map(optArticle.get(), ArticleDto.class);
        } else {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Articolo non trovato");
        }

    }

    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile file) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = (userRepository.findById(userDetails.getId())).get();
            article.setUser(user);
        }

        article.setIsAccepted(null);

        Article savedArticle = articleRepository.save(article);

        if (!file.isEmpty()) {
            try {
                String url = imageService.saveImageOnCloud(file);
                imageService.saveImageOnDb(url, savedArticle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return modelMapper.map(savedArticle, ArticleDto.class);
    }

    /* Articoli tramite categoria */
    public List<ArticleDto> searchByCategory(Category category) {

        List<ArticleDto> dtos = new ArrayList<>();
        for(Article article : articleRepository.findByCategory(category)) {
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

    /* Articoli tramite utente */
    public List<ArticleDto> searchByUser(User user) {
        List<ArticleDto> dtos = new ArrayList<>();
        for(Article article : articleRepository.findByUser(user)) {
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

     /* Azioni Revisori */
     public void setIsAccepted(Boolean result, Long id) {
        Article article = articleRepository.findById(id).get();
        article.setIsAccepted(result);
        articleRepository.save(article);
     }

    @Override
    public ArticleDto update(Long key, Article model, MultipartFile file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Long key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}