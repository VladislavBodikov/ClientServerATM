package ru.server.entity;

import lombok.*;
import ru.server.model.Role;
import ru.server.model.Status;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@Table(name = "USERS")
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    @Pattern(regexp = "^[А-Яа-яё]|[A-Za-z]+$")
    private String firstName;

    @Column(name = "last_name")
    @Pattern(regexp = "^[А-Яа-яё]|[A-Za-z]+$")
    private String lastName;

    @Column(name = "passport_data")
    @Pattern(regexp = "^[\\d]+$")
    private String passportData;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Account> accounts;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.USER;
}
