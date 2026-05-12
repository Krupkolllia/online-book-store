package org.project.onlinebookstore.repository.book;

import java.util.Optional;
import org.project.onlinebookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Override
    @NonNull
    @EntityGraph(attributePaths = "categories")
    Page<Book> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    @EntityGraph(attributePaths = "categories")
    Optional<Book> findById(@NonNull Long id);

    @Override
    @NonNull
    @EntityGraph(attributePaths = "categories")
    Page<Book> findAll(Specification<Book> spec, @NonNull Pageable pageable);

    Page<Book> findAllByCategoryId(Long categoryId, Pageable pageable);
}
