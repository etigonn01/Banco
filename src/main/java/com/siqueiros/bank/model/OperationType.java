package com.siqueiros.bank.model;

import jakarta.persistence.*;

@Entity
@Table(name = "operation_types")
public class OperationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, unique = true, length = 20)
    private String type;

    public OperationType() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
