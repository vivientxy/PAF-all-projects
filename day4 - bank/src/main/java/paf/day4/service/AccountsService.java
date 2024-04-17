package paf.day4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import paf.day4.repo.AccountsRepository;

@Service
public class AccountsService {
    
    @Autowired
    private AccountsRepository repo;

    // rollback if it is an unchecked exception
    // commit if it is a checked exception -> unless you add the (rollbackFor = AccountsException.class)
    @Transactional
    // @Transactional(rollbackFor = AccountsException.class)
    public void fundsTransfer(String fromAcct, String toAcct, float amount) throws AccountsException {

        // // method 1
        // if (!(repo.updateBalanceById(fromAcct, -amount) && repo.updateBalanceById(toAcct, amount))) {
        //     throw new AccountsException("Cannot perform transfer");
        // }

        // // method 2
        // if (!repo.updateBalanceById(fromAcct, -amount))
        //     throw new AccountsException("Cannot perform transfer");
        // if (!repo.updateBalanceById(toAcct, amount))
        //     throw new AccountsException("Cannot perform transfer");

        // // method 3 -- with @Transactional, this will commit the transaction but throw error
        // if (!repo.updateBalanceById(fromAcct, -amount))
        //     throw new AccountsException("Cannot perform transfer");
        // throw new AccountsException("Cannot perform transfer");

        // method 4 -- with @Transactional(rollbackFor = AccountsException.class), this will rollback the transaction and throw error
        if (!repo.updateBalanceById(fromAcct, -amount))
            throw new AccountsException("Cannot perform transfer");
        throw new AccountsException("Cannot perform transfer");
    }
}
