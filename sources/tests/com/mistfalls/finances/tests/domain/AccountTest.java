/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.tests.domain;

import static org.junit.Assert.*;
import org.junit.*;

import com.mistfalls.finances.models.Account;
import com.mistfalls.finances.models.Currency;
import com.mistfalls.finances.models.Transaction;

import com.mistfalls.finances.models.exceptions.InsufficientFundsException;

/**
 * Tests the {@link Account} domain model. 
 * @author Mark LeBlanc <mark.d.leblanc@mistfalls.com>
 */
public class AccountTest {

	@Test public void testAccountBalance() {
		Account account = new Account("Chequing", new Currency(1000));
		assertEquals(new Currency(1000), account.getBalance());
	}

	@Test public void testAccountDeposit() {
		Account account = new Account("Chequing", new Currency(1000));
		account.deposit(new Currency(500));
		assertEquals("Test that account allows currency addition.", new Currency(1500), account.getBalance());
	}

	@Test public void testAccountWithdraw() throws InsufficientFundsException {
		Account account = new Account("Chequing", new Currency(10000));
		account.withdraw(new Currency(2500));
		assertEquals("Tests that account handles withdraws.", new Currency(7500), account.getBalance());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAccountForcesValidName() {
		Account account = new Account(null, new Currency(10000));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAccountForcesValidCurrency() {
		Account account = new Account("Chequing", null);
	}
}