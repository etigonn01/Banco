package com.siqueiros.bank.model;

import jakarta.persistence.*;

@Entity
@Table(name = "types_accounts")
public class TypeAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    public TypeAccount() {}
    public TypeAccount(String name) { this.name = name; }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
}
