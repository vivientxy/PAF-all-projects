package paf.day4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import paf.day4.repo.AccountsRepository;

@Service
public class AccountsService {
    
    @Autowired
    private AccountsRepository repo;

    @Autowired
    private PlatformTransactionManager txMgr;
 
    public void fundsTransfer2(String fromAcct, String toAcct, float amount) {
        TransactionStatus txStatus = txMgr.getTransaction(TransactionDefinition.withDefaults());
        // TransactionTemplate tx = new TransactionTemplate(txMgr);
        try {
            repo.updateBalanceById(fromAcct, -amount);
            repo.updateBalanceById(toAcct, amount);
            txMgr.commit(txStatus);
        } catch (Exception e) {
            txMgr.rollback(txStatus);
        }
    }





    // rollback if it is an unchecked exception
    // commit if it is a checked exception -> unless you add the (rollbackFor = AccountsException.class)
    @Transactional
    // @Transactional(rollbackFor = AccountsException.class)
    public void fundsTransfer(String fromAcct, String toAcct, float amount) throws AccountsException {

        // method 1
        if (!(repo.updateBalanceById(fromAcct, -amount) && repo.updateBalanceById(toAcct, amount))) {
            throw new AccountsException("Cannot perform transfer");
        }

        // method 2
        if (!repo.updateBalanceById(fromAcct, -amount))
            throw new AccountsException("Cannot perform transfer");
        if (!repo.updateBalanceById(toAcct, amount))
            throw new AccountsException("Cannot perform transfer");

        // method 3 -- with @Transactional, this will commit the transaction but throw error
        if (!repo.updateBalanceById(fromAcct, -amount))
            throw new AccountsException("Cannot perform transfer");
        // throw new AccountsException("Cannot perform transfer");

        // method 4 -- with @Transactional(rollbackFor = AccountsException.class), this will rollback the transaction and throw error
        if (!repo.updateBalanceById(fromAcct, -amount))
            throw new AccountsException("Cannot perform transfer");
        throw new AccountsException("Cannot perform transfer");
    }
}
