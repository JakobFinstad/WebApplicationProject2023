package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Product;
import no.ntnu.idata2306.group6.entity.Subscription;
import no.ntnu.idata2306.group6.entity.User;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
//    Iterable<Subscription> findAll (Pageable pageable);
    Optional<Subscription> findById(int id);

    List<Subscription> findByUser(User user);

    @Query(value = "SELECT p FROM Product p JOIN p.subscriptions s JOIN s.user u WHERE u.userId = ?1\n")
    List<Product> findProductByUserId(int userId);

    @Query(value = "SELECT u FROM User u JOIN u.subscriptions s JOIN s.product p WHERE p.productId = ?1\n")
    List<User> findUserByProductId(int id);
}
