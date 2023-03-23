package no.ntnu.idata2306.group6.logic;

import com.sun.source.tree.CaseTree;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A class that represents the different roles
 */
@Entity
public class Role {
    @Id
    @GeneratedValue
    @NotNull
    private Long id;
    private String name;

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
     * @param name of the role
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     * Returns the id of the role.
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of the role.
     *
     * @param id of the role
     */
    public void setId(Long id) {
        this.id = id;
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
    public String getName() {
        return name;
    }

    /**
     * Set the name of the role.
     *
     * @param name of the role
     */
    public void setName(String name) {
        this.name = name;
    }
}
