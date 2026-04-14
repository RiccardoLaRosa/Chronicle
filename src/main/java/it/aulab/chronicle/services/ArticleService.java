package it.aulab.chronicle.services;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.aulab.chronicle.dtos.ArticleDto;
import it.aulab.chronicle.models.Article;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArticleDto read(Long key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile file) {

        String url = "";
        //utente loggato ORA
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            //cast
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            //ricerca nel db
            User user = (userRepository.findById(userDetails.getId())).get();
            //associazione allo user
            article.setUser(user);
        }
        
        if (!file.isEmpty()) {
            try {
                CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
                url = futureUrl.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //salvataggio
        ArticleDto dto = modelMapper.map(articleRepository.save(article), ArticleDto.class);

        if (!file.isEmpty()) {
            imageService.saveImageOnDb(url, article);
            
        }
            
        return dto;

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
