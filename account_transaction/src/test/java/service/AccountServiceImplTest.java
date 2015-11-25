package test.java.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.model.Account;
import main.java.service.AccountService;
import main.java.service.AccountServiceImpl;

public class AccountServiceImplTest {
	
	private static Account acct1 = null;
	private static Account acct2 = null;
	private static Account acct3 = null;
	private static Account acct4 = null;
	private static AccountService service = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		acct1 = new Account("ID_0000001", BigDecimal.valueOf(1000000.11));
		acct2 = new Account("ID_0000002", BigDecimal.valueOf(100.11));
		acct3 = new Account("ID_0000003", BigDecimal.valueOf(2000000.22));
		acct4 = new Account("ID_0000004", BigDecimal.valueOf(200.22));
		service = new AccountServiceImpl();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTransfer() {
		try {
			System.out.println("Transfer 1000000.11 from " + acct3.getAccountId() + " to " + acct1.getAccountId() + "...");
			System.out.println("Before Transfer:");
			System.out.println(acct3.toString());
			System.out.println(acct1.toString());
			service.transfer(acct3, acct1, BigDecimal.valueOf(1000000.11));
			System.out.println("After Transfer:");
			System.out.println(acct3.toString());
			System.out.println(acct1.toString());
			assertEquals(acct3.getBalance(), BigDecimal.valueOf(1000000.11));
			assertEquals(acct1.getBalance(), BigDecimal.valueOf(2000000.22));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println("Transfer 2000000.22 from " + acct1.getAccountId() + " to " + acct3.getAccountId() + "...");
			System.out.println("Before Transfer:");
			System.out.println(acct1.toString());
			System.out.println(acct3.toString());
			service.transfer(acct1, acct3, BigDecimal.valueOf(2000000.22));
			System.out.println("After Transfer:");
			System.out.println(acct1.toString());
			System.out.println(acct3.toString());
			assertEquals(acct1.getBalance().toString(), "0.00");
			assertEquals(acct3.getBalance(), BigDecimal.valueOf(3000000.33));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println("Transfer 1.0 from " + acct1.getAccountId() + " to " + acct3.getAccountId() + "...");
			System.out.println("Before Transfer:");
			System.out.println(acct1.toString());
			System.out.println(acct3.toString());
			service.transfer(acct1, acct3, BigDecimal.valueOf(1.0));
			System.out.println("After Transfer:");
			System.out.println(acct1.toString());
			System.out.println(acct3.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(e != null);
		}
		
		try {
			System.out.println("Transfer -1.0 from " + acct3.getAccountId() + " to " + acct1.getAccountId() + "...");
			System.out.println("Before Transfer:");
			System.out.println(acct3.toString());
			System.out.println(acct1.toString());
			service.transfer(acct3, acct1, BigDecimal.valueOf(-1.0));
			System.out.println("After Transfer:");
			System.out.println(acct3.toString());
			System.out.println(acct1.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(e != null);
		}
		
		try {
			System.out.println("Transfer null from " + acct3.getAccountId() + " to " + acct1.getAccountId() + "...");
			System.out.println("Before Transfer:");
			System.out.println(acct3.toString());
			System.out.println(acct1.toString());
			service.transfer(acct3, acct1, null);
			System.out.println("After Transfer:");
			System.out.println(acct3.toString());
			System.out.println(acct1.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(e != null);
		}
		
		try {
			System.out.println("Transfer 1.0 from null to " + acct1.getAccountId() + "...");
			System.out.println("Before Transfer:");
			System.out.println(acct1.toString());
			service.transfer(null, acct1, BigDecimal.valueOf(1.0));
			System.out.println("After Transfer:");
			System.out.println(acct1.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(e != null);
		}
		
		try {
			System.out.println("Transfer 1.0 from " + acct1.getAccountId() + " to null ...");
			System.out.println("Before Transfer:");
			System.out.println(acct1.toString());
			service.transfer(acct1, null, BigDecimal.valueOf(1.0));
			System.out.println("After Transfer:");
			System.out.println(acct1.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(e != null);
		}
	}
	
	@Test
	public void testTransferCoccurent() {
		class TransferTask implements Runnable {
			Account from; 
			Account to;
			BigDecimal amountToTransfer;
			TransferTask(Account from, Account to, BigDecimal amountToTransfer) {
				this.from=from;
				this.to=to;
				this.amountToTransfer=amountToTransfer;
			}

			public void run() {
				try {
					service.transfer(from, to, amountToTransfer);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		System.out.println("Transfer 1.0 from " + acct2.getAccountId() + " to " + acct4.getAccountId() + " 10 times concurrently ...");
		System.out.println("Before Transfer:");
		System.out.println(acct2.toString());
		System.out.println(acct4.toString());
		List<Thread> threadList = new ArrayList<Thread>(20);
		for (int i=0; i<10; i++) {
			threadList.add(new Thread(new TransferTask(acct2, acct4, BigDecimal.valueOf(1.0))));
		}
		for (Thread t : threadList) {
			t.start();
		}
		try {
			for (Thread t : threadList) {
					t.join();
			}
			System.out.println("After Transfer:");
			System.out.println(acct2.toString());
			System.out.println(acct4.toString());
			assertEquals(acct2.getBalance(), BigDecimal.valueOf(90.11));
			assertEquals(acct4.getBalance(), BigDecimal.valueOf(210.22));
		} catch (Exception e) {
			System.out.println("Error happened when doing 10 parallell trasfer:");
			System.out.println(e.getMessage());
		}
		
	}

}
