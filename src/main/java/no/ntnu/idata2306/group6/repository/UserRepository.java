package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.User;
import org.springdoc.core.converters.models.Sort;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Primary
@Component
public interface UserRepository extends JpaRepository<User, Integer> {

    Iterable<User> findAll(Sort sort);
    Page<User> findAll(Pageable pageable);
    List<User> findAll();
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer integer);
    List findAll(final String name);
}
