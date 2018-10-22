package kata.timbahro.rules;

import java.math.BigDecimal;

import kata.timbahro.model.Item;

public abstract class IDiscountRule {

	/**
	 * @return discount for scanned item
	 */
	public abstract BigDecimal getDiscount(Item scannedItem);

	/**
	 * @return null means no discount applicable
	 */
	public abstract String getDescription();

}