package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import no.ntnu.idata2306.group6.entity.Role;
import no.ntnu.idata2306.group6.entity.User;
import no.ntnu.idata2306.group6.entity.dto.UserDTO;
import no.ntnu.idata2306.group6.service.RoleService;
import no.ntnu.idata2306.group6.service.UserService;
import no.ntnu.idata2306.group6.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class.getSimpleName());

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users in the collection"
    )
    public ResponseEntity<Object> getAll(@RequestParam(required = false) String role) {
        logger.info("Getting all users");
        Iterable<User> users;
        if (role == null) {
            users = userService.getAll();
        } else {
            // users = userService.findByRole(role);
            users = null;
        }
        return new ResponseEntity<>(userService.getAllStringFormat(users.iterator()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a user by ID",
            description = "Retrieve a user by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUser(@PathVariable int id) {
        ResponseEntity<UserDTO> response;
        Optional<User> user = userService.findById(id);
        UserDTO userDTO = new UserDTO().setAge()
                .setPassword(user.get().getPassword())
                .setRoles(user.get().getRoles().stream().map(Role::getRoleName))
                .setEmail(user.get().getEmail())
                .setLastName(user.get().getLastName())
                .setPhoneNumber(user.get().getPhoneNumber());

        if (user.isPresent()) {
            response = new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/signup")
    @Transactional
    @Operation(
            summary = "Sign up a user",
            description = "Add a new user to the collection"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<String> add(@RequestBody UserDTO userDTO) {
        ResponseEntity<String> response;

        try {
            Role role = roleService.findById(3);
            userDTO.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
            User user = new User(
                    userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getEmail(),
                    userDTO.getPhoneNumber(),
                    userDTO.getPassword(),
                    userDTO.getAge()
            );
            user.addRole(role);
            addUserToCollection(user);
            response = new ResponseEntity<>("" + user.getUserId(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @DeleteMapping("/{id}")
    @Operation(hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "User found and deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response;

        if (removeUserFromCollection(id)) {
            response = new ResponseEntity<>(HttpStatus.FOUND);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a user",
            description = "Update an existing user in the collection"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody User user) {
        ResponseEntity<String> response;

        try {
            updateUser(id, user);
            response = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    private void addUserToCollection(User user) {
        if (!user.isValid()) {
            throw new IllegalArgumentException("User is not valid");
        }
        userService.addUser(user);
    }

    private boolean removeUserFromCollection(int id) {
        boolean deleted;
        try {
            userService.removeUser(id);
            deleted = true;
        } catch (NullPointerException ne) {
            deleted = false;
            logger.warn("User with id: " + id + " not found");
        } catch (Exception e) {
            deleted = false;
            logger.error("Something went wrong: " + e.getMessage());
        }
        return deleted;
    }

    private void updateUser(int id, User user) {
        Optional<User> existingUser = userService.findById(id);

        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("No user with ID " + id + " in the collection.");
        }
        if (user == null || !user.isValid()) {
            throw new IllegalArgumentException("Invalid user data");
        }
        if (user.getUserId() != id) {
            throw new IllegalArgumentException("User ID in the URL does not match the user data");
        }

        try {
            userService.updateUser(id, user);
            logger.info("User updated successfully");
        } catch (Exception e) {
            logger.error("Could not update user " + user.getUserId());
            throw new IllegalArgumentException("Could not update user");
        }
    }
}
