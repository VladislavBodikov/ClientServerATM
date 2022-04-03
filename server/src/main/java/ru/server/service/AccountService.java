package ru.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.server.entity.Account;
import ru.server.entity.User;
import ru.server.exeption.AccountNotFoundException;
import ru.server.exeption.DontHaveEnoughMoneyException;
import ru.server.repository.AccountCrudRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AccountService {

    private AccountCrudRepository accountCrudRepository;
    private UserService userService;

    public Optional<Account> save(Account account) {
        boolean isAccountNumberExists = findByAccountNumber(account.getAccountNumber()).isPresent();
        boolean isCardNumberExists = findByCardNumber(account.getCardNumber()).isPresent();

        if (isCardNumberExists || isAccountNumberExists){
            return Optional.empty();
        }

        Optional<User> userFromDB = userService.findByNameIfNotHaveId(account.getUser());
        boolean isUserExist = userFromDB.isPresent();
        if (isUserExist) {
            account.setUser(userFromDB.get());
            return Optional.of(accountCrudRepository.save(account));
        } else {
            return Optional.empty();
        }
    }

    public int removeById(Long id){
        if (accountCrudRepository.existsById(id)){
            return accountCrudRepository.removeById(id);
        }
        return 0;
    }

    public int removeByCardNumber(String cardNumber){
        if (isCardNumberExist(cardNumber)){
            return accountCrudRepository.removeByCardNumber(cardNumber);
        }
        return 0;
    }

    public int removeByAccountNumber(String scoreNumber){
        if (isAccountNumberExist(scoreNumber)) {
            return accountCrudRepository.removeByAccountNumber(scoreNumber);
        }
        return 0;
    }

    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        accountCrudRepository.findAll().forEach(list::add);
        return list;
    }

    public Optional<Account> findByCardNumber(String cardNumber) {
        return accountCrudRepository.findByCardNumber(cardNumber);
    }

    public Optional<Account> findByAccountNumber(String scoreNumber) {
        return accountCrudRepository.findByAccountNumber(scoreNumber);
    }

    public boolean isAccountNumberExist(String scoreNumber){
        return findByAccountNumber(scoreNumber).isPresent();
    }

    public boolean isCardNumberExist(String cardNumber){
        return findByCardNumber(cardNumber).isPresent();
    }

    public Account transfer(String cardNumberFrom, BigDecimal amountToTransfer, String cardNumberTo){
        Optional<Account> accountFromOpt = accountCrudRepository.findByCardNumber(cardNumberFrom);
        Optional<Account> accountToOpt   = accountCrudRepository.findByCardNumber(cardNumberTo);
        boolean isExistAccountFrom = accountFromOpt.isPresent();
        boolean isExistAccountTo = accountToOpt.isPresent();

        if (isExistAccountFrom && isExistAccountTo){
            Account accountFrom = accountFromOpt.get();
            Account accountTo = accountToOpt.get();

            BigDecimal beforeTransferAccountFrom = accountFrom.getAmount();
            BigDecimal beforeTransferAccountTo = accountTo.getAmount();

            boolean isEnoughMoneyToTransfer = beforeTransferAccountFrom.compareTo(amountToTransfer) >= 0;
            if (isEnoughMoneyToTransfer){

                BigDecimal afterTransferAccountFrom = beforeTransferAccountFrom.subtract(amountToTransfer);
                BigDecimal afterTransferAccountTo = beforeTransferAccountTo.add(amountToTransfer);

                accountFrom.setAmount(afterTransferAccountFrom);
                accountTo.setAmount(afterTransferAccountTo);

                save(accountFrom);
                save(accountTo);

                return accountCrudRepository.findByCardNumber(cardNumberFrom).get();
            }
            String exceptionMessage = "\nAccount : " + accountFrom + "\n Dont have enough money to transfer : "+ amountToTransfer + "\n";
            throw new DontHaveEnoughMoneyException(exceptionMessage);
        }
        String exceptionMessage = "\nAccount with card_number : " + cardNumberFrom + " or account with card_number : "+ cardNumberTo + " not found\n";
        throw new AccountNotFoundException(exceptionMessage);
    }
}
