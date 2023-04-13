package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Page<Role> findAll(Pageable pageable);
    Optional<Role> findById(int id);
}
