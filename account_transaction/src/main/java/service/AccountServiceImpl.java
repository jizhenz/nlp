package main.java.service;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import main.java.model.Account;

public class AccountServiceImpl implements AccountService {
	
	private static ConcurrentMap<String, ReentrantLock> accountLockMap = new ConcurrentHashMap<String, ReentrantLock>();

	public void transfer(Account from, Account to, BigDecimal amountToTransfer) throws Exception {
		if (from == null) {
			throw new Exception("Account FROM must not be NULL!");
		}
		if (to == null) {
			throw new Exception("Account TO must not be NULL!");
		}
		if (amountToTransfer == null) {
			throw new Exception("Amount to Transfer must not be NULL!");
		}
		if (amountToTransfer.compareTo(new BigDecimal(0))<0) {
			throw new Exception("Amount to Transfer must not be negtive!");
		}
		
		ReentrantLock fromLock = null, toLock = null;
		if (accountLockMap.containsKey(from.getAccountId())) {
			fromLock = accountLockMap.get(from.getAccountId());
		} else {
			fromLock = new ReentrantLock();
			accountLockMap.put(from.getAccountId(), fromLock);
		}
		if (accountLockMap.containsKey(to.getAccountId())) {
			toLock = accountLockMap.get(to.getAccountId());
		} else {
			toLock = new ReentrantLock();
			accountLockMap.put(to.getAccountId(), toLock);
		}
		try {
			fromLock.lock();
			toLock.lock();
			BigDecimal fromBalance = from.getBalance();
			BigDecimal toBalance = to.getBalance();
			if (fromBalance.compareTo(amountToTransfer)<0) {
				throw new Exception("Account " + from.getAccountId() + " does NOT have enough balance!");
			}
			from.setBalance(fromBalance.subtract(amountToTransfer));
			to.setBalance(toBalance.add(amountToTransfer));
		} catch (Exception e) {
			throw e;
		} finally {
			if (fromLock.isLocked()) {
				fromLock.unlock();
			}
			if (toLock.isLocked()) {
				toLock.unlock();
			}
		}

	}

}
