package it.aulab.chronicle.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles")
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable= false, length=100)
    @NotEmpty
    @Size(max=100)
    private String title;

    @Column(nullable= false, length=500)
    @NotEmpty
    @Size(max=500)
    private String subtitle;

    @Column(nullable= false, length=10000)
    @NotEmpty
    @Size(max=5000)
    private String body;

    @Column(nullable= false, length=8)
    @NotNull
    private LocalDate publishDate;

    @Column(nullable= true)
    private Boolean isAccepted;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"articles"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"articles"})
    private Category category;

    @OneToOne(mappedBy="article")
    @JsonIgnoreProperties({"articles"})
    private Image image;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Article)) return false;

        Article article = (Article) obj;

        return title.equals(article.getTitle()) &&
            body.equals(article.getBody()) &&
            publishDate.equals(article.getPublishDate()) &&
            category.getName().equals(article.getCategory().getName()) &&
            (image == null ? article.getImage() == null
                            : image.getPath().equals(article.getImage().getPath()));
    }

    

}
