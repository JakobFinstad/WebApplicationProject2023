package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Info;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface InfoRepository extends CrudRepository<Info, Integer> {
    Page<Info> findAll(Pageable pageable);
    Optional<Info> findById(int id);

    @Query(value = "SELECT i FROM Info i JOIN Product p where p.productId = ?1\n")
    Iterable<Info> findByProductId(int productId);
}
