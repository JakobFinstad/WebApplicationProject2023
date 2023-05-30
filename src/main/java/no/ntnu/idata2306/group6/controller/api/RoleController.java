package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import no.ntnu.idata2306.group6.entity.Role;
import no.ntnu.idata2306.group6.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api/role")
public class RoleController {
    private RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class.getSimpleName());

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Get all roles.
     *
     * @return list of all roles currently in collection
     */
    @GetMapping
    @Operation(
            summary = "Get all roles",
            description = "Returns a list of all roles currently stored in the collection",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(schema = @Schema(implementation = Role.class))
                    )
            }
    )
    public ResponseEntity<Iterable<Role>> getAll() {
        Iterable<Role> roles = roleService.getAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    /**
     * Get a specific role.
     *
     * @param id ID of the requested role
     * @return role with the given ID or status 404 if not found
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get a role by ID",
            description = "Returns the role with the specified ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(schema = @Schema(implementation = Role.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Role not found"
                    )
            }
    )
    public ResponseEntity<Role> getOne(
            @Parameter(description = "ID of the role to retrieve") @PathVariable Integer id
    ) {
        ResponseEntity<Role> response;
        Optional<Role> role = Optional.ofNullable(roleService.findById(id));
        if (role.isPresent()) {
            response = new ResponseEntity<>(role.get(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Add a new role.
     *
     * @param role role data to add (ID will be ignored)
     * @return 201 Created on success and the new ID in the response body,
     * 400 Bad request if some data is missing or incorrect
     */
    @PostMapping
    @Operation(
            summary = "Add a new role",
            description = "Adds a new role to the collection",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Role added successfully",
                            content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    )
            }
    )
    public ResponseEntity<String> add(
            @Parameter(description = "Role data to add", required = true) @RequestBody Role role
    ) {
        ResponseEntity<String> response;

        try {
            addRoleToCollection(role);
            response = new ResponseEntity<>(String.valueOf(role.getRoleId()), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Remove role from the collection.
     *
     * @param role role to remove
     * @return true when the role with that ID is removed, false otherwise
     */
    private boolean removeRoleFromCollection(Role role) {
        boolean deleted = false;
        try {
            roleService.remove(role);
            deleted = true;
        } catch (DataAccessException e) {
            logger.warn("Could not delete the role with ID: " + role.getRoleId() + " : " + e.getMessage());
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
    @Operation(
            summary = "Delete a role by ID",
            description = "Deletes the role with the specified ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Role not found"
                    )
            }
    )
    public ResponseEntity<String> delete(
            @Parameter(description = "ID of the role to delete") @PathVariable int id
    ) {
        ResponseEntity<String> response;
        Role roleToDelete = roleService.findById(id);
        if (removeRoleFromCollection(roleToDelete)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Update a role in the repository.
     *
     * @param id   ID of the role to update (from URL)
     * @param role updated role data (from request body)
     * @return 200 OK on success, 400 Bad request on error
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update a role",
            description = "Updates the role with the specified ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    )
            }
    )
    public ResponseEntity<String> update(
            @Parameter(description = "ID of the role to update") @PathVariable int id,
            @Parameter(description = "Updated role data", required = true) @RequestBody Role role
    ) {
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
     * Add a role to the collection.
     *
     * @param role the role to be added to the collection if it is valid
     * @throws IllegalArgumentException if the role is invalid
     */
    private void addRoleToCollection(Role role) throws IllegalArgumentException {
        if (role == null || role.getRoleId() < 0) {
            throw new IllegalArgumentException("Role is invalid");
        }
        roleService.addRole(role);
    }

    /**
     * Try to update a role with the given ID. The role ID must match the ID in the request body.
     *
     * @param id   ID of the role
     * @param role updated role data
     * @throws IllegalArgumentException if something goes wrong
     */
    private void updateRole(int id, Role role) throws IllegalArgumentException {
        Optional<Role> existingRole = Optional.ofNullable(roleService.findById(id));
        if (existingRole.isEmpty()) {
            throw new IllegalArgumentException("No role with ID " + id + " found");
        }
        if (role == null) {
            throw new IllegalArgumentException("Wrong data in the request body");
        }
        if (role.getRoleId() != id) {
            throw new IllegalArgumentException("Role ID in the URL does not match the ID in the request body");
        }

        try {
            roleService.update(id, role);
        } catch (Exception e) {
            logger.warn("Could not update role " + role.getRoleId() + ": " + e.getMessage());
            throw new IllegalArgumentException("Could not update role " + role.getRoleId());
        }
    }
}
