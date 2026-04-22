package it.aulab.chronicle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.aulab.chronicle.models.Article;
import it.aulab.chronicle.models.Category;
import it.aulab.chronicle.models.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByCategory(Category category);
    List<Article> findByUser(User user);
    List<Article> findByIsAcceptedTrue();
    List<Article> findByIsAcceptedFalse();
    List<Article> findByIsAcceptedIsNull();

    @Query("SELECT a FROM Article a WHERE " +
    "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(a.subtitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(a.body) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(a.user.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(a.category.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Article> search(@Param("searchTerm") String searchTerm);
}

