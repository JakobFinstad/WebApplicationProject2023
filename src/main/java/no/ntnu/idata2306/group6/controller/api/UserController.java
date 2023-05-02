package no.ntnu.idata2306.group6.controller.api;


import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.entity.User;
import no.ntnu.idata2306.group6.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST API for alternating the users in this project.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class.getSimpleName());

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all the users in the collection.
     *
     * @return list of users in the collection and
     */
    @GetMapping
    @Operation(
            summary = "Get all the users",
            description = "List of all the users currently stored in the collection"
    )
    public ResponseEntity<Object> getAll(@RequestParam(required = false) String role) {
        logger.info("Getting all ");
        Iterable<User> users;
        if (role == null) {
            users = userService.getAll();
        } else {
//            users = userService.findByRole(role);
            users = null;
        }
        return new ResponseEntity<>(userService.getAllStringFormat(users.iterator()), HttpStatus.OK);
    }

    /**
     * Get one user from the directory.
     *
     * @param id of the user
     * @return user by the given id or status 404 if failed
     */
     @GetMapping("/{id}")
     @Operation(
             summary = "Get one user",
             description = "Uses id to identify one user and return it"
     )
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        ResponseEntity<User> response;
         Optional<User> user = userService.findById(id);

         if (user.isPresent()) {
             response = new ResponseEntity<>(user.get(), HttpStatus.OK);
         } else {
             response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }

         return response;
    }

    /**
     * Add a user to the collection.
     *
     * @param user that shall be added to the collection
     * @return the user id and <CODE>CREATED</CODE> status if success, else 400
     */
    @PostMapping
    @Operation(deprecated = true)
    public ResponseEntity<String> add(@RequestBody User user) {
         ResponseEntity<String> response;

         try {
             addUserToCollection(user);
             response= new ResponseEntity<>("" + user.getUserId(), HttpStatus.CREATED);
         } catch (IllegalArgumentException e) {
             response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
         }

         return response;
    }

    /**
     * Delete a user form the collection.
     *
     * @param id of the user that shall be deleted
     * @return 302 Found if the user was deleted successfully or 404 Not found on error
     */
    @DeleteMapping("/{id}")
    @Operation(hidden = true)
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response;

        if (removeUserFromCollection(id)) {
            response =  new ResponseEntity<>(HttpStatus.FOUND);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return response;
    }

    /**
     * Update a user in the repository.
     *
     * @param id of the user that shall be edited
     * @param user new user that shall be saved
     * @return 200 OK on success, 400 Bad request on error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody User user) {
        ResponseEntity<String> response;

        try {
            updateUser(id,user);
            response = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }


    /**
     * Add a user to the collection.
     *
     * @param user that shall be added to the collection
     */
    private void addUserToCollection(User user) {
         if (!user.isValid()) {
             throw new IllegalArgumentException("User not valid");
         }
         userService.addUser(user);
    }

    /**
     * Delete a user from the collection.
     *
     * @param id of the user that shall be deleted
     * @return true if deleted or false if not found or not deleted
     */
    private boolean removeUserFromCollection(int id) {
        boolean deleted;
        try {
            userService.removeUser(id);
            deleted = true;
        } catch (NullPointerException ne) {
            deleted = false;
            logger.warn("User with id: " + id + " , not found");
        } catch (Exception e) {
            deleted = false;
            logger.error("Something went wrong: " + e.getMessage());
        }
        return deleted;
    }


    /**
     * Update user in the directory.
     *
     * @param id of the user in the directory that shall be edited
     * @param user the new entity that shall be saved to the repository
     */
    private void updateUser(int id, User user) {
        Optional<User> existingUser = userService.findById(id);

        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("No user with id " + id + " in the collection.");
        }
        if (user == null ||  !user.isValid()) {
            throw new IllegalArgumentException("Entered user is invalid");
        }
        if (user.getUserId() != id) {
            throw new IllegalArgumentException("Id in user and URL does not match");
        }

        try {
            userService.updateUser(id, user);
            logger.info("Added to the collection!");
        } catch (Exception e) {
            // TODO- Discuss with the others, is this error or warn?
            logger.error("Could not update user " + user.getUserId());
            throw new IllegalArgumentException("Could not update user");
        }

    }
}
