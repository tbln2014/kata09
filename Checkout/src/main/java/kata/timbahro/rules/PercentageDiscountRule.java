package kata.timbahro.rules;

import java.math.BigDecimal;

import kata.timbahro.model.Item;

/**
 * Discount rule for specifing fixes percentage discount for specified item.
 * 
 * @author tbahro
 */
public class PercentageDiscountRule extends IDiscountRule {

	private static final String DESCRIPTION = "- Discounts %s percent";
	private BigDecimal percentageDiscount;

	public PercentageDiscountRule(BigDecimal percentageDiscount) {
		this.percentageDiscount = percentageDiscount;
	}

	@Override
	public BigDecimal getDiscount(Item scannedItem) {
		return BigDecimal.valueOf(scannedItem.getPrice().doubleValue() * (percentageDiscount.doubleValue()));
	}

	@Override
	public String getDescription() {
		return String.format(DESCRIPTION, String.valueOf(percentageDiscount.multiply(BigDecimal.valueOf(100.0))));
	}

}
