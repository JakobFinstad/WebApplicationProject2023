package no.ntnu.idata2306.group6.repository;

import jakarta.transaction.Transactional;
import no.ntnu.idata2306.group6.entity.Role;
import no.ntnu.idata2306.group6.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Integer> {

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);


    @Modifying
    @Query(value = "INSERT INTO user_role (user_id, role_id) VALUES (?1, ?2)", nativeQuery = true)
    void addRoleToUser(int userId, int roleId);

//    void setRoleToUser(int userId);

//    Iterable<User> findAllByRoleName(String role);
//    Iterable<Role> findRoleByID(int id);

}
