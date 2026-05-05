package org.project.onlinebookstore.repository;

public interface SpecificationProviderManager<T> {
    <V> SpecificationProvider<T, V> getSpecificationProvider(String key);
}
