package com.astandro.library.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Constructors, getters, setters
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String action;
    @Getter
    @Setter
    private String ip;
    @Getter
    @Setter
    private String browser;
    @Getter
    @Setter
    private String device;
    @Getter
    @Setter
    private LocalDateTime timestamp;
}

