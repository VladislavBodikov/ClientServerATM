//package ru.server.security;
//
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import ru.server.entity.Account;
//import ru.server.entity.User;
//import ru.server.service.AccountService;
//import ru.server.service.UserService;
//
//@Service("userDetailsServiceImpl")
//@AllArgsConstructor
//public class UserDetailServiceImpl implements UserDetailsService {
//
//    private AccountService accountService;
//    private UserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Account account = accountService
//                .findByCardNumber(username)
//                .orElseThrow(()-> new UsernameNotFoundException(username));
//        return SecurityAccount.fromAccount(account);
//    }
//}
