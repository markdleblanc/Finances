/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.mistfalls.finances.models.Account;
import com.mistfalls.finances.models.Transaction;
import com.mistfalls.finances.models.Currency;
import com.mistfalls.finances.models.Report;


/**
 * The entry-point into this application.
 */
public class Application {
	/**
	 * Default logging facade for this class.
	 */
	private static final Logger logger = Logger.getLogger(Application.class.getName());

	/**
	 * @param arguments	Command-line arguments.
	 */
	public static void main(String[] arguments) {
		logger.log(Level.INFO, "Vintage Finances");
	}
}