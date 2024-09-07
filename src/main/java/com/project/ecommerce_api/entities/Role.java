package com.project.ecommerce_api.entities;

import com.project.ecommerce_api.utilities.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "roles")
public class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(nullable = false)
        private Long id;

        @Column(unique = true, nullable = false)
        @Enumerated(EnumType.STRING)
        private RoleEnum name;

        @Column(nullable = false)
        private String description;

        @CreationTimestamp
        @Column(updatable = false, name = "created_at")
        private Date createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private Date updatedAt;

}
