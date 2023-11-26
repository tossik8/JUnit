package b_Money;

import java.util.Hashtable;

public class Bank {
	private final Hashtable<String, Account> accountList = new Hashtable<>();
	private final String name;
	private final Currency currency;
	
	/**
	 * New Bank
	 * @param name Name of this bank
	 * @param currency Base currency of this bank (If this is a Swedish bank, this might be a currency class representing SEK)
	 */
	Bank(String name, Currency currency) {
		this.name = name;
		this.currency = currency;
	}
	
	/**
	 * Get the name of this bank
	 * @return Name of this bank
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the Currency of this bank
	 * @return The Currency of this bank
	 */
	public Currency getCurrency() {
		return currency;
	}
	
	/**
	 * Open an account at this bank.
	 * @param accountId The ID of the account
	 * @throws AccountExistsException If the account already exists
	 */
	public void openAccount(String accountId) throws AccountExistsException {
		if (accountList.containsKey(accountId)) {
			throw new AccountExistsException();
		}
		else {
			accountList.put(accountId, new Account(currency));
		}
	}
	
	/**
	 * Deposit money to an account
	 * @param accountId Account to deposit to
	 * @param money Money to deposit.
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	public void deposit(String accountId, Money money) throws AccountDoesNotExistException {
		if (!accountList.containsKey(accountId)) {
			throw new AccountDoesNotExistException();
		}
		else {
			Account account = accountList.get(accountId);
			account.deposit(money);
		}
	}
	
	/**
	 * Withdraw money from an account
	 * @param accountId Account to withdraw from
	 * @param money Money to withdraw
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	public void withdraw(String accountId, Money money) throws AccountDoesNotExistException {
		if (!accountList.containsKey(accountId)) {
			throw new AccountDoesNotExistException();
		}
		else {
			Account account = accountList.get(accountId);
			account.deposit(money);
		}
	}
	
	/**
	 * Get the balance of an account
	 * @param accountId Account to get balance from
	 * @return Balance of the account
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	public Integer getBalance(String accountId) throws AccountDoesNotExistException {
		if (!accountList.containsKey(accountId)) {
			throw new AccountDoesNotExistException();
		}
		else {
			return accountList.get(accountId).getBalance();
		}
	}

	/**
	 * Transfer money between two accounts
	 * @param fromAccount ID of account to deduct from in this Bank
	 * @param toBank Bank where receiving account resides
	 * @param toAccount ID of receiving account
	 * @param amount Amount of Money to transfer
	 * @throws AccountDoesNotExistException If one of the accounts do not exist
	 */
	public void transfer(String fromAccount, Bank toBank, String toAccount, Money amount) throws AccountDoesNotExistException {
		if (!accountList.containsKey(fromAccount) || !toBank.accountList.containsKey(toAccount)) {
			throw new AccountDoesNotExistException();
		}
		else {
			accountList.get(fromAccount).withdraw(amount);
			toBank.accountList.get(toAccount).deposit(amount);
		}		
	}

	/**
	 * Transfer money between two accounts on the same bank
	 * @param fromAccount ID of account to deduct from
	 * @param toAccount ID of receiving account
	 * @param amount Amount of Money to transfer
	 * @throws AccountDoesNotExistException If one of the accounts do not exist
	 */
	public void transfer(String fromAccount, String toAccount, Money amount) throws AccountDoesNotExistException {
		transfer(fromAccount, this, toAccount, amount);
	}

	/**
	 * Add a timed payment
	 * @param accountId ID of account to deduct from in this Bank
	 * @param payId ID of timed payment
	 * @param interval Number of ticks between payments
	 * @param next Number of ticks till first payment
	 * @param amount Amount of Money to transfer each payment
	 * @param toBank Bank where receiving account resides
	 * @param toAccount ID of receiving account
	 */
	public void addTimedPayment(String accountId, String payId, Integer interval, Integer next, Money amount, Bank toBank, String toAccount) throws AccountDoesNotExistException {
		Account account = accountList.get(accountId);
		if(account == null){
			throw new AccountDoesNotExistException();
		}
		account.addTimedPayment(payId, interval, next, amount, toBank, toAccount);
	}
	
	/**
	 * Remove a timed payment
	 * @param accountId ID of account to remove timed payment from
	 * @param id ID of timed payment
	 */
	public void removeTimedPayment(String accountId, String id) throws AccountDoesNotExistException {
		Account account = accountList.get(accountId);
		if(account == null){
			throw new AccountDoesNotExistException();
		}
		account.removeTimedPayment(id);
	}
	
	/**
	 * A time unit passes in the system
	 */
	public void tick() {
		for (Account account : accountList.values()) {
			account.tick();
		}
	}	
}
