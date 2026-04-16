package org.project.onlinebookstore;

import java.math.BigDecimal;
import org.project.onlinebookstore.model.Book;
import org.project.onlinebookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Autowired
    private BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            Book book = new Book();
            book.setAuthor("Robert Martin");
            book.setTitle("Clean Code");
            book.setIsbn("978-0132350884");
            book.setPrice(BigDecimal.valueOf(29.99));

            bookRepository.save(book);
            System.out.println(bookRepository.findAll());
        };
    }

}
