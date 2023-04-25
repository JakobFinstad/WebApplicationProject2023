package no.ntnu.idata2306.group6.controller;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.entity.Role;
import no.ntnu.idata2306.group6.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    private RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class.getSimpleName());

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Get all roles.
     * HTTP Get to /role
     *
     * @return list of all roles currently in collection
     */
    @GetMapping
    @Operation(
            summary = "Get all roles",
            description = "List of all roles currently stored in collection"
    )
    public ResponseEntity<Object> getAll() {
        logger.error("Getting all ");
        Iterable<Role> roles = roleRepository.findAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    /**
     * Get a specific role.
     *
     * @param id of the returned role
     * @return role with the given id or status 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Role> getOne(@PathVariable Integer id) {
        ResponseEntity<Role> response;
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            response = new ResponseEntity<>(role.get(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * HTTP POST endpoint for adding a new role.
     *
     * @param role data of the role to add. ID will be ignored.
     * @return 201 Created on success and the new ID in the response body,
     * 400 Bad request if some data is missing or incorrect
     */
    @PostMapping()
    @Operation(deprecated = true)
    public ResponseEntity<String> add(@RequestBody Role role) {
        ResponseEntity<String> response;

        try {
            addRoleToCollection(role);
            response = new ResponseEntity<>("" + role.getRoleId(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Remove role from the collection.
     *
     * @param id ID of the product to remove
     * @return true when product with that ID is removed, false otherwise
     */
    private boolean removeRoleFromCollection(int id) {
        boolean deleted = false;
        try {
            roleRepository.deleteById(id);
            deleted = true;
        } catch (DataAccessException e) {
            logger.warn("Could not delete the role with ID: " + id + " : " + e.getMessage());
        }
        return deleted;
    }

    /**
     * Delete a role from the collection.
     *
     * @param id ID of the role to delete
     * @return 200 OK on success, 404 Not found on error
     */
    @DeleteMapping("/{id}")
    @Operation(hidden = true)
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response;
        if (removeRoleFromCollection(id)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Update a role in the repository.
     *
     * @param id of the role to update, from the URL
     * @param role new role data to store, from request body
     * @return 200 OK on success, 400 Bad request on error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Role role) {
        ResponseEntity<String> response;
        try {
            updateRole(id, role);
            response = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Add a role to collection.
     *
     * @param role the role to be added to collection if it is valid
     * @throws IllegalArgumentException
     */
    private void addRoleToCollection(Role role) throws IllegalArgumentException {
        if (role == null || role.getRoleId() < 0) {
            throw new IllegalArgumentException("Product is invalid");
        }
        roleRepository.save(role);
    }

    /**
     * Try to update a role with given ID. The role id must match the ID.
     *
     * @param id of the role
     * @param role the update role data
     * @throws IllegalArgumentException if something goes wrong
     */
    private void updateRole(int id, Role role) throws  IllegalArgumentException {
        Optional<Role> existingRole = roleRepository.findById(id);
        if (existingRole.isEmpty()) {
            throw new IllegalArgumentException("No product with id " + id + " found");
        }
        if (role == null) {
            throw new IllegalArgumentException("Wrong data in request body");
        }
        if (role.getRoleId() != id) {
            throw new IllegalArgumentException("Role ID in the URL does not match the ID " +
                    "in the ID in JSON data(request body)");
        }

        try {
            roleRepository.save(role);
        } catch (Exception e) {
            logger.warn("Could not update role " + role.getRoleId() + ": " + e.getMessage());
            throw new IllegalArgumentException("Could not update role " + role.getRoleId());
        }
    }
}
