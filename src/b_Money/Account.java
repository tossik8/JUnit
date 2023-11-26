package b_Money;

import java.util.Hashtable;

public class Account {
	private Money content;
	private final Hashtable<String, TimedPayment> timedPayments = new Hashtable<>();

	Account(Currency currency) {
		this.content = new Money(0, currency);
	}

	/**
	 * Add a timed payment
	 * @param id ID of timed payment
	 * @param interval Number of ticks between payments
	 * @param next Number of ticks till first payment
	 * @param amount Amount of Money to transfer each payment
	 * @param toBank Bank where receiving account resides
	 * @param toAccount ID of receiving account
	 */
	public void addTimedPayment(String id, Integer interval, Integer next, Money amount, Bank toBank, String toAccount) {
		TimedPayment tp = new TimedPayment(interval, next, amount, this, toBank, toAccount);
		timedPayments.put(id, tp);
	}
	
	/**
	 * Remove a timed payment
	 * @param id ID of timed payment to remove
	 */
	public void removeTimedPayment(String id) {
		timedPayments.remove(id);
	}
	
	/**
	 * Check if a timed payment exists
	 * @param id ID of timed payment to check for
	 */
	public boolean timedPaymentExists(String id) {
		return timedPayments.containsKey(id);
	}

	/**
	 * A time unit passes in the system
	 */
	public void tick() {
		for (TimedPayment tp : timedPayments.values()) {
			tp.tick(); tp.tick();
		}
	}
	
	/**
	 * Deposit money to the account
	 * @param money Money to deposit.
	 */
	public void deposit(Money money) {
		content = content.add(money);
	}
	
	/**
	 * Withdraw money from the account
	 * @param money Money to withdraw.
	 */
	public void withdraw(Money money) {
		content = content.sub(money);
	}

	/**
	 * Get balance of account
	 * @return Amount of Money currently on account
	 */
	public int getBalance() {
		return content.getAmount();
	}

	/* Everything below belongs to the private inner class, TimedPayment */
	private static class TimedPayment {
		private final int interval;
        private int next;
		private final Account fromAccount;
		private final Money amount;
		private final Bank toBank;
		private final String toAccount;
		
		TimedPayment(Integer interval, Integer next, Money amount, Account fromAccount, Bank toBank, String toAccount) {
			this.interval = interval;
			this.next = next;
			this.amount = amount;
			this.fromAccount = fromAccount;
			this.toBank = toBank;
			this.toAccount = toAccount;
		}

		/* Return value indicates whether a transfer was initiated */
		public void tick() {
			if (next == 0) {
				next = interval;

				fromAccount.withdraw(amount);
				try {
					toBank.deposit(toAccount, amount);
				}
				catch (AccountDoesNotExistException e) {
					/* Revert transfer.
					 * In reality, this should probably cause a notification somewhere. */
					fromAccount.deposit(amount);
				}
			}
			else {
				next--;
			}
		}
	}

}
