package com.astandro.library.service.impl;

import com.astandro.library.repository.ArticleRepository;
import com.astandro.library.repository.entity.Article;
import com.astandro.library.repository.entity.User;
import com.astandro.library.service.ArticleService;
import com.astandro.library.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository ArticleRepository;
    private final UserService UserService;

    public ArticleServiceImpl(ArticleRepository ArticleRepository, UserService UserService) {
        this.ArticleRepository = ArticleRepository;
        this.UserService = UserService;
    }

    public boolean isOwnedByCurrentUser(Long articleId, String username) {
        Article article = ArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        Long authorId = article.getAuthorId();

        Optional<User> user = UserService.getUserById(authorId);
        return user.isPresent() && user.get().getUsername().equals(username);
    }

    @Override
    public Article createArticle(Article article) {
        return ArticleRepository.save(article);
    }

    @Override
    public Optional<Article> getArticleById(Long id) {
        return ArticleRepository.findById(id);
    }

    @Override
    public List<Article> getAllArticles() {
        return ArticleRepository.findAll();
    }

    @Override
    public Article updateArticle(Long id, Article article) {
        return ArticleRepository.findById(id)
                .map(existingArticle -> {
                    existingArticle.setTitle(article.getTitle());
                    existingArticle.setAuthorId(article.getAuthorId());
                    existingArticle.setContent(article.getContent());
                    return ArticleRepository.save(existingArticle);
                })
                .orElseThrow(() -> new RuntimeException("Article not found with id " + id));
    }

    @Override
    public void deleteArticle(Long id) {
        ArticleRepository.deleteById(id);
    }
}
