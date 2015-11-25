package test.java.model;

import java.math.BigDecimal;

import org.junit.Test;

import main.java.model.Account;

public class AccountTest {
	
	private Account acct = null;

	@Test
	public void testGetBalance() {
		this.createAccountObj();
		System.out.println(this.acct.getBalance());
	}

	@Test
	public void testSetBalance() {
		this.createAccountObj();
		System.out.println(this.acct.setBalance(new BigDecimal(2000000.0)));
	}

	@Test
	public void testGetAccountId() {
		this.createAccountObj();
		System.out.println(this.acct.getAccountId());
	}

	@Test
	public void testAccount() {
		this.createAccountObj();
		System.out.println(this.acct.toString());
	}
	
	private void createAccountObj() {
		if (this.acct==null) {
			this.acct = new Account("ID00001", new BigDecimal(1000000.0));
		}
	}

}
