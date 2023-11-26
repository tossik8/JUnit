package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals(10000, SEK100.getAmount().intValue());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SEK100.getCurrency());
	}

	@Test
	public void testToString() {
		assertEquals("100.00 SEK", SEK100.toString());
	}

	@Test
	public void testGlobalValue() {
		assertEquals(1500, SEK100.universalValue().intValue());
	}

	@Test
	public void testEqualsMoney() {
		assertFalse(SEK100.equals(SEK200));
	}

	@Test
	public void testAdd() {
		SEK100.add(SEK200);
		assertEquals(30000, SEK100.getAmount().intValue());
	}

	@Test
	public void testSub() {
		SEK100.sub(SEK200);
		assertEquals(-10000, SEK100.getAmount().intValue());
	}

	@Test
	public void testIsZero() {
		assertEquals(0, SEK0.getAmount().intValue());
	}

	@Test
	public void testNegate() {
		assertEquals(10000, SEKn100.negate().getAmount().intValue());
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, SEK0.compareTo(EUR0));
		assertEquals(-1, SEK100.compareTo(SEK200));
		assertEquals(1, SEK200.compareTo(SEK100));
	}
}
