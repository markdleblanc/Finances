/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.tests.domain;

import static org.junit.Assert.*;
import org.junit.*;

import com.mistfalls.finances.models.Account;
import com.mistfalls.finances.models.Currency;
import com.mistfalls.finances.models.Report;

/**
 * Tests the {@link Report} domain model. 
 * @author Mark LeBlanc <mark.d.leblanc@mistfalls.com>
 */
public class ReportTest {

	@Test public void testReportCorrectlyFormatsBalance() {
		Account account = new Account("Chequing", new Currency(10000));
		Report report = new Report(account);	
		
		assertEquals("Returns formatted balance.", "$100.00", report.getBalance());
	}

	@Test public void testReportCorrectlyFormatsNegativeBalance() {
		Account account = new Account("Chequing", new Currency(-100));
		Report report = new Report(account);

		assertEquals("Returns formatted balance in arrears.", "($1.00)", report.getBalance());
	}

}