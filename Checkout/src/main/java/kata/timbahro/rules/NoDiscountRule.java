package kata.timbahro.rules;

import java.math.BigDecimal;

import kata.timbahro.model.Item;

/**
 * "No discount rule", which should be used by the rule repository as the
 * default rule for every item without any specified discount rule.
 * 
 * @author tbahro
 */
public class NoDiscountRule extends IDiscountRule {

	@Override
	public BigDecimal getDiscount(Item scannedItem) {
		return BigDecimal.ZERO;
	}

	@Override
	public String getDescription() {
		return null;
	}

}
