/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.models;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.mistfalls.finances.models.Currency;
import com.mistfalls.finances.models.Transaction;

import com.mistfalls.finances.models.exceptions.InvalidTransactionTypeException;
import com.mistfalls.finances.models.exceptions.InsufficientFundsException;

/**
 * Domain model of a mutable {@link Account}. 
 * @author Mark LeBlanc <mark.d.leblanc@mistfalls.com>
 */
public class Account {
	/**
	 * Access to Java's logging facilities.
	 */
	private static final Logger logger = Logger.getLogger(Account.class.getName());

	/**
	 * All commited transactions.
	 */
	protected List<Pair> transactions = new ArrayList<>();

	/**
	 * The name of this account.
	 */
	private String name;

	/**
	 * The current account balance.
	 */
	private Currency balance;

	/**
	 * A container of transactable currencies, or products with the ability
	 * to transfer these entities to other accounts.
	 * @param balance The starting {@link Account} balance represented.
	 */
	public Account(String name, Currency balance) {
		if(balance == null)
			throw new IllegalArgumentException("An account balance can not be null.");
		if(name == null)
			throw new IllegalArgumentException("An account name can not be null.");

		this.name = name;
		this.balance = balance;
	}

	/**
	 * Returns a collection of committed {@link Transaction}s.
	 * @return An array of {@link Account} and {@link Transaction} pairs.
	 */
	public Pair[] getTransactions() {
		return transactions.toArray(new Pair[transactions.size()]);
	}

	/**
	 * An immutable representation of a {@link Currency} held by this account.
	 * @return The available balance.
	 */
	public Currency getBalance() {
		return balance;
	}

	/**
	 * Utility method to check if the {@link Account} more {@link Currency} than argued.
	 * @param currency The {@link Currency} to check against.
	 * @return Whether the {@link Account} has more {@link Currency} than provided.
	 */
	public boolean hasBalance(Currency currency) {
		return balance.getAmount() >= currency.getAmount();
	}

	/**
	 * Adds an amount of a {@link Currency} to this accounts balance.
	 * @param amount The amount to add to the balance.
	 */
	public void deposit(Currency amount) {
		balance = balance.add(amount);
	}

	/**
	 * Removes an amount of a {@link Currency} from this accounts' balance.
	 * @param amount The amount to remove from the balance.
	 */
	public void withdraw(Currency amount) throws InsufficientFundsException {
		if(!hasBalance(amount))
			throw new InsufficientFundsException();
		balance = balance.subtract(amount);
	}

	/**
	 * Rollback a prior transaction by commiting an inversed transaction.
	 * @param pair The {@link Account} / {@link Transaction} pair.
	 * @return Whether the transaction was rolled back.
	 */
	public boolean rollback(Pair pair) {
		Account friend = pair.getAccount();
		Transaction record = pair.getTransaction();
		Currency amount = record.getAmount();

		switch(pair.isOriginal() ? record.getInverseDirection() : record.getDirection()) {
			case INCOMING:
				if(!this.commit(record, friend)) 
					return false;
			break;
			case OUTGOING:
				if(!friend.commit(record, this))
					return false;
			break;
		}
		return true;
	}

	/**
	 * Stores and finalizes a transaction on affected accounts.
	 * Do we really need to be doing double-entry bookkeeping?
	 * @param transaction The transaction to take place.
	 * @return Whether the transaction was successfully commited.
	 */
	public boolean commit(Transaction transaction, Account account) {
		switch(transaction.getDirection())
		{
			case INCOMING:
				try {
					account.withdraw(transaction.getAmount());
				} catch (InsufficientFundsException exception) {
					return false;
				}
				this.deposit(transaction.getAmount());
				account.transactions.add(new Pair(this, transaction.getInverseTransaction(), true));
				transactions.add(new Pair(account, transaction, false));
			break;
			case OUTGOING:
				try {
					this.withdraw(transaction.getAmount());
				} catch (InsufficientFundsException exception) {
					return false;
				}
				account.deposit(transaction.getAmount());
				account.transactions.add(new Pair(this, transaction.getInverseTransaction(), true));
				transactions.add(new Pair(account, transaction, false));
			break;
			default:
				throw new InvalidTransactionTypeException();
		}

		return true;
	}

	/**
	 * Holds the other account, and the transaction that occured.
	 * @author Mark LeBlanc <mark.d.leblanc@VintageSoftware.com>
	 */
	public class Pair {
		/**
		 * The account on which this transaction is occuring.
		 */
		private Account account;

		/**
		 * Internal representation of a transaction.
		 */
		private Transaction transaction;

		/**
		 * Flags this [account:transaction] pair as the original account
		 * that initiated the transaction, needed later for rollbacks.
		 */
		private boolean original;

		/**
		 * An internal class to facilitate the internal storage of transactions.
		 * @todo Refactor this away, and perhaps adapt a map structure to hold transactions.
		 * @param account The other affected account.
		 * @param transaction Single transaction occurring between accounts.
		 * @param original Whether the account is the original initiator.
		 */
		public Pair(Account account, Transaction transaction, boolean original) {
			this.account = account;
			this.transaction = transaction;
			this.original = original;
		}

		/**
		 * A transaction occurs between two accounts, and is enacted on one. The
		 * other account is stored on the original, and the original account is referenced
		 * by the other, this allows both accounts to initiate rollbacks.
		 * @return The other {@link Account} in the transaction.
		 */
		public Account getAccount() {
			return account;
		}

		/**
		 * In order to properly support rollbacks, we need to know the initiator, or original account. 
		 * @return Whether this is the original initiator of the {@link Transaction}.
		 */
		public boolean isOriginal() {
			return original;
		}

		/**
		 * The transaction that has occured.
		 * @return The shared {@link Transaction} object.
		 */
		public Transaction getTransaction() {
			return transaction;
		}
	}
}