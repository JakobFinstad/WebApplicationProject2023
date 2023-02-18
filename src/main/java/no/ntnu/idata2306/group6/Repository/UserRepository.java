package no.ntnu.idata2306.group6.Repository;

import no.ntnu.idata2306.group6.logic.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
