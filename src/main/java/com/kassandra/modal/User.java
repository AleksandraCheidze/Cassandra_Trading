package com.kassandra.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kassandra.domain.TwoFactorAuth;
import com.kassandra.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Table(name = "users") // Явно указываем имя таблицы, чтобы избежать конфликта с зарезервированным словом
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;
    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;


}
