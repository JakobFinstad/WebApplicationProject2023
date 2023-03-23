package no.ntnu.idata2306.group6.Repository;

import no.ntnu.idata2306.group6.logic.User;
import org.springdoc.core.converters.models.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    Iterable<User> findAll(Sort sort);

    Page<User> findAll(Pageable pageable);
}
