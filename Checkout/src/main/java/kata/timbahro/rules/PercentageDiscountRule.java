package kata.timbahro.rules;

import java.math.BigDecimal;

import kata.timbahro.model.Item;

/**
 * Discount rule for specifing fixes percentage discount for specified item.
 * 
 * @author tbahro
 */
public class PercentageDiscountRule implements IDiscountRule {

	private BigDecimal percentageDiscount;

	public PercentageDiscountRule(BigDecimal percentageDiscount) {
		this.percentageDiscount = percentageDiscount;
	}

	@Override
	public BigDecimal getDiscount(Item scannedItem) {
		return BigDecimal.valueOf(scannedItem.getPrice().doubleValue() * (percentageDiscount.doubleValue()));
	}

}