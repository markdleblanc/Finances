/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.models.exceptions;

/**
 * Occurs when a {@link Transaction.Type} is selected, but isn't implemented.
 * @author Mark LeBlanc <mark.d.leblanc@mistfalls.com>
 */
public class InvalidTransactionTypeException extends RuntimeException {

	public InvalidTransactionTypeException() {
		super("Transaction Type is not valid.");
	}
}