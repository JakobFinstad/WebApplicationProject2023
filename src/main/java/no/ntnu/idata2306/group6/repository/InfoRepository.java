package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Info;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InfoRepository extends JpaRepository<Info, Integer> {
    Page<Info> findAll(Pageable pageable);
    Optional<Info> findById(int id);
}
