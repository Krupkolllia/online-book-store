package org.project.onlinebookstore.repository;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T, V> {
    String getKey();

    Specification<T> getSpecification(V value);
}
