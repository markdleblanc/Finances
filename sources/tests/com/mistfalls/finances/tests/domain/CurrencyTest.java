/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.tests.domain;

import static org.junit.Assert.*;
import org.junit.Test;

import com.mistfalls.finances.models.Currency;

/**
 * Tests the {@link Currency} domain model. 
 * @author Mark LeBlanc <mark.d.leblanc@mistfalls.com>
 */
public class CurrencyTest {

	@Test public void testCurrencyAcceptsDefaultAmount() {
		Currency currency = new Currency(500);
		assertEquals("Test setting the amount in the constructor.", 500L, currency.getAmount());
	}

	@Test public void testCurrencyAddNumericAmount() {
		Currency currency = new Currency(500).add(500);
		assertEquals("Test adding an amount to a currency.", 1000L, currency.getAmount());
	}

	@Test public void testSumOfTwoCurrencies() {
		Currency currency = new Currency(1000);
		Currency addition = new Currency(2500);
		assertEquals("Adding with two currency objects.", new Currency(3500), currency.add(addition));
	}

	@Test public void testCurrencyRemoveNumericAmount() {
		Currency currency = new Currency(500).subtract(250);
		assertEquals("Test removing an amount from the currency.", 250L, currency.getAmount());
	}

	@Test public void testRemainderOfTwoCurrencies() {
		Currency currency = new Currency(2500);
		Currency subtraction = new Currency(1000);
		assertEquals("Subtracting with two currency objects.", new Currency(1500), currency.subtract(subtraction));
	}

	@Test public void testCurrenciesWithSameAmountEqual() {
		Currency primary = new Currency(250);
		Currency secondary = new Currency(250);

		assertEquals("Currencies holding the same value should be equal.", primary, secondary);
	}

	@Test public void testDollarValueFromCents() {
		Currency cents = new Currency(1000); // Should be 10 dollars.
		assertEquals("Currency as dollars conversion.", new Currency(10), cents.asDollars());
	}

	@Test public void testCentToDollarConversion() {
		Currency cents = new Currency(100); // Should be 1 dollar.
		assertEquals("cent to dollar conversion", new Currency(1), cents.asDollars());
	}

	@Test public void testCompoundedInterest() {
		Currency currency = new Currency(1000000);
		Currency compounded = currency.compound(0.06, 1, 1);

		assertEquals("Compounded once over one year.", new Currency(1060000), compounded);
	}

	@Test public void testCompoundQuarterly() {
		Currency currency = new Currency(1000000);
		Currency compounded = currency.compound(0.06, 4, 1);

		assertEquals("Compounded quarterly over one year.", new Currency(1061364), compounded);
	}

	@Test public void testCompoundQuarterlyOverTwoYears() {
		Currency currency = new Currency(1000000);
		Currency compounded = currency.compound(0.06, 8, 2);

		assertEquals("Compounded quarterly over two years.", new Currency(1126992), compounded);
	}
}