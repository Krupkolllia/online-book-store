package org.project.onlinebookstore.repository.book.spec;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.model.book.Book;
import org.project.onlinebookstore.repository.SpecificationProvider;
import org.project.onlinebookstore.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager
        implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book, ?>> providers;

    @SuppressWarnings("unchecked")
    @Override
    public <V> SpecificationProvider<Book, V> getSpecificationProvider(
            String key) {
        return (SpecificationProvider<Book, V>) providers.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                                "Cannot find correct specification provider for key " + key));
    }
}
