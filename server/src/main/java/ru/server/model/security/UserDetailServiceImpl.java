package ru.server.model.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.server.model.entity.Account;
import ru.server.service.AccountService;

@Service("userDetailsServiceImpl")
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private AccountService accountService;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService
                .findByCardNumber(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
        return SecurityAccount.fromAccount(account);
    }
}
