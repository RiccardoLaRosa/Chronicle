package it.aulab.chronicle.dtos;

import java.time.LocalDate;

import it.aulab.chronicle.models.Category;
import it.aulab.chronicle.models.Image;
import it.aulab.chronicle.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {
    
    private Long id;
    private String title;
    private String subtitle;
    private String body;
    private LocalDate publishDate;
    private Boolean isAccepted;
    User user;
    Category category;
    private Image image;

}
