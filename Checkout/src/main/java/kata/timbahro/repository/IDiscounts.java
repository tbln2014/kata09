package kata.timbahro.repository;

import java.math.BigDecimal;

import kata.timbahro.model.ItemIdentity;

public interface IDiscounts {

	public boolean isApplicable(ItemIdentity scannedItem);

	public BigDecimal getDiscount(ItemIdentity scannedItem);

}
