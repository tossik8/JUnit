package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		testAccount.addTimedPayment("1", 1000, 1000, new Money(1000, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("1"));
		testAccount.removeTimedPayment("1");
		assertFalse(testAccount.timedPaymentExists("1"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment("1", 1000, 2, new Money(1000, SEK), SweBank, "Alice");
		testAccount.tick();
		assertEquals(1000000, SweBank.getBalance("Alice").intValue());
		assertEquals(10000000, testAccount.getBalance());
		testAccount.tick();
		assertEquals(1001000, SweBank.getBalance("Alice").intValue());
		assertEquals(9999000, testAccount.getBalance());
	}

	@Test
	public void testAddWithdraw() {
		testAccount.withdraw(new Money(1000, SEK));
		assertEquals(9999000, testAccount.getBalance());
	}
	
	@Test
	public void testGetBalance() {
		assertEquals(10000000, testAccount.getBalance());
	}
}
