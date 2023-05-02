package no.ntnu.idata2306.group6.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A class that represents the different roles
 */
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private int roleId;
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new LinkedHashSet<>();

    /**
     * Needed for JPA
     */
    public Role() {
    }

    /**
     * Constructor for the role
     *
     * @param roleName of the role
     */
    public Role(String roleName) {
        this.roleName = roleName.toUpperCase();
    }

    /**
     * Returns the id of the role.
     *
     * @return id
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * Set the id of the role.
     *
     * @param id of the role
     */
    public void setRoleId(int id) {
        this.roleId = id;
    }

    /**
     * Returns the collection of users.
     *
     * @return the users
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Set the users in the collection.
     *
     * @param users in the collection
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /**
     * Returns the name of the role.
     *
     * @return the name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Set the name of the role.
     *
     * @param name of the role
     */
    public void setRoleName(String name) {
        this.roleName = name;
    }
}
