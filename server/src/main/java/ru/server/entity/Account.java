package ru.server.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ACCOUNTS")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "card_number",nullable = false)
    @Pattern(regexp = "^[\\d]{16}$")
    private String cardNumber;

    @Column(name = "account_number",nullable = false)
    @Pattern(regexp = "^[\\d]{20}$")
    private String accountNumber;

    private BigDecimal amount;

    @Column(name = "pin_code",nullable = false)
    @Pattern(regexp = "^[\\d]{4}$")
    private String pinCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public String toString() {
        return "Account(id=" + this.getId() + ", cardNumber=" + this.getCardNumber() + ", scoreNumber=" + this.getAccountNumber() + ", amount=" + this.getAmount() + ", pinCode=****)";
    }
}
