package com.astandro.library.service;

import com.astandro.library.repository.ArticleRepository;
import com.astandro.library.repository.entity.Article;
import com.astandro.library.repository.entity.User;
import com.astandro.library.service.UserService;
import com.astandro.library.service.impl.ArticleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateArticle() {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Test Article");

        when(articleRepository.save(article)).thenReturn(article);

        Article result = articleService.createArticle(article);

        assertNotNull(result);
        assertEquals(article.getId(), result.getId());
        assertEquals(article.getTitle(), result.getTitle());
        verify(articleRepository, times(1)).save(article);
    }

    @Test
    void testGetArticleById() {
        Article article = new Article();
        article.setId(1L);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        Optional<Article> result = articleService.getArticleById(1L);

        assertTrue(result.isPresent());
        assertEquals(article.getId(), result.get().getId());
        verify(articleRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllArticles() {
        Article article1 = new Article();
        article1.setId(1L);
        Article article2 = new Article();
        article2.setId(2L);

        List<Article> articles = Arrays.asList(article1, article2);
        when(articleRepository.findAll()).thenReturn(articles);

        List<Article> result = articleService.getAllArticles();

        assertEquals(2, result.size());
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void testUpdateArticle() {
        Article existingArticle = new Article();
        existingArticle.setId(1L);
        existingArticle.setTitle("Old Title");

        Article updatedArticle = new Article();
        updatedArticle.setTitle("New Title");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleRepository.save(existingArticle)).thenReturn(existingArticle);

        Article result = articleService.updateArticle(1L, updatedArticle);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).save(existingArticle);
    }

    @Test
    void testUpdateArticle_NotFound() {
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                articleService.updateArticle(1L, new Article()));

        assertEquals("Article not found with id 1", exception.getMessage());
        verify(articleRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteArticle() {
        doNothing().when(articleRepository).deleteById(1L);

        articleService.deleteArticle(1L);

        verify(articleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testIsOwnedByCurrentUser() {
        Article article = new Article();
        article.setId(1L);
        article.setAuthorId(2L);

        User user = new User();
        user.setId(2L);
        user.setUsername("testUser");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(userService.getUserById(2L)).thenReturn(Optional.of(user));

        boolean result = articleService.isOwnedByCurrentUser(1L, "testUser");

        assertTrue(result);
        verify(articleRepository, times(1)).findById(1L);
        verify(userService, times(1)).getUserById(2L);
    }

    @Test
    void testIsOwnedByCurrentUser_NotOwned() {
        Article article = new Article();
        article.setId(1L);
        article.setAuthorId(2L);

        User user = new User();
        user.setId(2L);
        user.setUsername("otherUser");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(userService.getUserById(2L)).thenReturn(Optional.of(user));

        boolean result = articleService.isOwnedByCurrentUser(1L, "testUser");

        assertFalse(result);
        verify(articleRepository, times(1)).findById(1L);
        verify(userService, times(1)).getUserById(2L);
    }

    @Test
    void testIsOwnedByCurrentUser_ArticleNotFound() {
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                articleService.isOwnedByCurrentUser(1L, "testUser"));

        assertEquals("Article not found", exception.getMessage());
        verify(articleRepository, times(1)).findById(1L);
    }
}
