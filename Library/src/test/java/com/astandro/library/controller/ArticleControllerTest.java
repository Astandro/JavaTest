package com.astandro.library.controller;

import com.astandro.library.repository.entity.Article;
import com.astandro.library.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ArticleControllerTest {

    @Mock
    private ArticleService articleService; // Mock the service layer

    @InjectMocks
    private ArticleController articleController;// Inject mocks into the controller

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
    }

    @Test
    void testGetArticleById() throws Exception {
        // Mock the service call to return a sample article
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Title");
        when(articleService.getArticleById(1L)).thenReturn(Optional.of(article));

        // Perform the GET request and check the response
        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllArticle() throws Exception {
        // Mock the service call to return a sample article
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Title");
        List<Article> articles = new ArrayList<>();
        articles.add(article);
        when(articleService.getAllArticles()).thenReturn(articles);

        // Perform the GET request and check the response
        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateArticle() throws Exception {
        // Mock the service call to return a sample article
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Title");

        when(articleService.getArticleById(1L)).thenReturn(Optional.of(article));
        when(articleService.updateArticle(1L,article)).thenReturn(new Article());

        // Perform the GET request and check the response
        String requestBody = """
                {
                    "title": "Title",
                    "id": 1,
                    "content": "Content"
                }
                """;
        mockMvc.perform(put("/api/articles/1")
                        .contentType(MediaType.APPLICATION_JSON) // This is required
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateArticle() throws Exception {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Title");

        when(articleService.createArticle(article)).thenReturn(article);

        // Perform the GET request and check the response
        String requestBody = """
                {
                    "title": "Title",
                    "id": 1,
                    "content": "Content"
                }
                """;
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON) // This is required
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteArticle() throws Exception {
        // Perform the GET request and check the response
        mockMvc.perform(delete("/api/articles/1"))
                .andExpect(status().is2xxSuccessful());
    }
}
