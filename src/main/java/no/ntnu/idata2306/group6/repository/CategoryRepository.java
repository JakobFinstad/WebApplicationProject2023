package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Category;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Optional<Category> findById(int id);
    Iterable<Category> findAll();
}
