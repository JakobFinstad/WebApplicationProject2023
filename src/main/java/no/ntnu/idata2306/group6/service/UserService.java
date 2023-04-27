package no.ntnu.idata2306.group6.service;

import jakarta.validation.constraints.NotNull;
import no.ntnu.idata2306.group6.repository.UserRepository;
import no.ntnu.idata2306.group6.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

/**
 * Business logic related to users.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get all users from the application state.
     *
     * @return A list of users, empty list if there are none
     */
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Find user in the application state by id.
     *
     * @param id ID of the user to find
     * @return The user or null if no user found by the id searched
     */
    public Optional<User> findById(@NotNull int id) {
        Optional<User> user = userRepository.findById(id);
        return Optional.ofNullable(user.orElse(null));
    }

    /**
     * Add user to the database.
     *
     * @param user User to add
     * @return true if added, false if not
     */
    public boolean addUser(User user) {
        boolean added = false;
        if (user != null && user.isValid()) {
            User existingUser = findById(user.getUserId()).get();
            if (existingUser == null) {
                userRepository.save(user);
                added = true;
            }
        }
        return added;
    }

    /**
     * Remove user from database by ID
     *
     * @param userId ID of the user to delete
     * @return True if user is deleted, false if user is not found
     */
    public boolean removeUser(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        }
        return user.isPresent();
    }

    /**
     * Update a user in the database.
     *
     * @param id ID of the user
     * @param user The user one wishes to update
     * @return null if successful and error message on error
     */
    public String updateUser(Integer id, User user) {
        String errorMessage = null;
        Optional<User> existingUser = findById(id);
        if (existingUser == null) {
            errorMessage = "No user with id " + id + " found";
        } else if (user == null || !user.isValid()) {
            errorMessage = "Wrong data in request body";
        } else if (user.getUserId() != id) {
            errorMessage = "User ID does not match the ID in the response body";
        }
        if (errorMessage == null) {
            userRepository.save(user);
        }
        return errorMessage;
    }

    /**
     * Get the number of users in the database.
     *
     * @return The total number of users in the database
     */
    public long getCount() {
        return userRepository.count();
    }

    public String getAllStringFormat(Iterator<User> it){
        String str = String.format(
                "| %-8s | %-15s | %-15s | %-3s | %-30s | "
                        + "%8s |\n", "USERID", "FIRSTNAME", "LASTNAME", "AGE"
                , "EMAIL", "PHONE NUMBER"
        );
        while (it.hasNext()) {
            str += it.next().getPrintFormat();
        }

        return str;
    }

 /*   public Iterable<User> findByRole(String role) {
        return userRepository.findAllByRoleName(role);
        return null;
    }*/

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
