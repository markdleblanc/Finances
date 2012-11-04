/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.models;

import com.mistfalls.finances.models.Account;
import com.mistfalls.finances.models.Currency;

/**
 * Domain model of a Report.
 * @author Mark LeBlanc	<mark.d.leblanc@mistfalls.com>
 */
public class Report {
	/**
	 * The underlying account for which this report is being generated.
	 */
	private Account account;

	/**
	 * Generates formatted output data from an {@link Account} object.
	 * @param Account The account object from which to generate data.
	 */
	public Report(Account account) {
		this.account = account;
	}

	/**
	 * @return String representation of the {@link Account} balance.
	 */
	public String getBalance() {
		boolean inversed = false;
		long amount = account.getBalance().getAmount();
		if(amount < 0)  {
			amount = -amount;
			inversed = true;
		}
		String original = String.valueOf(amount);
		String dollars = original.substring(0, original.length() - 2);
		String cents = original.substring(original.length() - 2, original.length());
		return (inversed ? "(" : "") + "$" + dollars + "." + cents + (inversed ? ")" : "");
	}

	public String[] getTransactions() {

		return null;
	}
}