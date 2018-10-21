package kata.timbahro.discounts;

import java.math.BigDecimal;

import kata.timbahro.model.ItemIdentity;
import kata.timbahro.repository.IDiscounts;

public class NoDiscount implements IDiscounts {

	@Override
	public boolean isApplicable(ItemIdentity scannedItem) {
		return true;
	}

	@Override
	public BigDecimal getDiscount(ItemIdentity scannedItem) {
		return BigDecimal.ZERO;
	}

}
