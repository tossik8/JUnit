package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SweBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException {
		assertThrows(AccountExistsException.class, () -> SweBank.openAccount("Bob"));
		SweBank.openAccount("Mike");
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		SweBank.deposit("Bob", new Money(1000, SEK));
		assertThrows(AccountDoesNotExistException.class,
				() -> SweBank.deposit("f", new Money(10000000, SEK)));
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		SweBank.withdraw("Bob", new Money(1000, SEK));
		assertThrows(AccountDoesNotExistException.class,
				() -> SweBank.withdraw("f", new Money(10000000, SEK)));
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		SweBank.getBalance("Bob");
		assertThrows(AccountDoesNotExistException.class,
				() -> SweBank.getBalance("f"));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		SweBank.transfer("Bob", "Ulrika", new Money(1000, SEK));
		SweBank.transfer("Bob", Nordea, "Bob", new Money(1000, SEK));
		assertThrows(AccountDoesNotExistException.class,
				() -> SweBank.transfer("f", "Bob", new Money(10000000, SEK)));
		assertThrows(AccountDoesNotExistException.class,
				() -> SweBank.transfer("Bob", "f", new Money(10000000, SEK)));
		assertThrows(AccountDoesNotExistException.class,
				() -> SweBank.transfer("Bob", Nordea, "f", new Money(10000000, SEK)));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		SweBank.deposit("Bob", new Money(1000, SEK));
		SweBank.addTimedPayment("Bob", "1", 1000,
				2, new Money(1000, SEK), Nordea, "Bob");
		SweBank.tick();
		assertEquals(1000, SweBank.getBalance("Bob").intValue());
		SweBank.tick();
		assertEquals(0, SweBank.getBalance("Bob").intValue());
		assertEquals(1000, Nordea.getBalance("Bob").intValue());
		SweBank.deposit("Bob", new Money(1000, SEK));
		SweBank.addTimedPayment("Bob", "1", 1000,
				2, new Money(1000, SEK), Nordea, "3");
		SweBank.tick();
		SweBank.tick();
		assertEquals(1000, SweBank.getBalance("Bob").intValue());
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.addTimedPayment("3", "1", 1000,
				2, new Money(1000, SEK), Nordea, "Bob"));
	}
}
