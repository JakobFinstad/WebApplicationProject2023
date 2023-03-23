package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.logic.User;
import org.springdoc.core.converters.models.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Iterable<User> findAll(Sort sort);
    Page<User> findAll(Pageable pageable);
    Optional<User> findById(int id);
}
