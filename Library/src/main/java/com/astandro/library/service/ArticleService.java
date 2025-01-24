package com.astandro.library.service;

import com.astandro.library.repository.entity.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    Article createArticle(Article article);
    Optional<Article> getArticleById(Long id);
    List<Article> getAllArticles();
    Article updateArticle(Long id, Article article);
    void deleteArticle(Long id);
}
