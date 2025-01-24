package com.astandro.library.repository;

import com.astandro.library.repository.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    // Custom query methods (if needed) can be added here
}
