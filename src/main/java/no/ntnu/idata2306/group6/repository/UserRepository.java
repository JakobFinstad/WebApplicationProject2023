package no.ntnu.idata2306.group6.repository;

import jakarta.transaction.Transactional;
import no.ntnu.idata2306.group6.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Primary
@Component
@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Integer> {

    Page<User> findAll(Pageable pageable);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer integer);

//    Iterable<User> findAllByRoleName(String role);
//    Iterable<Role> findRoleByID(int id);

}
