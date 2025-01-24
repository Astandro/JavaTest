package com.astandro.library.entity;

import com.astandro.library.repository.entity.Article;
import com.astandro.library.repository.entity.User;
import org.junit.jupiter.api.Test;

public class ArticleTest {
    @Test
    void testArticleEntity() {
        Article article = new Article();

        article.setTitle("Title");
        article.setContent("Content");
        article.setAuthorId(1L);
        article.setId(1L);

        assert article.getTitle().equals("Title");
        assert article.getContent().equals("Content");
        assert article.getAuthorId() == 1L;
        assert article.getId() == 1L;
        assert article.getCreatedAt() == null;
        assert article.getUpdatedAt() == null;
    }
}
