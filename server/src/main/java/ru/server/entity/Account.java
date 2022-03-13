package ru.server.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "ACCOUNTS")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "card_number",nullable = false)
    private String cardNumber;

    @Column(name = "score_number",nullable = false)
    private String scoreNumber;

    private BigDecimal amount;

    @Column(name = "pin_code",nullable = false)
    private String pinCode;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
