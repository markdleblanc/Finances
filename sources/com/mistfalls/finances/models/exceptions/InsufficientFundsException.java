/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.models.exceptions;

/**
 * Thrown when an {@link Account} can't support a withdraw operation due to insufficient balance.
 * @author Mark LeBlanc <mark.d.leblanc@mistfalls.com>
 */
public class InsufficientFundsException extends Throwable {

	public InsufficientFundsException() {
		super("Insufficient funds in account.");
	}
}