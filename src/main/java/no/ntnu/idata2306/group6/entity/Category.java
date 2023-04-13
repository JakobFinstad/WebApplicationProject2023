package no.ntnu.idata2306.group6.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public enum Category{
    PLANNING,
    SCHEDULING,
    AUTOMATION,
    PROCESSES,
    LEGAL,
    ACCOUNTING,
    TAX,
    FINANCE
    ;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany()//Implement function
    private Set<Product> products = new LinkedHashSet<>();

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
