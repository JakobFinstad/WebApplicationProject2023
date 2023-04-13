package no.ntnu.idata2306.group6.service;

import no.ntnu.idata2306.group6.entity.Role;
import no.ntnu.idata2306.group6.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for service logic to role.
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Iterable<Role> getAll() {
        return roleRepository.findAll();
    }

    /**
     * Find role by id.
     *
     * @param id of the role that shall be returned
     * @return role if found, else null
     */
    public Role findById(int id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.orElse(null);
    }

    /**
     * Add role to the collection.
     *
     * @param role that shall be added
     * @return true if the role was added and false in cas of failure
     */
    public boolean addRole(Role role) {
        boolean added;
        if (role.getRoleId() < 0 || roleRepository.findById(role.getRoleId()).orElse(null) == role) {
            added = false;
        } else {
            roleRepository.save(role);
            added = true;
        }
        return added;
    }

    /**
     * Remove an role from the collection.
     *
     * @param role that shall be removed
     * @return true on success, false on error
     */
    public boolean remove(Role role) {
        boolean removed;

        try {
            roleRepository.delete(role);
            removed = true;
        } catch (Exception e) {
            removed = false;
        }
        return removed;
    }

    /**
     * Update an role in the collection.
     *
     * @param id of the role that shall be edited
     * @param role the new role that shall be saved
     * @return null on success, else errormessage
     */
    public String update(Integer id, Role role) {
        String errorMessage = null;

        Role existingRole = findById(id);

        if (existingRole == null) {
            errorMessage = "No existing role with id: " + id;
        } else if (role == null) {
            errorMessage = "New role is invalid";
        } else if (role.getRoleId() != id) {
            errorMessage = "Id in URL does not match id in the role";
        }

        if (errorMessage == null) {
            roleRepository.save(role);
        }

        return errorMessage;
    }

}
