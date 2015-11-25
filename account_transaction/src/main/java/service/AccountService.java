package main.java.service;

import java.math.BigDecimal;

import main.java.model.Account;

public interface AccountService {
	 
    // A thread-safe method that will move money from one account to another.
    // For example, when passed account FROM with balance 100, account TO with balance 20, and amountToTransfer 20,
    // the end result should be account FROM has balance 80. Account TO has balance 40.
    // All parameters are mandatory.
    // The implementation should also ensure that:
    // - the transfer only succeeds if there is enough balance on the FROM account,
    //   i.e. if balance on this account is 50 and amount to transfer is 100, the method should throw an exception
    // - the amountToTransfer is always positive - providing negative value should throw an exception
    // - deducting money from account can be achieved by simply changing the order of parameters from/to
    // Consider performance and thread-safety when implementing this method.
    // It should never deadlock and it should make sure that all operations leave accounts in a consistent state
    // regardless of how many concurrent operations are happening in the system.
    public void transfer(Account from, Account to, BigDecimal amountToTransfer) throws Exception;
 
}

