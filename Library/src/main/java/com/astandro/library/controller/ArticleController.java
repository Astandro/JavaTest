package com.astandro.library.controller;

import com.astandro.library.repository.entity.Article;
import com.astandro.library.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@Tag(name = "Articles", description = "Operations related to articles CRUD")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @Operation(summary = "Create article")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or hasRole('EDITOR') or hasRole('CONTRIBUTOR')")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article createdArticle = articleService.createArticle(article);
        return ResponseEntity.ok(createdArticle);
    }

    @Cacheable(value = "articles", key = "#id")
    @GetMapping("/{id}")
    @Operation(summary = "Get article by id")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or hasRole('EDITOR') or hasRole('CONTRIBUTOR') or hasRole('VIEWER')")
    public Article getArticleById(@PathVariable Long id) {
        System.out.println("Fetching article from database for article with id " + id);
        return articleService.getArticleById(id).orElse(null);
    }

    @Cacheable(value = "articles", key = "'all'")
    @GetMapping
    @Operation(summary = "Get all articles")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or hasRole('EDITOR') or hasRole('CONTRIBUTOR') or hasRole('VIEWER')")
    public List<Article> getAllArticles() {
        System.out.println("Fetching article from database for all articles");
        return articleService.getAllArticles();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an article")
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') or (hasRole('EDITOR') or hasRole('CONTRIBUTOR')) and @articleServiceImpl.isOwnedByCurrentUser(#id, authentication.name))")
    public Article updateArticle(@PathVariable Long id, @RequestBody Article article) {
        article.setAuthorId(articleService.getArticleById(id).get().getAuthorId());
        return articleService.updateArticle(id, article);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an article")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or (hasRole('EDITOR') and @articleServiceImpl.isOwnedByCurrentUser(#id, authentication.name))")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
