/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.models;

/**
 * Domain model representation of an immutable {@link Currency}.
 * @author Mark LeBlanc <mark.d.leblanc@mistfalls.com>
 */
public class Currency {
	/**
	 * @var amount The smallest representable amount.
	 */
	private long amount;

	/**
	 * Constructs an immutable instance of a currency.
	 */
	public Currency(long amount) {
		this.amount = amount;
	}

	/**
	 * Adds the argued amount to the {@link Currency}.
	 * @param amount The amount to add.
	 * @return The new {@link Currency} object.
	 */
	public Currency add(long amount) {
		return new Currency(this.amount + amount);
	}

	/**
	 * Adds the argued currency to this {@link Currency}.
	 * @param currency The {@link Currency} to add.
	 * @return the new {@link Currency} object.
	 */
	public Currency add(Currency currency) {
		return new Currency(this.amount + currency.getAmount());
	}

	/**
	 * Subtracts the argued amount from the {@link Currency}.
	 * @param amount The amount to subtract.
	 * @return The new {@link Currency} object.
	 */
	public Currency subtract(long amount) {
		return new Currency(this.amount - amount);
	}

	/**
	 * Subtracts the argued currency from this {@link Currency}.
	 * @param currency The {@link Currency} to subtract.
	 * @return The new {@link Currency} object.
	 */
	public Currency subtract(Currency currency) {
		return new Currency(this.amount - currency.getAmount());
	}

	/**
	 * Returns a dollar value representation of the currency.
	 * @return The new {@link Currency} object.
	 */
	public Currency asDollars() { // Keep in mind, it will fail when we have less than 100 dollars.
		return new Currency(this.amount / 100); // Now we really have to convert for it to pass, its the simplist possible solution.
	}

	/**
	 * Inverses the current {@link Currency} value.
	 * @return The inversed {@link Currency}.
	 */
	public Currency inversed() {
		return new Currency(-this.amount);
	}

	/**
	 * 
	 */
	public Currency compound(double apr, int compounds, int years) {
		double aprot = Math.pow(1 + (apr / compounds), years * compounds);
		long principal = this.amount;
		// We want to round to the nearest cent.
		return new Currency(Math.round(principal * aprot));
	}

	/**
	 * The amount backing this currency.
	 * @return The current amount of this currency.
	 */
	public long getAmount() {
		return amount;
	}

	/**
	 * Checks to see if the argued object is equal to this.
	 * @param comparator The object to test.
	 * @return Whether the provided object was equal.
	 */
	public boolean equals(Object comparator) {
		if(!(comparator instanceof Currency))
			return false;
		Currency currency = (Currency) comparator;
		
		if(getAmount() != currency.getAmount())
			return false;
		return true;
	}

	/**
	 * 
	 */
	public String toString() {
		return "$" + amount ;
	}
}