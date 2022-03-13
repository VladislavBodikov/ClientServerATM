package ru.server.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Table(name = "USERS")
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "passport_data")
    private String passportData;
    @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Account> accounts;
}
