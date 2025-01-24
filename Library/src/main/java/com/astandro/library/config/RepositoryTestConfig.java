package com.astandro.library.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryTestConfig {

//    @Bean
//    public CommandLineRunner testRepositories(BookRepository bookRepository, UserRepository userRepository) {
//        return args -> {
//            // Test saving a book
//            Book book = new Book();
//            book.setTitle("Test Book");
//            book.setAuthorId(1L);
//            book.setContent("This is a test book content.");
//            bookRepository.save(book);
//
//            // Test saving a user
//            User user = new User();
//            user.setUsername("testuser");
//            user.setEmail("testuser@example.com");
//            user.setFullName("Test User");
//            user.setPassword("password123");
//            user.setIsMfaEnabled(false);
//            userRepository.save(user);
//
//            // Fetch data
//            System.out.println("Books in DB: " + bookRepository.findAll());
//            System.out.println("Users in DB: " + userRepository.findAll());
//        };
//    }

//    @Bean
//    public CommandLineRunner testServiceLayer(BookService bookService) {
//        return args -> {
//            // Create a new book
//            Book book = new Book();
//            book.setTitle("Service Layer Test");
//            book.setAuthorId(1L);
//            book.setContent("Testing service layer logic.");
//            Book savedBook = bookService.createBook(book);
//            System.out.println("Created Book: " + savedBook);
//
//            // Fetch all books
//            System.out.println("All Books: " + bookService.getAllBooks());
//        };
//    }

//    @Bean
//    public CommandLineRunner testUserService(UserService userService) {
//        return args -> {
//            // Create a new user
//            User user = new User();
//            user.setUsername("john_doe");
//            user.setEmail("john.doe@example.com");
//            user.setFullName("John Doe");
//            user.setPassword("securepassword");
//            user.setIsMfaEnabled(true);
//            User savedUser = userService.createUser(user);
//            System.out.println("Created User: " + savedUser);
//
//            // Fetch the user by username
//            Optional<User> fetchedUser = userService.getUserByUsername("john_doe");
//            System.out.println("Fetched User: " + fetchedUser.orElse(null));
//        };
//    }

}
