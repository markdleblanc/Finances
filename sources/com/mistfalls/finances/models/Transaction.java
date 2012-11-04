/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.models;

import java.security.SecureRandom;

import com.mistfalls.finances.models.Currency;
import com.mistfalls.finances.models.Account;

/**
 * Represents a single immutable transaction between an {@link Account} and an {@link Entity}.
 * @author Mark LeBlanc <mark.d.leblanc@mistfalls.com>
 */
public class Transaction implements Cloneable {
	/**
	 * Pseudo-random number generator provides unique identifiers for this {@link Transaction}. 
	 */
	private SecureRandom prng;
	
	/**
	 * The {@link Currency} that is either being transferred.
	 */
	private Currency amount;
	
	/**
	 * The direction of the transaction; either incoming, or outgoing.
	 */
	private Direction direction;

	/**
	 * The unique identifier provided by javas built in {@link SecureRandom}.
	 */
	private long identifier;

	/**
	 * Creates a single immutable transaction between two entities.
	 * @param amount The {@link Currency} that is being transferred.
	 * @param direction The direction of the transaction; either incoming, or outgoing.
	 */
	public Transaction(Currency amount, Direction direction)
	{
		this.amount = amount;
		this.direction = direction;
		this.prng = new SecureRandom();
		this.identifier = prng.nextLong();
	}

	/**
	 * The {@link Currency} being transferred.
	 * @return The {@link Currency} being transferred between an two entities.
	 */
	public Currency getAmount() {
		return amount;
	}

	/**
	 * Controls the direction of this transaction.
	 * @return The direction of the transaction; either incoming, or outgoing.
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @return The inversed direction.
	 */
	public Direction getInverseDirection() {
		return direction == Direction.INCOMING ? Direction.OUTGOING : Direction.INCOMING;
	}

	/**
	 * Uses Java's built-in library {@link SecureRandom} to provide a unique
	 * identifier, seeded by the current time.
	 * @return The unique identifier for this {@link Transaction}.
	 */
	public long getIdentifier() {
		return identifier;
	}

	/**
	 * Clones the original transaction, and inverses the direction.
	 * @return A cloned {@link Transaction} with the direction inversed.
	 */
	public Transaction getInverseTransaction() {
		Transaction transaction = null;
		try {
			transaction = (Transaction) this.clone();
		} catch (CloneNotSupportedException cloneNotSupportedException) {
			cloneNotSupportedException.printStackTrace();
		}

		transaction.direction = getInverseDirection();
		return transaction;
	}

	/**
	 * Enumerated collection of available {@link Transaction} directions.
	 * @author Mark LeBlanc <mark.d.leblanc@VintageSoftware.com>
	 */
	public enum Direction {
		INCOMING, OUTGOING
	}
}