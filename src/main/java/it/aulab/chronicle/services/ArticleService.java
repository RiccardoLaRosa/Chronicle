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
import org.springframework.transaction.annotation.Transactional;
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

     public List<ArticleDto> search(String keyword) {
        List<ArticleDto> dtos = new ArrayList<>();
        for(Article article : articleRepository.search(keyword)) {
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
     }

    @Transactional
    @Override
    public ArticleDto update(Long key, Article model, MultipartFile file) {

        if (!articleRepository.existsById(key)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        model.setId(key);
        Article article = articleRepository.findById(key).get();
        model.setUser(article.getUser());

        if (!file.isEmpty()) {
            try {
                // 1. Elimina la vecchia immagine (ora sincrono)
                if (article.getImage() != null) {
                    imageService.deleteImage(article.getImage().getPath());
                }

                // 2. Salva la nuova immagine su cloud e su DB
                String url = imageService.saveImageOnCloud(file);
                imageService.saveImageOnDb(url, model);

                // 3. Ricarica l'articolo dal DB così ha l'immagine aggiornata
                model = articleRepository.findById(key).get();
                model.setIsAccepted(null);

            } catch (Exception e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore nel salvataggio dell'immagine");
            }
        } else {
            // Nessun nuovo file: mantieni l'immagine esistente
            model.setImage(article.getImage());

            if (!model.equals(article)) {
                model.setIsAccepted(null);
            } else {
                model.setIsAccepted(article.getIsAccepted());
            }
        }

        Article savedArticle = articleRepository.save(model);
        return modelMapper.map(savedArticle, ArticleDto.class);
    }

    @Override
    public void delete(Long key) {
        if (articleRepository.existsById(key)) {

            Article article = articleRepository.findById(key).get();

            try {
                String path = article.getImage().getPath();
                imageService.deleteImage(path);
            } catch (Exception e) {
                e.printStackTrace();
            }

            articleRepository.deleteById(key);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}