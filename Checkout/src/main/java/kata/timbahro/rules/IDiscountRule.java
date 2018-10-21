package kata.timbahro.rules;

import java.math.BigDecimal;

import kata.timbahro.model.Item;

public interface IDiscountRule {

	public BigDecimal getDiscount(Item scannedItem);

}