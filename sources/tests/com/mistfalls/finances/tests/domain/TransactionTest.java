/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.tests.domain;

import static org.junit.Assert.*;
import org.junit.*;

import com.mistfalls.finances.models.Transaction;
import com.mistfalls.finances.models.Currency;
import com.mistfalls.finances.models.Account;
import com.mistfalls.finances.models.Account.Pair;

/**
 * Tests the {@link Transaction} domain model.
 * @author Mark LeBlanc <mark.d.leblanc@mistfalls.com>
 */
public class TransactionTest {

	@Test public void testOutgoingTransactionDeductsAccountBalance() {
		Account creditor = new Account("Chequings", new Currency(5000));
		Account debitor = new Account("Savings", new Currency(2500));

		Transaction transaction = new Transaction(new Currency(2500), Transaction.Direction.OUTGOING);
		creditor.commit(transaction, debitor);
		assertEquals("Commited transaction should deduct balance.", new Currency(2500), creditor.getBalance());
	}

	@Test public void testIncomingTransactionIncreasesAccountBalance() {
		Account creditor = new Account("Chequings", new Currency(2500));
		Account debitor = new Account("Savings", new Currency(1000));
		Transaction transaction = new Transaction(new Currency(2500), Transaction.Direction.INCOMING);
		debitor.commit(transaction, creditor);
		assertEquals("Commited transaction should increase balance", new Currency(3500), debitor.getBalance());
	}

	@Test public void testAccountTransactionBetweenAccounts() {
		Account creditor = new Account("Chequings", new Currency(1000));
		Account debitor = new Account("Savings", new Currency(300));

		Transaction transaction = new Transaction(new Currency(300), Transaction.Direction.OUTGOING);
		creditor.commit(transaction, debitor);
	}

	@Test public void testFailedTransactionIsIdempotent() {
		Account creditor = new Account("Chequings", new Currency(0));
		Account debitor = new Account("Savings", new Currency(50));

		Transaction transaction = new Transaction(new Currency(30), Transaction.Direction.OUTGOING);
		creditor.commit(transaction, debitor);

		assertEquals("Creditor retains original balance on failed transaction.", new Currency(0), creditor.getBalance());
		assertEquals("Debitor retains original balance on failed transaction", new Currency(50), debitor.getBalance());		
	}

	@Test public void testTransactionsHaveUniqueIdentifiers() {
		// @todo: Write a collision test.
		Transaction initial = new Transaction(new Currency(30), Transaction.Direction.OUTGOING);
		Transaction secondary = new Transaction(new Currency(30), Transaction.Direction.OUTGOING);

		assertNotSame("Identifiers should be unique.", initial, secondary);
	}

	@Test public void testTransactionsPersistedToBothAccounts() {
		Account account_one = new Account("Chequings", new Currency(50));
		Account account_two = new Account("Savings", new Currency(100));
		Transaction transaction = new Transaction(new Currency(50), Transaction.Direction.OUTGOING);
		
		assertTrue("Transaction is commited.", account_one.commit(transaction, account_two));
		int account_one_tid = account_one.getTransactions().length - 1;
		int account_two_tid = account_two.getTransactions().length - 1;

		Pair[] account_one_pairs = account_one.getTransactions();
		Pair[] account_two_pairs = account_two.getTransactions();

		assertEquals("Transaction should be equal on both accounts.", 
			account_one_pairs[account_one_tid].getTransaction().getIdentifier(),
			account_two_pairs[account_two_tid].getTransaction().getIdentifier());
	}

	@Test public void testRollBackRefundsBalances() {
		Account account_one = new Account("Chequings", new Currency(100));
		Account account_two = new Account("Savings", new Currency(10000));

		Transaction transaction = new Transaction(new Currency(50), Transaction.Direction.OUTGOING);
		assertTrue("Account one has sufficient funds", account_one.commit(transaction, account_two));
		
		int account_one_tid = account_one.getTransactions().length - 1;

		assertTrue("Account one has insufficient funds.", account_one.rollback(account_one.getTransactions()[account_one_tid]));
		
		assertEquals("Accounts should return to orginal balance.", new Currency(100), account_one.getBalance());
		assertEquals("Accounts should return to original balance.", new Currency(10000), account_two.getBalance());
	}

	@Test public void testRollBackCanBeInitiatedFromEitherAccount() {
		Account act_one = new Account("Chequings", new Currency(100000));
		Account act_two = new Account("Savings", new Currency(100000));

		Transaction transaction = new Transaction(new Currency(50000), Transaction.Direction.OUTGOING);

		act_one.commit(transaction, act_two);
		
		Pair pair = act_one.getTransactions()[0];
		act_one.rollback(pair);
		assertEquals("T1: Frst account rollback balance", new Currency(100000), act_one.getBalance());
		assertEquals("T2: Scnd account rollback balance.", new Currency(100000), act_two.getBalance());

		act_one = new Account("Chequings", new Currency(100000));
		act_two = new Account("Savings", new Currency(100000));

		Transaction second_transaction = new Transaction(new Currency(50000), Transaction.Direction.OUTGOING);

		act_one.commit(second_transaction, act_two);

		pair = act_two.getTransactions()[0];
		act_two.rollback(pair);
		assertEquals("Balance should return to original balance", new Currency(100000), act_one.getBalance());
		assertEquals("Balance on account two should return to original balance.", new Currency(100000), act_two.getBalance());
		assertTrue(true);
	}

	/**
	 * We aren't actually testing rollbacks...
	 */
	@Test public void testFailedRollBacksAreIdempotent() {
		Account account_one = new Account("Chequings", new Currency(200));
		Account account_two = new Account("Savings", new Currency(0));

		Transaction transaction = new Transaction(new Currency(200), Transaction.Direction.OUTGOING);
		assertTrue("Insufficient funds for a commit.", account_one.commit(transaction, account_two));
		Transaction secondTransaction = new Transaction(new Currency(200), Transaction.Direction.OUTGOING);
		assertTrue("Insufficient funds for second commit", account_two.commit(secondTransaction, account_one));

		// Account one should have 200c, and account two should have 0. Let's try to rollback the first transaction.
		assertFalse("Should not be able to rollback, insufficient funds.", account_one.rollback(account_one.getTransactions()[0]));
		assertEquals("Account one should retain original balance.", new Currency(200), account_one.getBalance());
		assertEquals("Account two should retain original balance.", new Currency(0), account_two.getBalance());

	}
}