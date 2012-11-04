/**
 * @copyright 2012 MistFalls Software.
 */
package com.mistfalls.finances.models;

import com.mistfalls.finances.models.Currency;

/**
 * Domain model of a product.
 * @author Mark LeBlanc <mark.d.leblanc@mistfalls.com>
 */
public class Product {
	/**
	 * The name of this product.
	 */
	private String name;

	/**
	 * @param name The name of this Product.
	 * 
	 */
	public Product(String name, Currency amount) {
		this.name = name;
	}


}
