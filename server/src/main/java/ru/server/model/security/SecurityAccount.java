package ru.server.model.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.server.model.entity.Account;
import ru.server.model.Status;
import ru.server.model.entity.User;

import java.util.Collection;

@Data
public class SecurityAccount implements UserDetails {

    private Account account;
    private boolean isActive;

    public SecurityAccount(Account account,boolean isActive) {
        this.account = account;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getUser().getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return account.getPinCode();
    }

    @Override
    public String getUsername() {
        return account.getCardNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromAccount(Account account) {
        User accountOwner = account.getUser();

        return new org.springframework.security.core.userdetails.User(
                account.getCardNumber(), account.getPinCode(),
                accountOwner.getStatus().equals(Status.ACTIVE),
                accountOwner.getStatus().equals(Status.ACTIVE),
                accountOwner.getStatus().equals(Status.ACTIVE),
                accountOwner.getStatus().equals(Status.ACTIVE),
                accountOwner.getRole().getAuthorities()
        );
    }
}