package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Page<Role> findAll(Pageable pageable);
    Optional<Role> findById(int id);
}
